package cn.oge.sci.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.PropertyNameProcessor;
import cn.oge.kdm.rdp.center.evaluation.domain.EvaluationRule;
import cn.oge.kdm.rdp.center.evaluation.dto.EvaluationRuleDTO;
import cn.oge.kdm.service.dto.KKSEva;
import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;

public class KdmJsonUtils {

	public static List<RTDataSet> getRTDataSet(String json) {
		json = json.trim();
		// {"BasicResponse":{"succeeded":1},"RTDataSets":[{"tag":"0101B0AKA01FA031H",
		// "kksCode":"EB002HP1MKA01MK016BL01J1CA004CA01","RTDataValues":[]}]}
		JSONArray rtdsArray = null;

		Pattern pattern = Pattern.compile("^[ \t]*?\\{");
		Matcher matcher = pattern.matcher(json);
		if (matcher.find()) {
			JSONObject respObj = JSONObject.fromObject(json);
			rtdsArray = respObj.getJSONArray("RTDataSets");
			// Array个数
			// JSONObject rtvalObj = (JSONObject) rtdsArray.get(0);
			// rtdsArray = rtvalObj.getJSONArray("RTDataValues");
		}
		// [{"RTDataValues":[{"time":1426735494000,"value":[]}],"tag":"0101B0AKA01FA031H",
		// "kksCode":"EB002HP1MKA01MK016BL01J1CA004CA01"}]
		else {
			rtdsArray = JSONArray.fromObject(json);
		}

		List<RTDataSet> rtdsList = new ArrayList<RTDataSet>();
		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("RTDataValues", RTValue.class);

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(RTDataSet.class);
		jsonConfig.setClassMap(classMap);
		// JSON --> Java
		// 处理key大小写问题
		jsonConfig.registerJavaPropertyNameProcessor(RTValue.class, new PropertyNameProcessor() {
			@SuppressWarnings("rawtypes")
			public String processPropertyName(Class clazz, String name) {
				return name.toLowerCase();
			}
		});

		int size = rtdsArray.size();
		for (int i = 0; i < size; i++) {
			JSONObject jsonObj = rtdsArray.getJSONObject(i);
			// RTDataValues值为空情况可以适应
			// {"tag":"xxx","kksCode":"yyy","RTDataValues":[]}
			RTDataSet rtds = (RTDataSet) JSONObject.toBean(jsonObj, jsonConfig);
			List<RTValue> rtvalList = rtds.getRTDataValues();
			if (rtvalList == null) {
				rtdsList.add(rtds);
				continue;
			}
			for (RTValue rtval : rtvalList) {
				convertRTValue(rtval);
			}
			rtdsList.add(rtds);
		}

		return rtdsList;
	}

	public static RTValue convertRTValue(RTValue rtval) {
		Object value = rtval.getValue();
		if (value instanceof byte[]) {
			return rtval;
		}

		if (value instanceof Object[]) {
			Object[] a = (Object[]) value;
			byte[] b = new byte[a.length];
			for (int i = 0; i < a.length; i++) {
				b[i] = (byte) Integer.parseInt(a[i].toString());
			}
			rtval.setValue(b);
		}
		return rtval;
	}

	public static String readFile(String jsonPath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(jsonPath);
			return readFile(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	public static String readFile(InputStream inputStream) {

		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(inputStream, "UTF-8");
			br = new BufferedReader(isr);
			StringBuffer sbContent = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null) {
				sbContent.append(line);
			}
			return sbContent.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	public static List<KKSEva> getEvaList(String json) {

		// {"BasicResponse":{"succeeded":1},"KKSEvas":[{
		// "kksCode":"EB002HP3MEJ01ME029BL01J1CB002BB01",
		// "h1":74.0,"m1":50.0,"l1":30.0,"currState":"A",
		// "enable":"1","lowNA":0.0,"heightNA":0.0,"isIncrease":"1",
		// "curr_time":"May 15, 2015 3:57:11 PM","curr_data":25.2224
		// }]}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCollectionType(List.class);
		// net.sf.ezmorph.bean.MorphDynaBean --> cn.oge.kdm.service.dto.KKSEva
		jsonConfig.setRootClass(KKSEva.class);

		JSONObject respObj = JSONObject.fromObject(json, jsonConfig);
		JSONArray evasArray = respObj.getJSONArray("KKSEvas");
		@SuppressWarnings("unchecked")
		List<KKSEva> list = (List<KKSEva>) JSONArray.toCollection(evasArray, jsonConfig);
		return list;
	}

	public static List<EvaluationRule> getEvaRuleList(String json) {

		// {"content":[
		// {"id":"12","kksCode":"EB002HP0MKC01MK012BL01J1CC001BB03","enabled":true,"type":"THRESHOLD",
		// ---rule---
		// "rule":{"isIncrease":true,"highInvalid":5.0,"lowInvalid":1.0,"highLine":4.0,
		// "middleLine":3.0,"lowLine":2.0}}
		// ---page---
		// ],"totalPages":1,"last":true,"totalElements":2,
		// "size":500,"number":0,"first":true,"numberOfElements":2}

		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setCollectionType(List.class);
		// net.sf.ezmorph.bean.MorphDynaBean --> EvaluationRule
		jsonConfig.setRootClass(EvaluationRuleDTO.class);

		JSONObject respObj = JSONObject.fromObject(json);
		JSONArray jsonArray = respObj.getJSONArray("content");
		@SuppressWarnings("unchecked")
		List<EvaluationRule> list = (List<EvaluationRule>) JSONArray.toCollection(jsonArray, jsonConfig);

		return list;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object> getEvaRulePage(String json) {

		// {"content":[
		// {"id":"12","kksCode":"EB002HP0MKC01MK012BL01J1CC001BB03","enabled":true,"type":"THRESHOLD",
		// ---rule---
		// "rule":{"isIncrease":true,"highInvalid":5.0,"lowInvalid":1.0,"highLine":4.0,
		// "middleLine":3.0,"lowLine":2.0}}
		// ---page---
		// ],"totalPages":1,"last":true,"totalElements":2,
		// "size":500,"number":0,"first":true,"numberOfElements":2}

		JsonConfig jsonConfig = new JsonConfig();
		// 去掉content内容，只需要分页信息
		jsonConfig.setExcludes(new String[] { "content" });

		JSONObject respObj = JSONObject.fromObject(json, jsonConfig);
		jsonConfig = new JsonConfig();
		jsonConfig.setRootClass(Map.class);
		Map<String, Object> bean = (Map<String, Object>) JSONObject.toBean(respObj, jsonConfig);
		return bean;
	}

	public static void saveToFile(List<RTDataSet> rtdsList, String filePath) {
		JSONArray jsonArray = JSONArray.fromObject(rtdsList);
		// 保存为json数据
		try {
			FileWriter file = new FileWriter(filePath);
			jsonArray.write(file);
			file.flush();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
