package com.varsql.core.common.util;

import java.time.ZoneId;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.varsql.core.configuration.Configuration;
import com.vartech.common.utils.StringUtils;



public final class TimeZoneUtils {

	private static TimeZone timeZone;
	private static ZoneId zoneId;
	private static final Map<String, ZoneId> zoneIdMap = new HashMap<String, ZoneId>();

	static {
		if (StringUtils.isBlank(Configuration.getInstance().getTimeZoneId())) {
			timeZone = TimeZone.getDefault();
		} else {
			System.out.println("varsql time zone");
			timeZone = TimeZone.getTimeZone(Configuration.getInstance().getTimeZoneId());
		}
		
		zoneId = ZoneId.of(timeZone.getID());
	}

	private TimeZoneUtils() {
	}

	public static Calendar getTimeZoneCalendar() {
		return Calendar.getInstance(timeZone);
	}

	public static Calendar getTimeZoneCalendar(String zoneID) {
		return Calendar.getInstance(TimeZone.getTimeZone(zoneID));
	}
	
	public static ZoneId getZoneId() {
		return zoneId;
	}
	
	public static ZoneId getZoneId(String zoneId) {
		if(zoneIdMap.containsKey(zoneId)) {
			return zoneIdMap.get(zoneId);
		}
		
		ZoneId reval;
		try {
			reval= ZoneId.of(zoneId);
		}catch(Exception e) {
			reval= getZoneId();
		}
		
		zoneIdMap.put(zoneId, reval);
		
		return reval; 
	}
}