package com.varsql.web.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.TimeZone;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer;
import com.varsql.core.common.constants.VarsqlConstants;

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

	@Override
	public void customize(Jackson2ObjectMapperBuilder builder) {



		builder.timeZone(TimeZone.getDefault()); // 올바른 타임존을 설정해야 offset/zoned datetime이 올바로 설정됨.
        builder.locale(Locale.getDefault());
        builder.simpleDateFormat(VarsqlConstants.DATE_TIME_FORMAT);

        builder.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(VarsqlConstants.DATE_FORMAT)));
        builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(VarsqlConstants.DATE_TIME_FORMAT)));
        builder.serializerByType(OffsetDateTime.class, new CustomOffsetDateTimeSerializer(DateTimeFormatter.ofPattern(VarsqlConstants.DATE_TIME_FORMAT)));

	}

	public class CustomOffsetDateTimeSerializer extends OffsetDateTimeSerializer {
		public CustomOffsetDateTimeSerializer(DateTimeFormatter formatter) {
			super(OffsetDateTimeSerializer.INSTANCE, false, formatter);
		}
	}

}