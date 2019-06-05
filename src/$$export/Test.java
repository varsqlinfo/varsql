package $$export;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupString;
import org.stringtemplate.v4.ST.DebugState;

public class Test {
	public static void main(String[] args) {
		
		String template = "jdbc:oracle:thin:@{serverip}:{port}:{databaseName}"; 
		
		
		ST bbb = new ST(template, '{', '}');
		//bbb.debugState = new DebugState();
		
		bbb.add("serverip", "11111");
		bbb.add("port", "222");
		bbb.add("databaseName", "33");
		
		
		System.out.println(bbb.render());
	}
}
