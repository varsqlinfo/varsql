package com.varsql.web.util;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;

import com.varsql.common.util.SecurityUtil;
import com.varsql.db.vo.DatabaseInfo;
import com.varsql.web.app.database.DbTypeEnum;
import com.varsql.web.common.constants.VarsqlParamConstants;
import com.varsql.web.common.vo.DataCommonVO;

public class VarsqlUtil {
	
	public static String generateUUID (){
		return UUID.randomUUID().toString().replaceAll("-", ""); 
	}
	public static String objectToString(Object json) {
		try {
			return new ObjectMapper().writeValueAsString(json);
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
	
	public static DbTypeEnum getDBMetaImpl(String connid){
		try{
			DatabaseInfo  dbinfo= SecurityUtil.loginInfo().getDatabaseInfo().get(connid);
			return DbTypeEnum.valueOf(dbinfo.getType());
		}catch(Exception e){
			return DbTypeEnum.OTHER;
		}
		 
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
