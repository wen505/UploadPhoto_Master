package com.dao;

import com.model.FileMeta;

public interface FileMetaMapper {
    int deleteByPrimaryKey(String fileId);

    int insert(FileMeta record);

    int insertSelective(FileMeta record);

    FileMeta selectByPrimaryKey(String fileId);

    int updateByPrimaryKeySelective(FileMeta record);

    int updateByPrimaryKey(FileMeta record);
}