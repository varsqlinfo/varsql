package com.varsql.web.model.mapper.base;

import java.sql.Timestamp;
import java.util.Date;

import org.mapstruct.Mapper;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.common.util.VarsqlDateUtils;
import com.vartech.common.utils.DateUtils;

@Mapper(componentModel = "spring")
public class DateMapper {

	public String asString(Date date) {
		return date != null ? DateUtils.dateformat(VarsqlConstants.DATE_FORMAT, date) : null;
	}

	public Date asDate(String date) {
		return date != null ? VarsqlDateUtils.stringToDate(date): null;
	}

	public String asTimestampString(Timestamp date) {
		return date != null ? DateUtils.dateformat(VarsqlConstants.TIMESTAMP_FORMAT, date) : null;
	}

	public Timestamp asTimestamp(String date) {
		return date != null ? VarsqlDateUtils.stringToTimestamp(date)  : null;
	}
}