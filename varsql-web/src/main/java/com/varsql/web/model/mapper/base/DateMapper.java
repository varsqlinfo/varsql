package com.varsql.web.model.mapper.base;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.mapstruct.Mapper;

import com.varsql.core.common.constants.VarsqlConstants;
import com.vartech.common.utils.DateUtils;

@Mapper(componentModel = "spring")
public class DateMapper {

	public String asString(Date date) {
		return date != null ? DateUtils.dateformat(VarsqlConstants.DATE_FORMAT, date) : null;
	}

	public Date asDate(String date) {
		try {
			return date != null ? new SimpleDateFormat(VarsqlConstants.DATE_FORMAT).parse(date) : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public String asTimestampString(Timestamp date) {
		return date != null ? DateUtils.dateformat(VarsqlConstants.DATE_TIME_FORMAT, date) : null;
	}

	public Timestamp asTimestamp(String date) {
		try {
			return date != null ? new Timestamp(new SimpleDateFormat(VarsqlConstants.DATE_TIME_FORMAT).parse(date).getTime()) : null;
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}