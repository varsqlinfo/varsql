package com.varsql.core.data.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.stream.XMLStreamException;

import org.junit.jupiter.api.Test;

import com.varsql.core.db.DBType;


class SQLWriterTest {
	private int loopCount = 1000;
	private long totalTime =0;


	void loopTest() throws XMLStreamException {
		for (int i = 0; i < loopCount; i++) {
			test(i);
		}

		System.out.println("totalTime : "+ totalTime);
		System.out.println("average totalTime : "+ totalTime/loopCount);
		System.out.println("totalTime : "+ (totalTime/loopCount/1000.0));
	}

	@Test
	void test1() {
		test(-1);
	}

	void test(int i2) {

		String fileName = "c:/zzz/insert/insertHuge"+i2+".sql";
		if(i2 < 0) {
			fileName = "c:/zzz/insertHuge.sql";
		}

		fileName = "c:/zzz/insert/insertHuge"+i2+".sql";

		long start = System.currentTimeMillis();
		try(FileOutputStream fos = new FileOutputStream(fileName);
				SQLWriter writer = new SQLWriter(fos, DBType.H2,"test2222")){

			HashMap tmp;
			for( int i =0 ;i <10000; i++){
				tmp = new HashMap();

				tmp.put("NO1", i);
				//tmp.put("NAME", "한글1 "+i);
				tmp.put("DATATYPE", null);
				tmp.put("TYPENAME", "한'''글3 "+i);
				//tmp.put("LENGTH", "aawefe"+i);
				tmp.put("NULLABLE", "aawefe"
						+ "test\nawef awfeawef && awefawef </test>"+i);
				//tmp.put("COMMENT", "aawefe"+i);
				tmp.put("PRIMAY_KEY", "null");
				tmp.put("AUTO_INCREMENT", "aawefe"+i);
				tmp.put("DEFAULTVAL", "aawefe"+i);

				if(i !=0  && i %1000==0) {
					//System.out.println("csv row :" + i);
				}

				writer.addRow(tmp);
			}

			writer.write();

			//System.out.println("complete");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long end = System.currentTimeMillis();

		totalTime += (end - start);
		System.out.println("실행시간\t" + (end - start)/1000.0);


	}

}
