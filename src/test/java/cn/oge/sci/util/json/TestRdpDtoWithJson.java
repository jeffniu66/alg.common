package cn.oge.sci.util.json;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.junit.Test;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;
import cn.oge.sci.util.KdmJsonUtils;

public class TestRdpDtoWithJson {

	@SuppressWarnings("unchecked")
	@Test
	public void test_JsonArray_toList() {
		String buffer = getJson("space-wave.json");
		JSONArray jsonArray = JSONArray.fromObject(buffer);
		// List<RTDataSet> list = (List<RTDataSet>) JSONArray.toCollection(
		// jsonArray, RTDataSet.class);

		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("RTDataValues", RTValue.class);
		classMap.put("Value", String.class);

		@SuppressWarnings("deprecation")
		List<RTDataSet> rtdsList = (List<RTDataSet>) JSONArray.toList(jsonArray, RTDataSet.class, classMap);
		for (RTDataSet dataset : rtdsList) {
			List<RTValue> rtvList = dataset.getRTDataValues();
			Object value = rtvList.get(0).getValue();
			System.out.println(value);
		}
	}

	@Test
	public void custom_convert() {
		String buffer = getJson("space-wave.json");
		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(buffer);
		System.out.println(JSONArray.fromObject(rtdsList));
	}

	@Test
	public void jsonfile_times() {
		String buffer = getJson("rtds-times.json");
		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(buffer);
		System.out.println(JSONArray.fromObject(rtdsList));
	}

	@Test
	public void testLong() {
		// Time and time
		// 属性名/字段名大小写
		String buffer = "[{\"tag\":\"0101B0AKA01FA031H\",\"kksCode\":\"EB002HP1MKA01MK016BL01J1CA004CA01\","
				+ "\"RTDataValues\":[{\"time\":1433385763000},{\"Time\":1433619751000},"
				+ "{\"Time\":1433798140000},{\"Time\":1433798681000}]}]";

		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(buffer);
		System.out.println(JSONArray.fromObject(rtdsList));
	}

	public static String getJson(String file) {
		InputStream inputStream = TestRdpDtoWithJson.class.getResourceAsStream(file);
		return KdmJsonUtils.readFile(inputStream);
	}

}
