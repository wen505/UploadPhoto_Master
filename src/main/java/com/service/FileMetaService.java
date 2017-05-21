package com.service;

import com.model.FileMeta;

import java.util.List;

public interface FileMetaService {

    public int addFileMeta(FileMeta fileMeta);

    public int updateFileMeta(FileMeta fileMeta);

    public int deleteFileMeta(String fileid);

    public FileMeta findFileMeta(String fileid);

}
