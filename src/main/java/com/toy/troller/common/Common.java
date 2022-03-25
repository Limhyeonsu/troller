package com.toy.troller.common;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class Common {
	public static String saveFile(String path, MultipartFile file, boolean extension) {
		long time = System.currentTimeMillis();
		String saveNm = String.valueOf(time);
		
		if(extension) {
			saveNm += "." + FilenameUtils.getExtension(file.getOriginalFilename());
		}
		
		try {
		
			InputStream input = file.getInputStream();
			String filePath = path + saveNm;
			
			File target = new File(filePath);
			
			FileUtils.copyInputStreamToFile(input, target);
			
			input.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return saveNm;
	}
}
