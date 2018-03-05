package com.varsql.app.util;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.varsql.app.common.beans.DataCommonVO;
import com.varsql.app.common.constants.VarsqlParamConstants;
import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.SecurityUtil;
import com.varsql.core.db.beans.DatabaseInfo;
import com.varsql.core.db.util.DbInstanceFactory;

public class VarsqlUtil {
	
	public static String generateUUID (){
		return UUID.randomUUID().toString().replaceAll("-", ""); 
	}
	
	public static boolean isAjaxRequest(HttpServletRequest request){
		String headerInfo = request.getHeader("X-Requested-With");
		
		if("XMLHttpRequest".equals(headerInfo)){
			return true;
		}else{
			return false; 
		}
	}
	
	public static String getVconnID (HttpServletRequest req){
		return (String) req.getAttribute(VarsqlParamConstants.VCONNID); 
	}
	
	public static String objectToString(Object json) {
		try {
			 ObjectMapper objectMapper = new ObjectMapper();
			 objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
			return objectMapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String objectToKeyLowerString(Object json) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			SimpleModule module = new SimpleModule("LowerCaseKeySerializer", new Version(1,0,0,null));
			module.addKeySerializer(Object.class, new LowerCaseKeySerializer());
			mapper.registerModule(module);
			return mapper.writeValueAsString(json);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 
	 * @Method Name  : setPagingParam
	 * @Method 설명 : 페이징 파라미터 셋팅
	 * @작성일   : 2015. 4. 27. 
	 * @작성자   : ytkim
	 * @변경이력  :
	 * @param paramMap
	 * @return
	 */
	public static DataCommonVO setPagingParam(DataCommonVO paramMap) {
		int page = paramMap.getInt(VarsqlParamConstants.SEARCH_NO, VarsqlParamConstants.SEARCH_DEFAULT_FIRST);
		int rows = paramMap.getInt(VarsqlParamConstants.SEARCH_ROW,VarsqlParamConstants.SEARCH_DEFAULT_ROW);
		
		int first = (page-1)*rows ;
		
		paramMap.put(VarsqlParamConstants.SEARCH_FIRST, first);
		paramMap.put(VarsqlParamConstants.SEARCH_ROW, rows);
		
		return paramMap; 
	}
	
	public static DbInstanceFactory getConnidToDbInstanceFactory(String connid){
		try{
			DatabaseInfo  dbinfo= SecurityUtil.userDBInfo(connid);
			return getDbInstanceFactory(dbinfo.getType());
		}catch(Exception e){
			return DbInstanceFactory.OTHER;
		}
	}
	
	public static DbInstanceFactory getDbInstanceFactory(String type){
		try{
			return DbInstanceFactory.valueOf(type);
		}catch(Exception e){
			return DbInstanceFactory.OTHER;
		}
	}
	
	/**
	 * 
	 * @Method Name  : setResponseDownAttr
	 * @Method 설명 : 파일 다운로드  송성 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 23. 
	 * @변경이력  :
	 * @param res
	 * @param fileName
	 */
	public static void setResponseDownAttr(HttpServletResponse res, String fileName){
		res.setContentType("application/octet-stream");
		res.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\";",fileName));
		res.setCharacterEncoding(VarsqlConstants.CHAR_SET);
		res.setHeader("Content-Transfer-Encoding", "binary;");
		res.setHeader("Pragma", "no-cache;");
		res.setHeader("Expires", "-1;");
	}
	
	public static String getCurrentTimestamp(){
		return getCurrentTimestamp(System.currentTimeMillis());
	}
	
	public static String getCurrentTimestamp(long time){
		return new Timestamp(time).toString();
	}
}

class LowerCaseKeySerializer extends JsonSerializer {
	@Override
	public void serialize(Object value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeFieldName(value.toString().toLowerCase());
	}
}
