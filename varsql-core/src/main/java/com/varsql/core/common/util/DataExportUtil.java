package com.varsql.core.common.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.varsql.core.common.constants.VarsqlConstants;

public class DataExportUtil {

	/**
	 *
	 * @Method Name  : toTextWrite
	 * @Method 설명 : text download
	 * @작성일   : 2019. 4. 9.
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param downText
	 * @param output
	 * @throws IOException
	 */
	public static void toTextWrite(String downText ,OutputStream output) throws IOException{
		try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter (output, VarsqlConstants.CHAR_SET));) {
			out.write(downText);
			out.close();
		}
	}
}
