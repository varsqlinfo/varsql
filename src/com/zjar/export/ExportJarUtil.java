package com.zjar.export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.compress.utils.IOUtils;

public class ExportJarUtil {
	
	private File target_file = null;
	
	private final String file_sep = File.separator;
	
	private static final String JAR_VERSION = "-0.0.1";
	
	private static final String EXPORT_PATH = "E:/09.utils/99.yona/apache-tomcat-8.5.23/webapps/vsql/WEB-INF/lib/";
	
	public ExportJarUtil(){}
	
	public void zip(File sourceFile, File targetFile, boolean includeFlag, String addDir, String [] excludeDir) throws IOException {
		
		target_file = targetFile.getAbsoluteFile();	
		
		zip(sourceFile,new FileOutputStream(targetFile), includeFlag, addDir, excludeDir);
	}
	
	public void zip(File sourceFile, OutputStream os,boolean includeFlag , String addDir, String[] excludeDir) throws IOException {
		
		System.out.println("sourceFile : "+ sourceFile);
		
		if(!sourceFile.exists()) throw new IOException("src : ["+sourceFile+"] is not found");
		
		OutputStream out = null;
		BufferedOutputStream bufOut = null;
		JarArchiveOutputStream zipOutput = null;
		
		try {
			out = os;
			bufOut = new BufferedOutputStream(out);
			zipOutput = new JarArchiveOutputStream(bufOut);
			
			zipOutput.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
			zipOutput.setEncoding("UTF-8");
			addFileToZip(zipOutput, sourceFile, addDir, includeFlag , 0 , excludeDir);
		} finally {
			if(zipOutput != null) zipOutput.finish(); 
			if(zipOutput != null) zipOutput.close();
			if(bufOut != null) bufOut.close();
			if(out != null) out.close();
		}
	}
	
	private void addFileToZip(JarArchiveOutputStream zOut, File zipFile,
			String base, boolean includeFlag, int depth, String[] excludeDir) throws IOException {
		
		if(target_file != null)
			if(zipFile.compareTo(target_file) == 0) return; 
		
		base = base.replaceAll("[/\\\\]", java.util.regex.Matcher.quoteReplacement(File.separator));
		
		String entryName = base + zipFile.getName();
		
		boolean addJar = true;
		
		if(depth == 1 && excludeDir !=null){
			String zipFileName =zipFile.getName(); 
			for(String path:excludeDir){
				if(zipFileName.endsWith(path)){
					addJar = false; 
					break ;
				}
			}
		}
		
		if(!addJar) return ; 
		 
		if (zipFile.isFile()) {
			JarArchiveEntry zipEntry = new JarArchiveEntry(entryName);
			zOut.putArchiveEntry(zipEntry);
			zipEntry.setSize(zipFile.length());
			
			IOUtils.copy(new FileInputStream(zipFile), zOut);
			zOut.closeArchiveEntry();
			
		} else {
			if(!includeFlag){
				entryName = "";
				includeFlag = true; 
			}else{
				entryName = entryName + file_sep;
			}
			if(!"".equals(entryName)){
				JarArchiveEntry zipEntry = new JarArchiveEntry(entryName);
				zOut.putArchiveEntry(zipEntry);
			}
			
			File[] children = zipFile.listFiles();

			if (children != null) {
				for (File child : children) {
					addFileToZip(zOut, new File(child.getAbsolutePath()), entryName, includeFlag, depth+1 , excludeDir);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("start jar create");
			String rootPath = new File("").getAbsolutePath(); 
			ExportJarUtil zu= new ExportJarUtil();
			
			zu.zip(new File(rootPath+"/WebContent/WEB-INF/classes"), new File(EXPORT_PATH+"varsql-app"+JAR_VERSION+".jar"), false,"" , null);
			zu.zip(new File(rootPath+"/WebContent/webstatic"), new File(EXPORT_PATH+"varsql-static"+JAR_VERSION+".jar"), true,"META-INF/resources/", null);
			
			String[] excludeDirOrPath = {"lib","classes"}; 
			zu.zip(new File(rootPath+"/WebContent/WEB-INF"), new File(EXPORT_PATH+"varsql-web"+JAR_VERSION+".jar"), true ,"META-INF/resources/" ,excludeDirOrPath);
			
			System.out.println("end jar create");
			System.out.println("export version  : " + JAR_VERSION);
			System.out.println("export jar path : " + EXPORT_PATH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
