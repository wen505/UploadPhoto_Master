package com.service;

import com.common.vo.RspData;

import java.io.InputStream;

public interface FileOperateService {

    public RspData uploadImage(InputStream inputStream, String filePath, String fileName);

}
