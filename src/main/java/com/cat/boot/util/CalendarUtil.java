package com.cat.boot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cat.boot.exception.CatException;

public class CalendarUtil {
	
	private HashMap makePlanMap(ArrayList planList){
		  HashMap<Integer,ArrayList> planMap = new HashMap<Integer,ArrayList>();

		  Iterator planIterator = planList.iterator();
		  
		  while (planIterator.hasNext()) {
		  Object aa =  planIterator.next();
		  List qb = (List) aa;
		  int j = Integer.parseInt( qb.get(1).toString());
		  if(!planMap.containsKey(j)) {
			  ArrayList a = new ArrayList();
			  a.add(aa);
			  planMap.put(j, a);
		  } else {
			  ArrayList a = planMap.get(j);
			  a.add(aa);
			  planMap.replace(j, a);
		  }
		  } 
		   
		  return planMap;
	}

	public static String getYyyyMmDd(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(c.getTime());
	}
	
	public static String getYyMm(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");
		return df.format(c.getTime());
	}
	
	public static String getMmDd(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("MM-dd");
		return df.format(c.getTime());
	}
	
	public static String getHHmm(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		return df.format(c.getTime());
	}

	public static String getYyyyMmDdHHmmss(Calendar c) {

		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(c.getTime());
	}

	public static String getYyyyMmDdZh(Calendar c) {
		if (c == null)
			return "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		return df.format(c.getTime());
	}

	public static String getYyyyMmDdHHmmss(Calendar c, int i) {
		if (c == null)
			return "";
		c.add(Calendar.DAY_OF_MONTH, i);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(c.getTime());
	}
	
	public static String getYyyyMmDdHHmmss(Calendar c,int t, int i) {
		if (c == null)
			return "";
		c.add(t, i);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return df.format(c.getTime());
	}
	
	public static String getYesterday() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		return getYyyyMmDd(c);
	}
	
	public static boolean isToday(String date) {
		String today = getYyyyMmDd(Calendar.getInstance());
		try {
			String s = getYyyyMmDd(CalendarUtil.StringToCalendar(date));
			if(today.equals(s)) {
				return true;
			} else {
				return false;
			}
		} catch (CatException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isYesterday(String date) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -1);
		String today = getYyyyMmDd(c);
		try {
			String s = getYyyyMmDd(CalendarUtil.StringToCalendar(date));
			if(today.equals(s)) {
				return true;
			} else {
				return false;
			}
		} catch (CatException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static Calendar now() {
		return Calendar.getInstance();
	}

	public static Calendar clearTime(Calendar c, boolean returnClone) {

		Calendar result = null;
		if (c == null)
			return result;
		if (returnClone) {
			result = (Calendar) c.clone();
		} else {
			result = c;
		}
		result.set(Calendar.HOUR_OF_DAY, 0);
		result.set(Calendar.MINUTE, 0);
		result.set(Calendar.SECOND, 0);
		result.set(Calendar.MILLISECOND, 0);
		return result;
	}

	public static Calendar StringToCalendar(String value) throws CatException {

		if (value == null) {
			return null;
		}
		Calendar result = null;
		if (value instanceof String && !value.equals("")) {
			try {
				Date date = CalendarUtil.strToDateTime(value);
				result = Calendar.getInstance();
				result.setTime(date);
			} catch (CatException e) {
				throw new CatException("时间转换出现了错误");
			}
		}
		return result;
	}

	public static final Date strToDateTime(String dateTimeString) throws CatException {
		try {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date result = df.parse((String) dateTimeString);
			return result;

		} catch (ParseException e) {
			try {
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Date result = df.parse((String) dateTimeString);
				return result;
			} catch (ParseException e1) {

				try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					Date result = df.parse((String) dateTimeString);
					return result;
				} catch (ParseException e2) {
					throw new CatException("时间转换出现了错误");
				}

			}
		}
	}

	public static String dateToWeek(String datetime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}
	
	public static String dateToWeek(Calendar cal) {
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	public static long compareTo(String t1, String t2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date d1;
		try {
			d1 = sdf.parse("2000-01-01 " + t1);
			Date d2 = sdf.parse("2000-01-01 " + t2);
			return d1.getTime() - d2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long compareNow(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = sdf.parse(data + " 23:59:59");
			Date d2 = Calendar.getInstance().getTime();
			return d1.getTime() - d2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long compareToDate(String t1, String t2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d1;
		try {
			d1 = sdf.parse(t1);
			Date d2 = sdf.parse(t2);
			
			return d1.getTime() - d2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static long compareNow2(String data) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = sdf.parse(data + " 00:00:00");
			Date d2 = Calendar.getInstance().getTime();
			return d1.getTime() - d2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static long compareToTime(String t1, String t2) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1;
		try {
			d1 = sdf.parse(t1);
			Date d2 = sdf.parse(t2);

			return d1.getTime() - d2.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static boolean compareTo(String t1, String t2, String t3, String t4) {
		// 当开始时间 或者结束时间在已预约之间
		if (compareTo(t1, t3) >= 0 && CalendarUtil.compareTo(t1, t4) < 0) {
			return true;
		}
		if (compareTo(t2, t3) > 0 && CalendarUtil.compareTo(t2, t4) <= 0) {
			return true;
		}
		// 当开始时间在已预约之前 结束时间在已预约之后
		if (compareTo(t1, t3) < 0 && CalendarUtil.compareTo(t2, t4) > 0) {
			return true;
		}
		// 当开始时间和结束时间等于已预约
		if (compareTo(t1, t3) == 0 && CalendarUtil.compareTo(t2, t4) == 0) {
			return true;
		}
		return false;
	}
	
	public static boolean isLastDay(int i) {
		Calendar calendar = Calendar.getInstance();
		if(calendar.get(Calendar.DAY_OF_MONTH) == calendar
				.getActualMaximum(Calendar.DAY_OF_MONTH)-i) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String iniMonth(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, i);
		return getYyMm(calendar);
	}
	
	public static String iniMonthDay(int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, i);
		return getYyyyMmDd(calendar);
	}
	
	public static long compare(String time,int hour) {
		try {
			Calendar c = CalendarUtil.StringToCalendar(time);
			c.add(Calendar.HOUR_OF_DAY, hour);
			return Calendar.getInstance().getTime().getTime() - c.getTime().getTime();
		} catch (CatException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return 0;
		}
	}
	
	//这个就是方法
    public static List<String> getTimeList(String startDate, String endxDate){
        SimpleDateFormat sdf ;
        int calendarType;

        switch (startDate.length()){
            case 10:
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                calendarType = Calendar.DATE;
                break;
            case 7:
                sdf = new SimpleDateFormat("yyyy-MM");
                calendarType = Calendar.MONTH;
                break;
            case 4:
                sdf = new SimpleDateFormat("yyyy");
                calendarType = Calendar.YEAR;
                break;
            default:
                return null;
        }

        List<String> result = new ArrayList<>();
        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        try {
            min.setTime(sdf.parse(startDate));
            min.add(calendarType, 0);
            max.setTime(sdf.parse(endxDate));
            max.add(calendarType, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(min.getTime()));
            curr.add(calendarType, 1);
        }
        return result;
    }

	public static void main(String[] args) {
		/*try {
			org.apache.commons.json.JSONObject j =  new org.apache.commons.json.JSONObject();
			j.put("key", "111");
			j.put("value", "222");
			
			
			org.apache.commons.json.JSONArray xx = new org.apache.commons.json.JSONArray();
			org.apache.commons.json.JSONObject j2 =  new org.apache.commons.json.JSONObject();
			j2.put("key", "111");
			j2.put("value", "222");
			xx.add(j2);
			j.put("array", xx);
			
			String xxx = j.toString();
			System.out.println(xxx);
			org.apache.commons.json.JSONObject h = (org.apache.commons.json.JSONObject)
					org.apache.commons.json.JSON.parse(xxx);
			
			System.out.println(h);
		} catch (org.apache.commons.json.JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		
		List<List<String>> l1 = new ArrayList<List<String>>();
		List<List<String>> l2 = new ArrayList<List<String>>();
		Map<String,List<String>> map_l1 = new HashMap<String,List<String>>();
		Map<String,List<String>> map_l2 = new HashMap<String,List<String>>();
		
		//Map<Integer,String> map_key1 = new HashMap<Integer,String>();
		//Map<Integer,String> map_key2 = new HashMap<Integer,String>();
		
		//List<Integer> keyxs = new ArrayList<Integer>();
		for (List<String> list : l1) {
			// 如果有重复的，则带上列头做key
			String tnname = list.get(5);
			// 用tnname做key
			if(!map_l1.containsKey(tnname)) {
				map_l1.put(tnname, list);
			} else {
				// 带上列头
				tnname = list.get(0)+list.get(1)+tnname;
				map_l1.put(tnname, list);
			}
			
			// 放入排序，作为输出
			//int i = Integer.valueOf(list.get(0)) * 1000 + Integer.valueOf(list.get(1));
			//if(!keyxs.contains(i)) {
				//keyxs.add(i);
			//}
			// 将key放到对应的keymap中，做排序处理
			//map_key1.put(i, tnname);
		}
		for (List<String> list : l2) {
			// 如果有重复的，则带上列头做key
			String tnname = list.get(5);
			// 用tnname做key
			if(!map_l2.containsKey(tnname)) {
				map_l2.put(tnname, list);
			} else {
				// 带上列头
				tnname = list.get(0)+list.get(1)+tnname;
				map_l2.put(tnname, list);
			}
			
			// 放入排序，作为输出
			//int i = Integer.valueOf(list.get(0)) * 1000 + Integer.valueOf(list.get(1));
			//if(!keyxs.contains(i)) {
			//	keyxs.add(i);
			//}
			// 将key放到对应的keymap中，做排序处理
			//map_key2.put(i, tnname);
		}
		// 排序
		//Collections.sort(keyxs);
		
		JSONArray pbx1ls = new JSONArray();
		JSONArray pbx2ls = new JSONArray();
		
		List<String> map_l2_keys = new ArrayList<String>();
		// 循环map_l1.key
		for (String key : map_l1.keySet()) {
			// 当第二个数组有key时，比较
			JSONArray pb1s = new JSONArray();
			JSONArray pb2s = new JSONArray();
			List<String> jobx1 = map_l1.get(key);
			if(map_l2.containsKey(key)) {
				// 都有时，比较
				map_l2_keys.add(key);
				List<String> jobx2 = map_l2.get(key);
				for (int k = 0; k < jobx1.size(); k++) {
					String x1 = jobx1.get(k) == null ? "null" : jobx1.get(k);
					String x2 = jobx2.get(k) == null ? "null" : jobx1.get(k);
					JSONObject j1 = new JSONObject();
					JSONObject j2 = new JSONObject();
					if(k<20) {
						if(x1.equals(x2)) {
							j1.put("pb", "1");
							j1.put("values", x1);
							pb1s.add(j1);
							pb2s.add(j1);
						} else {
							j1.put("pb", "0");
							j1.put("values", x1);
							j2.put("pb", "0");
							j2.put("values", x2);
							pb1s.add(j1);
							pb2s.add(j2);
						}
					} else {
						j1.put("pb", "1");
						j1.put("values", x1);
						j2.put("pb", "1");
						j2.put("values", x2);
						pb1s.add(j1);
						pb2s.add(j2);
					}
				}
				pbx2ls.add(pb2s);
			} else {
				// 没有时，则只插入pb1s
				for (int k = 0; k < jobx1.size(); k++) {
					JSONObject j1 = new JSONObject();
					j1.put("pb", k<20 ? "0" : "1");
					j1.put("values", jobx1.get(k) == null ? "null" : jobx1.get(k));
					pb1s.add(j1);
				}
			}
			pbx1ls.add(pb1s);
		}
		// map_l2.key
		for (String key : map_l2.keySet()) {
			List<String> jobx2 = map_l2.get(key);
			JSONArray pb2s = new JSONArray();
			// 如果key没有被插入，则插入到pb2s中
			if(!map_l2_keys.contains(key)) {
				for (int k = 0; k < jobx2.size(); k++) {
					JSONObject j1 = new JSONObject();
					j1.put("pb", k<20 ? "0" : "1");
					j1.put("values", jobx2.get(k) == null ? "null" : jobx2.get(k));
					pb2s.add(j1);
				}
			}
			pbx2ls.add(pb2s);
		}
		
	
		
		
		List<List<String>> job1 = new ArrayList<List<String>>();
		List<String> xxs = new ArrayList<String>();
		job1.add(Arrays.asList(new String[] { "1","1","1","SYSPACKSTMT","4","A","I ","DSNKSX01","N","3","NNNN","NNNN"," "," "," ","NCOSUB","1","0","        ","SYSLH100","SYSIBM","SYSIBM","  N", "2021122618480132",""," ","0","","NULLID",null,null,null,null,null}));
		job1.add(Arrays.asList(new String[] { "3","1","1","SYSPACKSTMT","4","A","I ","DSNKSX01","N","3","NNNN","NNNN"," "," "," ","NCOSUB","1","0","        ","SYSLH100","SYSIBM","SYSIBM","  N", "2021122618480132",""," ","0","","NULLID",null,null,null,null,null}));
		
		List<List<String>> job2 = new ArrayList<List<String>>();
		job2.add(Arrays.asList(new String[] { "1","1","1","SYSPACKSTMT","4","A","I ","DSNKSX01","N","3","NNNN","NNNN"," "," "," ","NCOSUB","1","0","        ","SYSLH100","SYSIBM","SYSIBM","  N", "2021122618480132",""," ","0","","NULLID",null,null,null,null,null}));
		job2.add(Arrays.asList(new String[] { "2","1","1","SYSPACKSTMT","4","A","I ","DSNKSX01","N","3","NNNN","NNNN"," "," "," ","NCOSUB","1","0","        ","SYSLH100","SYSIBM","SYSIBM","  N", "2021122618480132",""," ","0","","NULLID",null,null,null,null,null}));
		
		Map<Integer,List<String>> map_job1 = new HashMap<Integer,List<String>>();
		Map<Integer,List<String>> map_job2 = new HashMap<Integer,List<String>>();
		
		List<Integer> keys = new ArrayList<Integer>();
		// 循环数组job1
		for (List<String> list : job1) {
			int i = Integer.valueOf(list.get(0)+list.get(1));
			map_job1.put(i, list);
			if(!keys.contains(i)) {
				keys.add(i);
			}
		}
		// 循环数组job2
		for (List<String> list : job2) {
			int i = Integer.valueOf(list.get(0)) * 10 + Integer.valueOf(list.get(1));
			map_job2.put(i, list);
			if(!keys.contains(i)) {
				keys.add(i);
			}
		}
		Collections.sort(keys);
		
		JSONArray pbx1s = new JSONArray();
		JSONArray pbx2s = new JSONArray();
		// 循环keys 
		for (Integer i : keys) {
			JSONArray pb1s = new JSONArray();
			JSONArray pb2s = new JSONArray();
			if(map_job1.containsKey(i) && map_job2.containsKey(i)) {
				// 都有时，比较
				List<String> jobx1 = map_job1.get(i);
				List<String> jobx2 = map_job2.get(i);
				for (int k = 0; k < jobx1.size(); k++) {
					String x1 = jobx1.get(k) == null ? "null" : jobx1.get(k);
					String x2 = jobx2.get(k) == null ? "null" : jobx1.get(k);
					JSONObject j1 = new JSONObject();
					JSONObject j2 = new JSONObject();
					if(k<20) {
						if(x1.equals(x2)) {
							j1.put("pb", "1");
							j1.put("values", x1);
							pb1s.add(j1);
							pb2s.add(j1);
						} else {
							j1.put("pb", "0");
							j1.put("values", x1);
							j2.put("pb", "0");
							j2.put("values", x2);
							pb1s.add(j1);
							pb2s.add(j2);
						}
					} else {
						j1.put("pb", "1");
						j1.put("values", x1);
						j2.put("pb", "1");
						j2.put("values", x2);
						pb1s.add(j1);
						pb2s.add(j2);
					}
				}
			} else if(map_job1.containsKey(i) && !map_job2.containsKey(i)) {
				// map_job1 有 但是map_job2没有
				List<String> jobx1 = map_job1.get(i);
				for (int k = 0; k < jobx1.size(); k++) {
					JSONObject j1 = new JSONObject();
					JSONObject j2 = new JSONObject();
					j1.put("pb", k<20 ? "0" : "1");
					j1.put("values", jobx1.get(k) == null ? "null" : jobx1.get(k));
					j2.put("pb", k<20 ? "0" : "1");
					j2.put("values", "null");
					pb1s.add(j1);
					pb2s.add(j2);
				}
			} else {
				// map_job2 有 但是map_job1没有
				List<String> jobx1 = map_job2.get(i);
				for (int k = 0; k < jobx1.size(); k++) {
					JSONObject j1 = new JSONObject();
					JSONObject j2 = new JSONObject();
					j2.put("pb", k<20 ? "0" : "1");
					j2.put("values", jobx1.get(k) == null ? "null" : jobx1.get(k));
					j1.put("pb", k<20 ? "0" : "1");
					j1.put("values",  "null");
					pb1s.add(j1);
					pb2s.add(j2);
				}
			}
			pbx1s.add(pb1s);
			pbx2s.add(pb2s);
		}
		System.out.println(pbx1s);
		System.out.println(pbx2s);
		
		
		
		
		List<String> xxzs = new ArrayList<String>();
		xxs.add("11111");
		xxs.add("2222");
		xxs.add("call sysproc");
		xxs.add("xxxxxxxxxx");
		xxs.add("yyyyyyy");
		xxs.add("call sysproc");
		xxs.add("aaaaaaaaa");
		xxs.add("bbbbbbbbbb");
		Map<Integer,List<String>> map = new HashMap<Integer,List<String>>();
		int i = 0;
		for (String xx : xxs) {
			if(!map.containsKey(i)) {
				map.put(i, new ArrayList<String>());
			}
			if(!xx.contains("call sysproc")) {
				List<String> s = map.get(i);
				s.add(xx);
				map.replace(i, s);
			} else {
				List<String> s = new ArrayList<String>();
				s.add(xx);
				map.put(i+1, s);
				i=i+2;
			}
		}
		for (Integer j : map.keySet()) {
			List<String> x = map.get(j);
			// 执行方法
			
		}
		System.out.println(map);
		long y = compare("2021-03-16 13:26",2);
		System.out.println(y);
		String x = iniMonth(1);
		System.out.println(x);
		// System.out.println(compareTo("09:00", "07:00"));
		List<String> userId = new ArrayList<String>();
		
		String xxx = "{'a':'aa','b':'bb'}";
		JSONObject j = JSONObject.parseObject(xxx);
		String yyy = j.toJSONString();
	}
}
