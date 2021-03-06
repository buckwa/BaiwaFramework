package com.buckwa.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

public class BuckWaDateUtils {
	private static Logger logger = LoggerFactory.getLogger(BuckWaDateUtils.class);

	public static final Locale thaiLocale = new Locale("th", "TH");

	public static String convert_ddmmyyy_to_yyyymmdd(String ddmmyyStr) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/mm/yyyy",
				Locale.US);
		SimpleDateFormat yyyyddmm_sdf = new SimpleDateFormat("yyyy-mm-dd",
				Locale.US);
		try {
			Date tmpdate = null;
			if (ddmmyyStr != null) {
				tmpdate = ddmmyyy_sdf.parse(ddmmyyStr);
			}
			returnStr = yyyyddmm_sdf.format(tmpdate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("   " + ddmmyyStr
				+ " convert_ddmmyyy_to_yyyymmdd  returnStr:" + returnStr);
		return returnStr;

	}
	public static String getShortThaiMonthFromDate(Timestamp dateIn){
		String returnstr ="";
		try{
			  
			Date date = new Date(dateIn.getTime());  
			Calendar calendar = new GregorianCalendar(); 
			calendar.setTime(date); 
			int dateInt = calendar.get(Calendar.DATE);
			int monthIn =calendar.get(Calendar.MONTH);
			int yearInt = calendar.get(Calendar.YEAR)+543;
			
			
			String monthStr ="";
			if(monthIn==0){
				monthStr="ม.ค.";
			}
			if(monthIn==1){
				monthStr="ก.พ.";
			}
			if(monthIn==2){
				monthStr="มี.ค.";
			}
			if(monthIn==3){
				monthStr=" เม.ย.";
			}
			if(monthIn==4){
				monthStr=" พ.ค.";
			}
			if(monthIn==5){
				monthStr="มิ.ย.";
			}
			if(monthIn==6){
				monthStr="ก.ค.";
			}
			if(monthIn==7){
				monthStr="ส.ค.";
			}
			if(monthIn==8){
				monthStr="ก.ย.";
			}
			
			if(monthIn==9){
				monthStr="ต.ค.";
			}			
			if(monthIn==10){
				monthStr="พ.ย.";
			}			
			if(monthIn==11){
				monthStr="ธ.ค.";
			}
			returnstr = dateInt+" "+monthStr+ " "+yearInt;
	  
			//logger.info("Date = " + calendar.get(Calendar.DATE));

			//logger.info("Month = " + calendar.get(Calendar.MONTH));
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return returnstr;
	}
		
	
	public static Date convert_to_yyyy_MM_dd(String ddmmyyStr) {
		String returnStr = "";
		SimpleDateFormat yyyyddmm_sdf = new SimpleDateFormat("yyyy-MM-dd",
				thaiLocale);
		Date tmpdate = null;
		try {

			if (ddmmyyStr != null) {
				tmpdate = yyyyddmm_sdf.parse(ddmmyyStr);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("   " + ddmmyyStr
				+ " convert_ddmmyyy_to_yyyymmdd  returnStr:" + returnStr);
		return tmpdate;

	}

	public static String convert_ddmmyyy_to_yyyyddMM(String ddmmyyStr) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/mm/yyyy",
				Locale.US);
		SimpleDateFormat yyyyddmm_sdf = new SimpleDateFormat("yyyy-dd-MM",
				Locale.US);
		try {
			Date tmpdate = null;
			if (ddmmyyStr != null) {
				tmpdate = ddmmyyy_sdf.parse(ddmmyyStr);
			}
			returnStr = yyyyddmm_sdf.format(tmpdate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		logger.info("   " + ddmmyyStr
				+ " convert_ddmmyyy_to_yyyyddMM  returnStr:" + returnStr);
		return returnStr;

	}

	public static String getCurrentDateNoTime() {
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm:ss", Locale.US);
		// formatter = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
		Date returnDate = null;
		String dateStr = "";
		try {

			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());

			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			// Put it back in the Date object
			returnDate = cal.getTime();
			dateStr = ddmmyyy_sdf.format(returnDate);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return dateStr;
	}

	public static String get_ddmmyy_from_date(Date dateIn) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/mm/yy",
				Locale.US);

		try {

			returnStr = ddmmyyy_sdf.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static String get_yyddmm_from_date(Date dateIn) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("yymmdd", Locale.US);

		try {

			returnStr = ddmmyyy_sdf.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static Date getCurrentDateTime() {
		return new Date(System.currentTimeMillis());
	}

	public static String getddmmyy_from_yyddmm(String yyddmm) {
		String returnStr = "";
		SimpleDateFormat yyddmm_sdf = new SimpleDateFormat("yymmdd", Locale.US);
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/mm/yy",
				Locale.US);
		try {
			Date tmpDate = yyddmm_sdf.parse(yyddmm);
			returnStr = ddmmyyy_sdf.format(tmpDate);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static String get_ddmmyyyyhhmmss_from_date(Timestamp dateIn) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat(
				"dd/MM/yyyy HH:mm:ss", thaiLocale);

		try {

			returnStr = ddmmyyy_sdf.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static String get_ddmmyyyy_from_date(Date dateIn) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/mm/yyyy",
				Locale.US);

		try {

			returnStr = ddmmyyy_sdf.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static String get_ddMMyyyy_from_date(Date dateIn) {
		String returnStr = "";
		if (null != dateIn) {
			SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
			try {
				returnStr = ddmmyyy_sdf.format(dateIn);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return returnStr;
	}

	public static String get_hhmmss_from_date(Date dateIn) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("HH:mm:ss",
				Locale.US);

		try {

			returnStr = ddmmyyy_sdf.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static String get_ddMMyyyy_thai_from_date(Date dateIn) {
		String returnStr = "";
		if (null != dateIn) {
			SimpleDateFormat sdf_thai = new SimpleDateFormat("dd/MM/yyyy", thaiLocale);
			try {
				returnStr = sdf_thai.format(dateIn);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return returnStr;
	}
	
	
	public static String get_MMddyyyy_thai_from_date(Date dateIn) {
		String returnStr = "";
		if (null != dateIn) {
			SimpleDateFormat sdf_thai = new SimpleDateFormat("MM/dd/yyyy");
			try {
				returnStr = sdf_thai.format(dateIn);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return returnStr;
	}

	public static String get_ddmmyyyy_time_thai_from_date(Date dateIn) {
		String returnStr = "";
		SimpleDateFormat ddmmyyy_time_thai = new SimpleDateFormat(
				"MM/dd/yyyy HH:mm:ss", thaiLocale);

		try {

			returnStr = ddmmyyy_time_thai.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;
	}

	public static String get_current_ddmmyyyy_thai_from_date() {
		Date dateIn = new Date();
		String returnStr = "";
		SimpleDateFormat sdf_thai = new SimpleDateFormat("dd/MM/yyyy",
				thaiLocale);

		try {
			returnStr = sdf_thai.format(dateIn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static String get_current_yyyy_MM_dd_thai_from_date() {
		Date dateIn = new Date();
		String returnStr = "";
		SimpleDateFormat sdf_thai = new SimpleDateFormat("yyyy-MM-dd",
				thaiLocale);

		try {
			returnStr = sdf_thai.format(dateIn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}

	public static int getYearCurrent() {
		Date dateNow = new Date(System.currentTimeMillis());
		String returnStr = "";
		SimpleDateFormat sdf_thai = new SimpleDateFormat("yyyy", Locale.US);

		try {
			returnStr = sdf_thai.format(dateNow);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Integer.parseInt(returnStr);

	}

	public static String getMonthTH(Date dateIn) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM", new Locale(
				"th", "th"));
		String returnStr = "";
		try {
			returnStr = dateFormat.format(dateIn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;
	}

	public static String getDayTH(Date dateIn) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd", new Locale(
				"th", "th"));
		String returnStr = "";
		try {
			returnStr = dateFormat.format(dateIn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;
	}

	public static String getYearTH(Date dateIn) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", new Locale(
				"th", "th"));
		String returnStr = "";
		try {
			returnStr = dateFormat.format(dateIn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;
	}

	public static int getYear(Date dateIn) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);
		String returnStr = "";
		try {
			returnStr = dateFormat.format(dateIn);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return Integer.parseInt(returnStr);
	}

	public static java.sql.Date utilDateToSqlDate(java.util.Date uDate) {
		if (null != uDate) {
			try {
				DateFormat sqlDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
				return java.sql.Date.valueOf(sqlDateFormatter.format(uDate));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;

	}

	public static java.util.Date sqlDateToutilDate(java.sql.Date sDate) {
		try {
			if (sDate != null) {
				DateFormat utilDateFormatter = new SimpleDateFormat(
						"dd-MM-yyyy");
				return (java.util.Date) utilDateFormatter
						.parse(utilDateFormatter.format(sDate));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Date parseDate(String dateStr) {
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		Date tmpDate = null;
		if (dateStr != null && !"".equals(dateStr)) {
			try {
				tmpDate = ddmmyyy_sdf.parse(dateStr);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return tmpDate;

	}
	
	public static Timestamp parseTimeStamp(String dateStr) {
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		Timestamp tmpDate = null;
		if (dateStr != null && !"".equals(dateStr)) {
			try {
				Date tmpDatex = ddmmyyy_sdf.parse(dateStr);
				tmpDate = new Timestamp(tmpDatex.getTime());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return tmpDate;

	}

	public static Date parseDateTH(String dateStr) {
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("yyyy-MM-dd",
				thaiLocale);
		Date tmpDate = null;
		try {
			tmpDate = ddmmyyy_sdf.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return tmpDate;

	}
	
	public static Date convertStr_MMddyyy_TH_to_Date(String dateStr) {
		SimpleDateFormat ddmmyyy_sdf = new SimpleDateFormat("MM/dd/yyy",
				thaiLocale);
		Date tmpDate = null;
		try {
			tmpDate = ddmmyyy_sdf.parse(dateStr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return tmpDate;

	}

	public static Date convertDateThToDateEn(Date dateTh) {
		try {
			String year = DateFormatUtils.format(dateTh, "yyyy", new Locale(
					"en", "US"));
			String day = DateFormatUtils.format(dateTh, "dd", new Locale("en",
					"US"));
			String month = DateFormatUtils.format(dateTh, "MM", new Locale(
					"en", "US"));
			String dateStr = day + "/" + month + "/"
					+ (Integer.parseInt(year) - 543);

			return parseDate(dateStr);
		} catch (Exception ex) {

		}
		return null;
	}
	
	public static Date convertDateToDateEn(Date dateTh) {
		try {
			String year = DateFormatUtils.format(dateTh, "yyyy", new Locale(
					"en", "US"));
			String day = DateFormatUtils.format(dateTh, "dd", new Locale("en",
					"US"));
			String month = DateFormatUtils.format(dateTh, "MM", new Locale(
					"en", "US"));
			String dateStr = day + "/" + month + "/"
					+ (Integer.parseInt(year));

			return parseDate(dateStr);
		} catch (Exception ex) {

		}
		return null;
	}
	
	public static String getCustomFormat_thai_from_date(Date dateIn, String format) {
		String returnStr = "";
		SimpleDateFormat sdf_thai = new SimpleDateFormat(format, thaiLocale);

		try {

			returnStr = sdf_thai.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}
	
	public static String getCustomFormat_en_from_date(Date dateIn, String format) {
		String returnStr = "";
		SimpleDateFormat sdf_thai = new SimpleDateFormat(format);

		try {

			returnStr = sdf_thai.format(dateIn);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return returnStr;

	}
	
	public static String get_current_ddMMMMyyyy_thai_from_date(Date dateIn){
		if(dateIn!=null)
			return getCustomFormat_thai_from_date(dateIn,"dd MMMM yyyy");
		else
			return "";
	}
	
	public static String get_current_ddMMMyyyy_thai_from_date(Date dateIn){
		if(dateIn!=null)
			return getCustomFormat_thai_from_date(dateIn,"dd MMM yyyy");
		else
			return "";
	}
	
	public static String get_current_ddMMMyyyyhhmmss_thai_from_date(Timestamp dateIn) {
		if(dateIn!=null)
			return getCustomFormat_thai_from_date(dateIn,"dd MMM yyyy HH:mm:ss");
		else
			return "";

	}
	
	public static String convertENYearToTHYear(String year) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		return getCustomFormat_thai_from_date(calendar.getTime(), "yyyy");
	}
	
	public static String convertTHYearToENYear(String year) {
		Calendar calendar = Calendar.getInstance(thaiLocale);
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		return getCustomFormat_en_from_date(calendar.getTime(), "yyyy");
	}
	
	public static String convertTime(Time time) {
		SimpleDateFormat form = new SimpleDateFormat("HH:mm");  
		return form.format(time);
	}
	
		
	
	public static void main(String arg[]) {

		logger.info(" xx:" + getCurrentDateNoTime());
	}

}
