package com.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ConcurrentHashMap;

public class FileCheckUtil {
	private static ConcurrentHashMap<String, String> ExtFileTypeMap = new ConcurrentHashMap<String, String>();

	
	static {
		ExtFileTypeMap.put("PDF", "pdf");
		ExtFileTypeMap.put("GIF", "gif");
		ExtFileTypeMap.put("JPEG", "jpg");
		ExtFileTypeMap.put("PNG", "png");
		ExtFileTypeMap.put("XML", "xml");
		ExtFileTypeMap.put("Text", "txt");
		ExtFileTypeMap.put("PDF", "pdf");
		ExtFileTypeMap.put("CSS", "css");
		ExtFileTypeMap.put("JS", "js");
		ExtFileTypeMap.put("HTML", "html");
		ExtFileTypeMap.put("JSON", "json");
		ExtFileTypeMap.put("MP4", "mp4");
	};

	// 根据文件头检查文件类型
	public static String identifyFileType(final String fileName) {
		String fileType = "Undetermined";
		final File file = new File(fileName);
		try {
            System.out.println("file.toPath()==========="+file.toPath());
			fileType = Files.probeContentType(file.toPath());
		} catch (IOException ioException) {
			System.out.println("ERROR: Unable to determine file type for "
					+ fileName + " due to exception " + ioException);
		}

		return fileType;
	}

	// 获取文件扩展名
	public static String getExtension(String path) {
		String ext = com.google.common.io.Files.getFileExtension(path);
		return ext;
	}

	// 比较文件后缀跟内容类型是否相符
	public boolean compareFileExtWithContentType(String fileName) {
		String fileType = identifyFileType(fileName);
        if(fileType != null && fileType.indexOf("/")>-1){
            fileType = fileType.split("/")[1];
        }
		String ext = getExtension(fileName);
		System.out.println("文件名称:" + fileName + " 文件后缀:" + ext + " 跟文件内容类型:"
				+ fileType);
		if (ExtFileTypeMap.containsKey(fileType.toUpperCase())) {
			if (ext.equalsIgnoreCase(ExtFileTypeMap.get(fileType.toUpperCase()))) {
				return true;
			} else {
				System.out.println("文件后缀:" + ext + "跟文件内容类型不一致:" + fileType);
				return false;
			}
		} else {
			System.out.println(fileType + "该文件类型不在支持范围内");
			return false;
		}
	}


}
