package com.varsql.core.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.springframework.util.ResourceUtils;

public class BaseTest {
	public String getResourceContent(String filePath) {
		try {
			return FileUtils.readFileToString(new File(getClass().getResource(filePath).getFile()) ,Charset.defaultCharset());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
