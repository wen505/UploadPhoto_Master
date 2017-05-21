package com.controller;

import com.alibaba.fastjson.JSON;
import com.common.controller.BaseController;
import com.common.exception.BusinessException;
import com.common.id.BasicEntityIdGenerator;
import com.common.util.ConstantUtil;
import com.common.util.DateUtil;
import com.common.util.FileCheckUtil;
import com.common.vo.RspData;
import com.model.FileMeta;
import com.service.DocumentDetector;
import com.service.DocumentSanitizer;
import com.service.FileMetaService;
import com.service.FileOperateService;
import com.service.impl.ExcelDocumentDetectorImpl;
import com.service.impl.ImageDocumentSanitizerImpl;
import com.service.impl.PdfDocumentDetectorImpl;
import com.service.impl.WordDocumentDetectorImpl;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.*;

import static ij.macro.MacroConstants.LOG;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


/**
 * Created by Administrator on 2016/5/30.
 */
@Controller
@RequestMapping("/ulic")
public class FileMetaController extends BaseController<FileMetaController> {

    @Resource
    private FileMetaService fileMetaService;

    @Resource
    private FileOperateService fileOperateService;

    @RequestMapping("/home")
    public String goHome(){
        return "/index";
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public @ResponseBody String upload(MultipartHttpServletRequest request, HttpServletResponse response) {
        request.getContextPath();
        request.getServletContext().getContextPath();
        LinkedList<FileMeta> files = new LinkedList<FileMeta>();
        FileMeta fileMeta = null;
        Iterator<String> itr =  request.getFileNames();
        MultipartFile mpf = null;
        File tmpFile = null;
        Path tmpPath = null;
        RspData rspData = null;
        try {
            while(itr.hasNext()){
                mpf = request.getFile(itr.next());
                /* 1: 检测文档信息 (file type + file content) */
                // File content
                if ((mpf == null) || (mpf.getInputStream() == null)) {
                    throw new IllegalArgumentException("未获取到文件内容 !");
                }
                // File type
                String fileContentType = mpf.getContentType() == null ? "" : mpf.getContentType();
                if ((fileContentType == null) || (fileContentType.trim().length() == 0)) {
                    throw new IllegalArgumentException("未获取到文件类型 !");
                }
                // 将上传的文件放到临时目录
                tmpFile = File.createTempFile("uploaded-", null);
                tmpPath = tmpFile.toPath();
                long copiedBytesCount = Files.copy(mpf.getInputStream(), tmpPath, StandardCopyOption.REPLACE_EXISTING);
                if (copiedBytesCount != mpf.getSize()) {
                    throw new IOException("将文件放到临时目录出错！");
                }
                /* 2: 为目标文件类型初始化一个检测类，并验证 */
                boolean isSafe = false;
                // 文件检测类
                DocumentDetector documentDetector = null;
                DocumentSanitizer documentSanitizer = null;
                String fileType = "";
                String[] fileTypeArr = fileContentType.split("/");
                if(fileTypeArr[0].indexOf("image") > -1){
                    fileType = "image";
                }else{
                    fileType = fileTypeArr[1];
                }
                switch (ConstantUtil.ExtTypeMap.get(fileType.toLowerCase())) {
                    case "PDF":
                        documentDetector = new PdfDocumentDetectorImpl();
                        isSafe = documentDetector.isSafe(tmpFile);
                        break;
                    case "WORD":
                        documentDetector = new WordDocumentDetectorImpl();
                        isSafe = documentDetector.isSafe(tmpFile);
                        break;
                    case "EXCEL":
                        documentDetector = new ExcelDocumentDetectorImpl();
                        isSafe = documentDetector.isSafe(tmpFile);
                        break;
                    case "IMAGE":
                        documentSanitizer = new ImageDocumentSanitizerImpl();
                        isSafe = documentSanitizer.madeSafe(tmpFile);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknow file type specified !");
                }

                /*3 : 根据检测的结果执行相应的代码 */
                if (!isSafe) {
                    logger.error("检测到不安全的文件，不能上传 !");
                    // 将临时文件移除
                    safelyRemoveFile(tmpPath);
                    rspData = RspData.error(ConstantUtil.SYSTEM_ERROR_CODE, "该文件有问题，不能上传！");
                    String str = JSON.toJSONString(rspData);
                   return "{\"files\":["+str+"]}";
                } else {

                    fileMeta = new FileMeta();
                    fileMeta.setFileName(mpf.getOriginalFilename());
                    fileMeta.setFileSize(mpf.getSize()/1024+" Kb");
                    fileMeta.setFileType(mpf.getContentType());
                    fileMeta.setUploadTime(DateUtil.formatDateToString(new Date()));
                    String fileExtension = FileCheckUtil.getExtension(mpf.getOriginalFilename());
                    BasicEntityIdGenerator basicEntityIdGenerator = new BasicEntityIdGenerator();
                    //生成新的文件名
                    String newFileName = basicEntityIdGenerator.generateLongIdString()+"."+fileExtension;
                    fileMeta.setObjfileName(newFileName);
                    fileMeta.setFileId(basicEntityIdGenerator.generateLongIdString());
                    //读取配置文件
                    Properties prop = new Properties();//属性集合对象
                    String realUploadPath = "";//文件上传正式目录
                    prop.load(FileMetaController.class.getClassLoader().getResourceAsStream("image.properties"));
                    realUploadPath = prop.getProperty("file.realUploadPath");
                    //将文件上传至目录
                    if(realUploadPath != null && !"".equals(realUploadPath)){
                        //将临时文件上传至指定目录
                        rspData = fileOperateService.uploadImage(new FileInputStream(tmpFile),realUploadPath,newFileName);
                        // 将临时文件移除
                        safelyRemoveFile(tmpPath);
                    }
                    fileMeta.setFilePath(realUploadPath+newFileName);
                    fileMeta.setPictureUrl("http:\\\\localhost:9090\\ulic\\picture\\"+fileMeta.getFileId()+".do");
                    fileMeta.setDeleteUrl("/ulic/delete/"+fileMeta.getFileId()+".do");
                    fileMeta.setDeleteType("DELETE");
                    fileMetaService.addFileMeta(fileMeta);
                    files.add(fileMeta);
                }
            }
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            rspData = RspData.error(ConstantUtil.SYSTEM_ERROR_CODE, "文件或文件类型有问题！");
        } catch (IOException e) {
            e.printStackTrace();
            rspData = RspData.error(ConstantUtil.SYSTEM_ERROR_CODE, "系统错误，请联系管理员！");
        }
        String str = "";
        if(rspData != null){
            str = JSON.toJSONString(rspData);
        }else{
            str = JSON.toJSONString(files);
        }
        //[{"deleteType":"DELETE","deleteUrl":"/ulic/delete/866217002871222272.do","fileId":"866217002871222272","fileName":"psb.jpeg","filePath":"D:/Github/uploadFiles/866216997150191616.jpeg","fileSize":"137 Kb","fileType":"image/jpeg","objfileName":"866216997150191616.jpeg","pictureUrl":"http:\\\\localhost:9090\\ulic\\picture\\866217002871222272.do","uploadTime":"2017/05/21 16:59:46"}]
        return "{\"files\":["+str+"]}";
    }

    //删除图片调用方法
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public @ResponseBody String deleteFile(@PathVariable String id) {
        RspData rspData = null;
        FileMeta fileMeta = fileMetaService.findFileMeta(id);
        if(fileMeta != null){
            File imageFile = new File(fileMeta.getFilePath());
            //调用文件删除方法
            rspData = this.safelyRemoveFile(imageFile.toPath());
            fileMetaService.deleteFileMeta(id);
        }
        String str = JSON.toJSONString(rspData);
        return "{\"files\"[:"+str+"]}";
    }

    //预览图片调用方法
    @RequestMapping(value = "/picture/{id}", method = RequestMethod.GET)
    public void picture(HttpServletResponse response, @PathVariable String id) {
        FileMeta fileMeta = fileMetaService.findFileMeta(id);
        File imageFile = new File(fileMeta.getFilePath());
        response.setContentType(fileMeta.getFileType());
        //response.setContentLength(image.getSize().intValue());
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            logger.error("不能显示图片 "+id, e);
        }
    }

    //展示缩略图方法
    @RequestMapping(value = "/thumbnail/{id}", method = RequestMethod.GET)
    public void thumbnail(HttpServletResponse response, @PathVariable String id) {
        FileMeta fileMeta = fileMetaService.findFileMeta(id);
        File imageFile = new File(fileMeta.getFilePath());
        response.setContentType(fileMeta.getFileType());
        response.setContentLength(1);
        try {
            InputStream is = new FileInputStream(imageFile);
            IOUtils.copy(is, response.getOutputStream());
        } catch(IOException e) {
            logger.error("Could not show picture "+id, e);
        }
    }

    /**
     * 删除文件方法
     *
     * @param p file to remove
     */
    private RspData safelyRemoveFile(Path p) {
        RspData rspData = null;
        try {
            if (p != null) {
                // Remove temporary file
                if (!Files.deleteIfExists(p)) {
                    // 如果删除失败，则覆盖内容以进行清理
                    Files.write(p, "-".getBytes("utf8"), StandardOpenOption.CREATE);
                    rspData = RspData.issuccess(true);
                }
            }
        }catch (Exception e) {
            logger.error("不能删除文件 !", e);
            rspData = RspData.error(ConstantUtil.SYSTEM_ERROR_CODE, "系统异常！");
        }
        return rspData;
    }

}
