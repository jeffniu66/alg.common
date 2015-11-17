package cn.oge.sci.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;

import org.junit.Test;

import cn.oge.kdm.service.dto.KKSEva;
import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;
import cn.oge.sci.data.kdm.KdmDubboApi;

public class TestDataload {

	private static String host = "211.154.164.67";
	private static int port = 5080;

	public static String[] kksArr = { "EB002HP1MKA01MK016BL01J1CA004CA01", "EB002HP1MKA01MK016BM01J1CA004CA01",
			"EB002HP1MKA01MK016BN01J1CA004CA01", "EB002HP1MKA01MK016BP01J1CA004CA01",
			"EB002HP1MKA01MK016BR01J1CA004CA01", "EB002HP1MKA01MK016BQ01J1CA004CA01",
			"EB002HP1MKA01MK016BS01J1CA004CA01", "EB002HP1MKA01MK016BT01J1CA004CA01" };

	// 桐子林1号机组
	public static String[] kksArr2 = { //
	/*----*/"EB001HP0MKA01MK016BL01J1CA004CA01", // +X
			"EB001HP0MKA01MK016BM01J1CA004CA01", // +X+Y
			"EB001HP0MKA01MK016BN01J1CA004CA01", // +Y
			"EB001HP0MKA01MK016BP01J1CA004CA01", // -X+Y
			"EB001HP0MKA01MK016BR01J1CA004CA01", // -X
			"EB001HP0MKA01MK016BQ01J1CA004CA01", // -X-Y
			"EB001HP0MKA01MK016BS01J1CA004CA01", // -Y
			"EB001HP0MKA01MK016BT01J1CA004CA01" // / -X-Y
	};

	@Test
	public void testGetTimes() {
		String kks = kksArr2[0];// 67桐子林vzdb实时模拟数据
		long endTime = new Date().getTime();
		long startTime = endTime - 30 * 60 * 1000;// 半小时
		List<RTDataSet> times = RestDataloader.getRTDataTimes(kks, startTime, endTime, host, port);
		System.out.println(JSONArray.fromObject(times));
	}

	@Test
	public void testGetTimes2() {
		String kks = kksArr[0];// 67向家坝vzdb没有实时模拟数据
		long endTime = 1438358400000l;// 2015,7,1
		long startTime = 1433088000000l;// 2015,5,1
		List<RTDataSet> times = RestDataloader.getRTDataTimes(kks, startTime, endTime, host, port);
		System.out.println(JSONArray.fromObject(times));
	}

	/**
	 * 获取快照数据
	 */
	@Test
	public void testGetSnopshot() {
		String kks = kksArr[0];
		kks = "EB001HP0MKC01MK012BL01J1CC001AA06";// 上导轴承X向摆度
		List<RTDataSet> rtdsList = RestDataloader.getSnapshot(kks, host, port);
		System.out.println(JSONArray.fromObject(rtdsList));
	}

	@Test
	public void testGetHist() {
		String kks = "EB001HP0MKC01MK012BL01J1CC001AA06";// 上导轴承X向摆度
		long endTime = new Date().getTime();
		long startTime = endTime - 1000 * 60 * 3;// 1000 * 60 = 1分钟
		List<RTDataSet> rtdsList = RestDataloader.getRTDataHistory(kks, startTime, endTime, host, port);
		JSONArray jsonArray = JSONArray.fromObject(rtdsList);
		System.out.println(JSONArray.fromObject(jsonArray));
		// 保存为json数据
		try {
			FileWriter file = new FileWriter("target/data.json");
			jsonArray.write(file);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 测试写的方法
	 */
	@Test
	public void testWriteRTDataSet(){
		List<RTDataSet> rtDataSetList = new ArrayList<RTDataSet>(); 
		RTDataSet rtDataSet = new RTDataSet();
		
		List<RTValue> rtValueList = new ArrayList<RTValue>();
		RTValue rtValue = new RTValue();
		rtValue.setTime((new Date()).getTime());
		rtValue.setValue(120);
		rtValueList.add(rtValue);
		
		rtDataSet.setRTDataValues(rtValueList);
		rtDataSet.setKksCode("EB001HP1MKD01MK012BN01J1CC001BB02");
		rtDataSetList.add(rtDataSet);
		String dubboUrl = "dubbo://" + host + ":" + 20883;
		KdmDubboApi kdmDubboApi = new KdmDubboApi(dubboUrl);
		kdmDubboApi.writeRtds(rtDataSetList);
	}
	
	/**
	 * 获取评价配置
	 */
	@Test
	public void testGetEvaList() {
		List<KKSEva> evaList = RestDataloader.getEvaList(host, port);
		System.out.println(JSONArray.fromObject(evaList));
	}

	/**
	 * 获取评价配置JSON
	 */
	@Test
	public void testGetEvaJson() {
		String json = RestDataloader.getEvaJson(host, port);
		System.out.println(json);
	}
}
