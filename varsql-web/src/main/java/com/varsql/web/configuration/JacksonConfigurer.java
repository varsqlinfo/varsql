package com.varsql.web.configuration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * -----------------------------------------------------------------------------
* @fileName		: JacksonConfigurer.java
* @desc		: jackson 설정. 
* @author	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
*2020. 4. 27. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
public class JacksonConfigurer implements Jackson2ObjectMapperBuilderCustomizer {
 
    private static final String dateFormat = "yyyy-MM-dd";
    private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
 
	@Override
	public void customize(Jackson2ObjectMapperBuilder builder) {
		builder.timeZone(TimeZone.getDefault()); // 올바른 타임존을 설정해야 offset/zoned datetime이 올바로 설정됨.
        builder.locale(Locale.getDefault());      
        builder.simpleDateFormat(dateTimeFormat);
        builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
        builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
		
	}
 
}