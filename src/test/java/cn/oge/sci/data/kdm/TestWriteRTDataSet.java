package cn.oge.sci.data.kdm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.junit.Test;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;
import cn.oge.sci.data.DataLoader;

/**
 * 简单测试写接口，检查部署是否ok
 *
 */
public class TestWriteRTDataSet {

	@Test
	public void 鹤岗() {

		String dubboUrl = "dubbo://10.1.128.222:20883";

		DataLoader dataLoader = new KdmDubboApi(dubboUrl);
		try {
			List<RTDataSet> rtdsList = new ArrayList<RTDataSet>();
			RTDataSet rtds = new RTDataSet();
			String kksCode = "BC253FP3HFK01HF772ZZ00M0DC002AA05";// A磨煤机机械侧评价量
			// String kksCode = "EB000HP5MKB01MK298ZZ01J2CA006AA08";//
			// 磁极伸长偏差85-计算量
			rtds.setKksCode(kksCode);
			List<RTValue> rtdVals = new ArrayList<RTValue>();
			RTValue rtval = new RTValue();
			rtval.setTime(new Date().getTime());
			rtval.setValue(2.0);
			rtdVals.add(rtval);
			rtds.setRTDataValues(rtdVals);
			rtdsList.add(rtds);
			Map<String, Object> result = dataLoader.writeRtds(rtdsList);
			System.out.println(JSONArray.fromObject(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void 青居() {

		String dubboUrl = "dubbo://10.61.70.80:20883";

		DataLoader dataLoader = new KdmDubboApi(dubboUrl);
		try {
			List<RTDataSet> rtdsList = new ArrayList<RTDataSet>();
			RTDataSet rtds = new RTDataSet();
			String kksCode = "EB752HP1MKB01MK297ZZ01J2CA006AA08";// 磁极伸长偏差84-计算量
			rtds.setKksCode(kksCode);
			List<RTValue> rtdVals = new ArrayList<RTValue>();
			RTValue rtval = new RTValue();
			rtval.setTime(new Date().getTime());
			rtval.setValue(2.0);
			rtdVals.add(rtval);
			rtds.setRTDataValues(rtdVals);
			rtdsList.add(rtds);
			Map<String, Object> result = dataLoader.writeRtds(rtdsList);
			System.out.println(JSONArray.fromObject(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
