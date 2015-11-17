package cn.oge.sci.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;

public class KdmUtils {

	/** 有效时间差 */
	public static Long RTDataSet_Valid_TimeDiff = 5 * 60 * 1000L;// 60*1000表示一分钟，有效时间差5分钟

	public static List<Double> convert(RTDataSet rtds) {
		// 存在有空的情况
		// {"BasicResponse":{"succeeded":1},"RTDataSets":[{"tag":"0101A0AKA13FA031H",
		// "kksCode":"EB002HP0MKA01MK016BS01J1CA004CA01","RTDataValues":[]}]}
		List<RTValue> values = rtds.getRTDataValues();
		if (values.isEmpty()) {
			return null;
		}

		// "{'BasicResponse':{'succeeded':1},'RTDataSets':[{
		// 'Code':'EB136HP1MKD01HE001MV01J1CC001CA01',
		// 'kksCode':'EB136HP1MKD01HE001MV01J1CC001CA01',
		// 'RTDataValues':[{'Time':'2014-05-16 16:30:08.789',
		// 'Value':[69,30,127,1,50,82,0,0,-104]}]}]}"

		List<Double> list = new ArrayList<Double>();

		for (RTValue val : values) {
			String st = val.toString();
			list.add(Double.parseDouble(st));
		}

		return list;
	}

	/**
	 * 校验List<RTDataSet>里时间差是否有效
	 * 
	 * @param rtdsList
	 * @return
	 */
	public static boolean validTimeDiff(List<RTDataSet> rtdsList) {
		Long maxTimeDiff = KdmUtils.maxTimeDiff(rtdsList);
		if (maxTimeDiff > RTDataSet_Valid_TimeDiff) {
			System.out.println("时间差：4815000-->" + 4815000 / 60000 + "分钟");
			showRtdsListTime(rtdsList);
			return false;
		}
		return true;
	}

	public static void showRtdsListTime(List<RTDataSet> rtdsList) {
		for (RTDataSet rtds : rtdsList) {
			List<RTValue> rtval = rtds.getRTDataValues();
			if (rtval == null) {
				continue;
			}
			long theTime = rtval.get(0).getTime();
			System.out.println(String.format("编码：%s，时间：%2d[%2$tY-%2$tm-%2$te %2$tT]", rtds.getKksCode(), theTime));
		}
	}

	/**
	 * 求List<RTDataSet>里最大的时间差
	 * 
	 * @param rtdsList
	 * @return
	 */
	public static Long maxTimeDiff(List<RTDataSet> rtdsList) {

		Long minTime = -1L;
		Long maxTime = -1L;
		boolean isFirst = true;
		for (RTDataSet rtds : rtdsList) {
			List<RTValue> rtval = rtds.getRTDataValues();
			if (rtval == null) {
				continue;
			}
			long theTime = rtval.get(0).getTime();

			if (isFirst) {
				minTime = theTime;
				maxTime = theTime;
				isFirst = false;
			} else {

				if (minTime > theTime) {
					minTime = theTime;
				}
				if (maxTime < theTime) {
					maxTime = theTime;
				}
			}
		}

		return maxTime - minTime;
	}


	/**
	 * 根据传入的KKS数组顺序调整RTDataSet列表数据的顺序
	 * 
	 * @param rtdsList
	 * @param kksArr
	 * @return
	 */
	public static List<RTDataSet> sortRtds(List<RTDataSet> rtdsList, String[] kksArr) {
		Map<String, RTDataSet> mapRtds = new HashMap<String, RTDataSet>();
		for (RTDataSet rtds : rtdsList) {
			mapRtds.put(rtds.getKksCode(), rtds);
		}
		List<RTDataSet> newList = new ArrayList<RTDataSet>();
		for (String kks : kksArr) {
			if (kks == null) {
				newList.add(null);
			} else {
				newList.add(mapRtds.get(kks));
			}
		}
		return newList;
	}

	public static String arr2str(String[] array) {
		StringBuffer sbStr = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null) {
				continue;
			}
			sbStr.append(",").append(array[i]);
		}

		if (sbStr.length() > 0) {
			return sbStr.substring(1);
		}
		return "";
	}

}
