package com.example.demo;


import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileFunction {

	@Value("${file.Upload_DIR}")
	private String Upload_DIR;
	
	
	public String UploadFile(String fileName) {
		
		return Upload_DIR+fileName;
	}
	
	public boolean DeleteFile(String fileName) {
		
		String path=Upload_DIR+fileName;
		File f=new File(path);
		
		return f.delete();
	}
	
	
}
