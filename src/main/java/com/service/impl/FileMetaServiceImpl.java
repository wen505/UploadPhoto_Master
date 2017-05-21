package com.service.impl;

import com.common.exception.BusinessException;
import com.common.util.ConstantUtil;
import com.dao.FileMetaMapper;
import com.model.FileMeta;
import com.service.FileMetaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service("fileMetaService")
public class FileMetaServiceImpl implements FileMetaService{
    @Resource
    private FileMetaMapper fileMetaMapper;
    @Override
    public int addFileMeta(FileMeta fileMeta) {
        int res = fileMetaMapper.insert(fileMeta);
        if(res <= 0){
            throw new BusinessException("保存文件信息异常", ConstantUtil.BUSSINESS_ERROR_CODE);
        }
        return res;
    }

    @Override
    public int updateFileMeta(FileMeta fileMeta) {
        int res = fileMetaMapper.updateByPrimaryKey(fileMeta);
        if(res <= 0){
            throw new BusinessException("更新文件信息异常", ConstantUtil.BUSSINESS_ERROR_CODE);
        }
        return res;
    }

    @Override
    public int deleteFileMeta(String fileId) {
        int res = fileMetaMapper.deleteByPrimaryKey(fileId);
        if(res <= 0){
            throw new BusinessException("删除文件信息异常", ConstantUtil.BUSSINESS_ERROR_CODE);
        }
        return res;
    }

    @Override
    public FileMeta findFileMeta(String fileid) {
        FileMeta fileMeta = fileMetaMapper.selectByPrimaryKey(fileid);
        return fileMeta;
    }
}
