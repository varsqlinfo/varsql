package $$export;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;
import org.stringtemplate.v4.ST.DebugState;

import com.varsql.app.util.CheckUtils;
import com.varsql.app.util.VarsqlUtils;

public class Test {
	public static void main(String[] args) {
		
		String template = "jdbc:oracle:thin:@{serverip}{if(port)}:{port}{endif}:{databaseName}"; 
		
		
		ST bbb = new ST(template, '{', '}');
		//bbb.debugState = new DebugState();
		
		bbb.add("serverip", "11111");
		bbb.add("port", CheckUtils.isNumber("-1")?111:false);
		bbb.add("databaseName", "33");
		
		System.out.println(getValidFileName("C:../../zzz.txt"));
		System.out.println(bbb.render());
	}
	
	public static String getValidFileName(String fileName) {
		fileName = fileName.replace("../", "");
		fileName = fileName.replace("..\\", "");
		
		return fileName; 
	}
}
