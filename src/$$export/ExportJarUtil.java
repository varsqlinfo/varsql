package $$export;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream.UnicodeExtraFieldPolicy;
import org.apache.commons.compress.utils.IOUtils;
/**
 * 
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: ExportJarUtil.java
* @DESC		: jar 생성 util.  
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2016. 11. 15. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class ExportJarUtil {
	
	private File target_file = null;
	
	private final String file_sep = File.separator;
	
	private static final String JAR_VERSION = "-0.0.1";
	
	private static final String EXPORT_PATH = "C:\\01.util\\tomcat\\webapps\\vsql\\WEB-INF\\lib/";
	
	enum JAR_TYPE{
		APP("varsql-app","/WebContent/WEB-INF/classes", Arrays.asList("$$export") ,false ,"")
		,WEB("varsql-web","/WebContent/WEB-INF", Arrays.asList("WEB-INF/lib","WEB-INF/classes","WEB-INF/web.xml","WEB-INF/varsql-fn.tld"), true ,"META-INF/resources/")
		,STATIC("varsql-static","/WebContent/webstatic",null,true ,"META-INF/resources/");
		
		public String jarNamePath ="";
		public String sourcePath ="";
		public List<String> excludeDirOrPath;
		public boolean firstIncludeFlag;
		public String addDir ="";
		
		JAR_TYPE(String name , String path , List<String> excludeDirOrPath, boolean firstIncludeFlag, String addDir){
			String rootPath = new File("").getAbsolutePath(); 
			
			this.jarNamePath = EXPORT_PATH+name+JAR_VERSION+".jar";
			this.sourcePath = rootPath+path;
			this.excludeDirOrPath = excludeDirOrPath;
			this.firstIncludeFlag = firstIncludeFlag; 
			this.addDir = addDir; 
		}
		
		public String toString() {
			return new StringBuilder().append(this.name())
					.append(" jarNamePath [").append(jarNamePath).append(" ]")
					.append(" sourcePath [").append(sourcePath).append(" ]")
					.toString();
		}
	}
	
	public ExportJarUtil(){}
	
	public void zip(JAR_TYPE jarType) throws IOException {
		
		File sourceFile = new File(jarType.sourcePath); 
		
		if(!sourceFile.exists()) throw new IOException("src : ["+sourceFile+"] is not found");
		
		System.out.println("############start "+jarType.name()+ " ##############" );
		System.out.println(jarType);
		
		OutputStream os = new FileOutputStream(jarType.jarNamePath);
		
		boolean includeFlag = jarType.firstIncludeFlag;
		String addDir= jarType.addDir;
		
		String[] excludeDir = null;
		
		if(jarType.excludeDirOrPath!=null){
			excludeDir  = (String[])jarType.excludeDirOrPath.toArray();
		}
		
		OutputStream out = null;
		BufferedOutputStream bufOut = null;
		JarArchiveOutputStream zipOutput = null;
		
		try {
			out = os;
			bufOut = new BufferedOutputStream(out);
			zipOutput = new JarArchiveOutputStream(bufOut);
			
			zipOutput.setCreateUnicodeExtraFields(UnicodeExtraFieldPolicy.ALWAYS);
			zipOutput.setEncoding("UTF-8");
			
			addDir = addDir.replaceAll("[/\\\\]", java.util.regex.Matcher.quoteReplacement(File.separator));
			
			excludeDir = getPathReplace(addDir,excludeDir);
			addFileToZip(zipOutput, sourceFile, addDir, includeFlag , 0 , excludeDir);
			
			if(jarType == JAR_TYPE.WEB){
				addWebMetafile(zipOutput);
			}
		} finally {
			if(zipOutput != null) zipOutput.finish(); 
			if(zipOutput != null) zipOutput.close();
			if(bufOut != null) bufOut.close();
			if(out != null) out.close();
		}
	}
	
	/**
	 * 
	 * @Method Name  : addWebMetafile
	 * @Method 설명 : tld 파일 추가 하기.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 16. 
	 * @변경이력  :
	 * @param zOut
	 * @throws IOException
	 */
	public void addWebMetafile(JarArchiveOutputStream zOut) throws IOException{
		String sourcePath = JAR_TYPE.WEB.sourcePath;
		
		
		File file = new File(sourcePath+File.separator);
		
		File [] tldFile = file.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File arg0, String arg1) {
				if(arg1.endsWith(".tld")){
					return true; 
				}
				return false;
			}
		});
		
		String addPath =pathReplace("META-INF/resources/"); 
		
		for (int i = 0; i < tldFile.length; i++) {
			File sourceFile = tldFile[i];
			
			String entryName = addPath+ sourceFile.getName();
			
			JarArchiveEntry zipEntry = new JarArchiveEntry(entryName);
			zOut.putArchiveEntry(zipEntry);
			zipEntry.setSize(sourceFile.length());
			
			IOUtils.copy(new FileInputStream(sourceFile), zOut);
			zOut.closeArchiveEntry();
		}
	}

	/**
	 * 
	 * @Method Name  : addFileToZip
	 * @Method 설명 : jar 에 파일 추가.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 16. 
	 * @변경이력  :
	 */
	private void addFileToZip(JarArchiveOutputStream zOut, File zipFile,
			String base, boolean includeFlag, int depth,  String[] excludeDir) throws IOException {
		
		if(target_file != null)
			if(zipFile.compareTo(target_file) == 0) return; 
		
		String entryName = base + zipFile.getName();
		
		boolean addJar = true;
		
		if(excludeDir !=null){
			
			for(String path:excludeDir){
				if(entryName.startsWith(path)){
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
	
	private String[] getPathReplace(String addDir, String[] excludeDir) {
		if(excludeDir !=null){
			for(int i =0 ;i < excludeDir.length;i++){
				excludeDir[i] = addDir+pathReplace(excludeDir[i]);
			}
		}
		return excludeDir;
	}
	
	private String pathReplace(String path){
		return path.replaceAll("[/\\\\]", java.util.regex.Matcher.quoteReplacement(File.separator));
	}
	
	public static void main(String[] args) {
		try {
			System.out.println("start jar create");
			 
			ExportJarUtil zu= new ExportJarUtil();
			
			zu.zip(JAR_TYPE.APP);
			zu.zip(JAR_TYPE.WEB);
			zu.zip(JAR_TYPE.STATIC);
			
			System.out.println("end jar create");
			System.out.println("export version  : " + JAR_VERSION);
			System.out.println("export jar path : " + EXPORT_PATH);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
