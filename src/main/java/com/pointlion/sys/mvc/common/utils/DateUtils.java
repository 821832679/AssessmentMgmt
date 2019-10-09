package com.pointlion.sys.mvc.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author John.liu
 * @date 2015-5-26
 * @description 时间工具类
 */
public class DateUtils {
	
	static SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
	static SimpleDateFormat df4 = new SimpleDateFormat("yyyyMMddHHmmss");
	static SimpleDateFormat df5 = new SimpleDateFormat("yyyyMMdd");
	static SimpleDateFormat df6 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	static SimpleDateFormat df7 = new SimpleDateFormat("M月d日");
	static SimpleDateFormat df8 = new SimpleDateFormat("yyyy年M月d日");
	static SimpleDateFormat df10 = new SimpleDateFormat("HH");
	static SimpleDateFormat df9 = new SimpleDateFormat("mm");
	static SimpleDateFormat df11 = new SimpleDateFormat("yyyy年M月");
	static SimpleDateFormat df12 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ssss");
	static SimpleDateFormat df13 = new SimpleDateFormat("HH:mm");
	static SimpleDateFormat df14 = new SimpleDateFormat("yyyy.MM");
	static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");  
	static TimeZone zone = TimeZone.getTimeZone("GMT+8:00");
	//static Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"), Locale.CHINESE);
	
	static final int secondsDayMod = 24*60*60;
	static final int secondsHourMod = 60*60;
	
	static{
		TimeZone.setDefault(zone);
		df1.setTimeZone(zone);
		df2.setTimeZone(zone);
		df3.setTimeZone(zone);
		df5.setTimeZone(zone);
	}
	
	public static String getHmString(Date date){
		return df13.format(date);
	}
	
	public static String getYMString(Date date){
		return df11.format(date);
	}
	
	public static String get2YMDString(Date date){
		if(date == null) return null;
		return df8.format(date);	
	}
	public static boolean sameDay(Date date1, Date date2){
		if(date1 == null && date2 == null){
			return true;
		}else if(date1 != null && date2 != null){
			return date1.getYear()==date2.getYear() && date1.getMonth()==date2.getMonth() && date1.getDate() == date2.getDate();
		}
		return false;
	}
	
	public static Date convert2Date(long millseconds){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.setTimeInMillis(millseconds);
		return c.getTime();
	}
	
	public static Date getFutureDate(int nDay){
		Calendar c = Calendar.getInstance(zone);
		c.add(Calendar.DATE, nDay);
		return c.getTime();
	}
	/**
	 * yyyy-MM-dd HH:mm string 
	 * @param millseconds
	 * @return
	 */
	public static String convert2DateYmdHs(long millseconds){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.setTimeInMillis(millseconds);
		return df3.format(c.getTime());
	}
	
	public static String get2MDString(Date date){
		if(date == null) return null;
		return df7.format(date);	
	}
	
	public static Date convert2YMdhmsTime(String timeString){
		if(timeString == null || "".equals(timeString)){
			return null;
		}
		try {
			return df1.parse(timeString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * yyyy-MM-dd
	 * @param dateString
	 * @return
	 */
	public static Date convert2YMdTime(String dateString){
		if(dateString == null || "".equals(dateString)){
			return null;
		}
		try {
			return df2.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static int getDateWeek(Date date){
		Calendar c = Calendar.getInstance(zone);
		if(date == null) return c.get(Calendar.DAY_OF_WEEK);
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK);
	}
	
	public static String getDateWeekDesc(Date date){
		int dayOfWeek = -1;
		String weekDesc = "";
		Calendar c = Calendar.getInstance(zone);
		if(date != null) {
			c.setTime(date);
			dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
		}
		switch(dayOfWeek){
		case 1:
			weekDesc = "星期日";
			break;
		case 2:
			weekDesc = "星期一";
			break;
		case 3:
			weekDesc = "星期二";
			break;
		case 4:
			weekDesc = "星期三";
			break;
		case 5:
			weekDesc = "星期四";
			break;
		case 6:
			weekDesc = "星期五";
			break;
		case 7:
			weekDesc = "星期六";
			break;
		default:
			break;
		}
		return weekDesc;
	}
	
	/**
	 * 得到开始到结束日期之间的日期数组
	 * @param begin  yyyy-MM-dd
	 * @param end    yyyy-MM-dd
	 * @return
	 * @throws Exception 
	 */
	public static Object [] getBegin2EndTimeArray(String begin,String end)
	{
		List<String > dates=new ArrayList<String>();
		Calendar startDay = Calendar.getInstance();  
	    Calendar endDay = Calendar.getInstance();  
	  
	    try 
	    {
			startDay.setTime(FORMATTER.parse(begin));
			endDay.setTime(FORMATTER.parse(end));  
		} catch (ParseException e) 
	    {
			return null;
		}  
	   
	    
	    // 给出的日期开始日比终了日大则 return null
	    if (startDay.compareTo(endDay) > 0) 
	    {  
	    	return null;  
	    }  
	    
	    dates.add(begin);
	    
	    if (startDay.compareTo(endDay) == 0) 
	    {  
	    	return dates.toArray();  
	    } 
	    // 现在输出中的日期  
	    Calendar currentPrintDay = startDay;  
	    while (true) 
	    {  
		     // 日期加一  
		     currentPrintDay.add(Calendar.DATE, 1);  
		     // 日期加一后判断是否达到终了日，达到则终止打印  
		     if (currentPrintDay.compareTo(endDay) == 0) 
		     {  
		    	 break;  
		     }  
		     // 输出日期  
		    // System.out.println(FORMATTER.format(currentPrintDay.getTime()));  
		     dates.add(FORMATTER.format(currentPrintDay.getTime()));
	    }  
	    dates.add(end);
				
		return dates.toArray();
	}
	
	public static List<Date> getDateArray(Date start, Date end){
		List<Date> data = new ArrayList<Date>();
		if(start == null || end == null || start.after(end)){
			return data;
		}
		TimeZone.setDefault(zone);
		Calendar c = Calendar.getInstance(zone);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		data.add(start);
		while(true){
			c.add(Calendar.DATE, 1);
			if(c.getTime().after(end)){
				break;
			}
			//System.out.println(convert2YMdhm(c.getTime()));
			data.add(c.getTime());
		}
		return data;
	}
	
	public static Date getTodayBeginDate(){
		TimeZone.setDefault(zone);
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}
	
	public static Date getTodayEndDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.add(Calendar.HOUR_OF_DAY, 23-c.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	
	public static Date getWeekBeginDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.add(Calendar.DAY_OF_WEEK, Calendar.SUNDAY - c.get(Calendar.DAY_OF_WEEK));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}
	
	public static Date getWeekEndDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - c.get(Calendar.DAY_OF_WEEK));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	
	public static Date getWeekendBeginDate(){
		Calendar c = Calendar.getInstance(zone);
		c.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - c.get(Calendar.DAY_OF_WEEK));
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 1);
		return c.getTime();
	}
	
	public static Date getWeekendEndDate(){
		Calendar c = Calendar.getInstance(zone);
		c.add(Calendar.DAY_OF_WEEK, Calendar.SATURDAY - c.get(Calendar.DAY_OF_WEEK) + 1);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	
	public static String getYMdhmsTime(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		return df1.format(c.getTime());
	}
	
	public static Date getMonthBeginDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.add(Calendar.DAY_OF_MONTH, -c.get(Calendar.DAY_OF_MONTH)+1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	public static Date getMonthEndDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		int maxDays = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		c.add(Calendar.DAY_OF_MONTH, maxDays - c.get(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	/**
	 * yyyy-MM-dd  current
	 * @return
	 */
	public static String getYMd(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		return df2.format(c.getTime());
	}
	/**得到今天 date
	 * 
	 * @return
	 */
	public static Date getDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		return c.getTime();
	}
	/**
	 * 获取今天的日期（没有小时、分、秒、微秒） date
	 * @return
	 */
	public static Date getTodayDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	/**
	 * 获取明天的日期（没有小时、分、秒、微秒） date
	 * @return
	 */
	public static Date getTomorrowDate(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.add(Calendar.DATE, 1);//加1天
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static String trimTime2Date(String timeString){
		return timeString.split("\\s+")[0];
	}
	
	public static String trimTime2Time(String timeString){
		return timeString.split("\\s+")[1];
	}
	
	/**
	 * yyyy-MM-dd  string
	 * @param dateTime
	 * @return
	 */
	public static String covert2YMd(Date dateTime){
		if(dateTime == null) return "";
		return df2.format(dateTime);
	}
	/**
	 * yyyy-MM-dd HH:mm string
	 * @param dateTime
	 * @return
	 */
	public static String convert2YMdhm(Date dateTime){
		if(dateTime == null) return "";
		return df3.format(dateTime);
	}
	/**
	 * 转换成时间字符串 YYYY-MM-DD HH:MM:SS   string
	 * @param dateTime
	 * @return
	 */
	public static String convert2YMdhms(Date dateTime){
		if(dateTime == null) return "";
		return df1.format(dateTime);
	}
	/**
	 * yyyyMMddHHmmss  string
	 * @param dateTime
	 * @return
	 */
	public static String get2YMdHmsString(Date dateTime){
		if(dateTime == null) return "";
		return df4.format(dateTime);
	}
	/**
	 * dateTime和当前时间的差距的中文表示, for: [8天][8小时][8分钟]前
	 * @param dateTime  小于当前
	 * @return [8天][8小时][8分钟]前
	 */
	public static String diff2Cn(Date dateTime) {
		if (dateTime == null)
			return "";
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		Calendar c = Calendar.getInstance(zone, Locale.CHINESE);
		long diff = dateTime.getTime() - c.getTimeInMillis();
		String tag = "";
		if(diff > 0){
			tag = "后";
		}else{
			tag = "前";
		}
		
		diff = Math.abs(diff);
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		if(day > 0){
			if(day > 365)
				return (day/365) + "年" + tag;
			else if(day > 30)
				return (day/30) + "月" + tag;
			else
				return day + "天" + tag;
			
		}else if(hour > 0){
			return hour + "小时" + tag;
			//return (hour > 0 ? hour + "小时" : "") + (min > 0 ? min + "分钟" : "0分钟") + tag;
		}else{
			
			return (min==0 ? 1 :min) + "分钟" + tag;
		}
		//return (day > 0 ? day + "天" : "")+(hour > 0 ? hour + "小时" : "")+(min > 0 ? min + "分钟" : "") + tag;
	}
	
	public static String diff2Cn(Date dateTime, String suffix) {
		if (dateTime == null)
			return "";
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		Calendar c = Calendar.getInstance(zone, Locale.CHINESE);
		
		long diff = dateTime.getTime() - c.getTime().getTime();
		diff = Math.abs(diff);
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		if(day > 0)
		{
			if(day > 365)
				return (day/365) + "年" + suffix;
			else if(day > 30)
				return (day/30) + "月" + suffix;
			else
				return day + "天" + suffix;
		}else if(hour > 0){
			return hour + "小时" + suffix;
			//return (hour > 0 ? hour + "小时" : "") + (min > 0 ? min + "分钟" : "0分钟") + tag;
		}else{
			return min + "分钟" + suffix;
		}
		//return (day > 0 ? day + "天" : "")+(hour > 0 ? hour + "小时" : "")+(min > 0 ? min + "分钟" : "") + tag;
	}
	
	/**
	 * 判断当前时间是否早于dateTime
	 * @param dateTime
	 * @return
	 */
	public static boolean before(Date dateTime){
		if(dateTime == null)  return false;
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		
		return dateTime.getTime() - c.getTimeInMillis() > 0;
	}
	/**
	 * 判断当前时间是否晚于dateTime
	 * @param dateTime
	 * @return
	 */
	public static boolean after(Date dateTime){
		if(dateTime == null)  return false;
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		
		return c.getTimeInMillis() - dateTime.getTime() > 0;
	}
	
	public static String getNDaysWeekday(int n){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		c.add(Calendar.DAY_OF_MONTH, n);
		switch(c.get(Calendar.DAY_OF_WEEK)){
		case 7:
			return "周六";
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		}
		return "";
	}
	
	/**
	 * 获取YYYYMMDD格式化时间 string
	 */
	public static String formatSth(Date date)
	{
		String strDate = df5.format(date);
		return strDate;
	}
	/**
	 * 
	 * 得到日期的特殊格式（用于订单编号）
	 * @param date
	 * @return
	 */
	public static String getString_yyMMddHHmmssSSS(Date date)
	{
		String strDate="";
		if(null!=date)
			strDate = df6.format(date);
		
		return strDate;
	}
	/**
	 * 获取当前几时（24小时制）
	 */
	public static Integer getHour()
	{
		String hour = df10.format(new Date());
		
		return Integer.valueOf(hour);
	}
	/**
	 * 获取当前几分
	 */
	public static Integer getMinute()
	{
		String hour = df9.format(new Date());
		
		return Integer.valueOf(hour);
	}
	/**
	 * 获取当前时间 （yyyy-MM-dd）
	 */
	public static String getDateStr()
	{
		String date = df2.format(new Date());
		
		return date;
	}
	/**
	 * 增加几天
	 * @param date yyyy-MM-dd
	 * @param d  天数
	 * @return	yyyy-MM-dd
	 */
	public static String add(String date,int d)
	{
		String rs="";
		Date _date=convert2YMdTime(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(_date);
		cal.add(Calendar.DATE, d);
		
		rs=covert2YMd(cal.getTime());
		
		//System.out.println((new SimpleDateFormat("yyyy-MM-dd")).format(cal.getTime()));
		return rs;
	}
	/**
	 * 判断时间过期
	 * @param millseconds
	 * @param in_expires_seconds
	 * @return true 过期  false不过期
	 */
	public static boolean judgeExpired(long millseconds, long in_expires_seconds){
		Calendar cal = Calendar.getInstance();
		long curr = cal.getTimeInMillis();
		cal.setTimeInMillis(millseconds);
		int days = (int)(in_expires_seconds / secondsDayMod);
		int hours = (int)((in_expires_seconds % secondsDayMod) / secondsHourMod);
		int minutes = (int)((in_expires_seconds % secondsHourMod) / 60);
		int seconds = (int)(in_expires_seconds % 60);
		
		cal.add(Calendar.DATE, days);
		cal.add(Calendar.HOUR, hours);
		cal.add(Calendar.MINUTE, minutes);
		cal.add(Calendar.SECOND, seconds);
//		System.out.println(convert2YMdhms(cal.getTime()));
		return cal.getTimeInMillis() < curr;
	}
	
	public static boolean isInDate(Date date, Date strDateBegin,  
	        Date strDateEnd) {  
		if(strDateBegin.getTime()<=date.getTime()&& date.getTime()<=strDateEnd.getTime()){
			return true;
		}
		return false;
	}
	
	public static String getDiffDateString(int dayDiff){
		return covert2YMd(getDiffDate(dayDiff));
	}
	
	public static Date getDiffDate(int dayDiff){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, dayDiff);
		return cal.getTime();
	}
	
	public static Date getDiffTime(int seconds){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	public static int getMonthDiff(String startDate, String endDate){
		Date stDate = convert2YMdTime(startDate);
		Date edDate = convert2YMdTime(endDate);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(stDate);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(edDate);
		int yearDiff = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
		return yearDiff * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss:ssss
	 * @return
	 */
	public static String getYMdhmsTime2(){
		Calendar c = Calendar.getInstance(zone,Locale.CHINESE);
		return df12.format(c.getTime());
	}
	
	/**
	 * 与当前时间比较，在当前小时之前hour个小时
	 * @param date 时间
	 * @param hour
	 * @return
	 */
	public static boolean isAllowRefund(Date date,Integer hour)
	{
		Date currDate = DateUtils.getDate();
		if(date != null)
		{
			if(date.getTime()-currDate.getTime()>hour*60*60*1000)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 得到以当前日期为准的几天范围的日期数组  yyyy-MM-dd
	 * <br/>
	 * 用于场馆预订 判断sku中的开放时间
	 * @param range
	 * @return
	 */
	public static String [] getDateRange(Integer range)
	{
		String rs[]=null;
		try
		{
			rs=new String[range];
			String dateStr=getDateStr();
			for(int i=0;i<range;i++)
				rs[i]=add(dateStr, i);
			
		}catch(Exception e)
		{
			
		}
		return rs;
	}
	
	/**
	 * yyyy-MM-dd  string
	 * @param dateTime
	 * @return
	 */
	public static String covert2PointYMd(Date dateTime){
		if(dateTime == null) return "";
		return df14.format(dateTime);
	}
	
	/**
	 * 判断某个日期是否在数组中
	 * @param dateArray
	 * @param date   yyyy-MM-dd
	 * @return
	 */
    public static Boolean isContains(String [] dateArray,String date)
    {
    	Collection temp_c = new ArrayList();  
    	temp_c.addAll(Arrays.asList(dateArray));
    	return temp_c.contains(date);
    }
    
    /**
	* 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
	* 
	* @param nowTime 当前时间
	* @param startTime 开始时间
	* @param endTime 结束时间
	* @return
	*/
	public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
		if (nowTime.getTime() == startTime.getTime() || nowTime.getTime() == endTime.getTime()) {
			return true;
		}

		Calendar date = Calendar.getInstance();
		date.setTime(nowTime);

		Calendar begin = Calendar.getInstance();
		begin.setTime(startTime);

		Calendar end = Calendar.getInstance();
		end.setTime(endTime);

		if (date.after(begin) && date.before(end)) {
			return true;
		} else {
			return false;
		}
	}
    
	public static void main(String[] args) {
		//Object [] ds=getBegin2EndTimeArray("2015-10-02","2015-10-02");
		//for(Object o:ds)
		//Object a=null;
		//String sss=diff2Cn(getWeekendBeginDate(),"前");
		
		/*List<Date> dateArray = getDateArray(getDate(), getFutureDate(6));
		for(Date d : dateArray){
			System.out.println(getDateWeekDesc(d));
		}*/
		
//		System.out.println(getNDaysWeekday(0));
//		Calendar c = Calendar.getInstance();
//		System.out.println(c.getTime());
//		c.set(Calendar.DATE, 5);
//		
//		System.out.println(c.getTime());
//		System.out.println(after(c.getTime()));
		
//		System.out.println(getDate());
//		System.out.println(DateUtils.convert2YMdhm(getDate())+", "+DateUtils.convert2YMdhm(getWeekendBeginDate())+", "+DateUtils.convert2YMdhm(getWeekendEndDate()));
//		System.out.println(DateUtils.convert2YMdhm(getWeekBeginDate()));
		
//		System.out.println(getMonthBeginDate()+"/"+getMonthEndDate());
/*		
		Calendar c = Calendar.getInstance();
		c.set(2016, 1, 21);
		System.out.println(c.getTime());
		c.add(Calendar.DATE, 100);
		System.out.println(c.getTime());*/
		
		System.out.println(isContains(getDateRange(2), "2016-07-22"));
	}
}
