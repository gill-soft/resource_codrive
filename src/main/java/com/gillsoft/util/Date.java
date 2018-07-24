package com.gillsoft.util;

import org.apache.logging.log4j.core.util.datetime.FastDateFormat;

public class Date {
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String TIME_FORMAT = "HH:mm";
	public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm";
	public final static FastDateFormat dateFormat =  FastDateFormat.getInstance(DATE_FORMAT);
	public final static FastDateFormat dateFormatFull = FastDateFormat.getInstance(FULL_DATE_FORMAT);
	
	public static java.util.Date getFullDateString(String date, String time) {
		try {
			return dateFormatFull.parse(String.join(" ", date, time));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
