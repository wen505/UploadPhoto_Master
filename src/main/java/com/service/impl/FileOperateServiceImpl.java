package com.service.impl;

import com.common.exception.BusinessException;
import com.common.util.ConstantUtil;
import com.common.util.ImageCompressUtil;
import com.common.vo.RspData;
import com.dao.FileMetaMapper;
import com.model.FileMeta;
import com.service.FileMetaService;
import com.service.FileOperateService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Service("fileOperateService")
public class FileOperateServiceImpl implements FileOperateService {

    @Override
    public RspData uploadImage(InputStream inputStream, String filePath, String fileName) {
        RspData rspData = RspData.issuccess(true);
        try {
            File parentfile = new File(filePath);
            if(!parentfile.exists()){
                parentfile.mkdirs();
                parentfile.setExecutable(false);//设置新建的文件夹执行权限为false
            }
            ImageCompressUtil.saveMinPhoto(inputStream, filePath+ fileName, 0D,  0D);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            rspData = RspData.error(ConstantUtil.SYSTEM_ERROR_CODE, "上传图片异常！");
        } catch (Exception e) {
            e.printStackTrace();
            rspData = RspData.error(ConstantUtil.SYSTEM_ERROR_CODE, "系统异常！");
        }
        return rspData;
    }
}
