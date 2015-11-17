package cn.oge.sci.util;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Test;

import cn.oge.kdm.rdp.center.evaluation.domain.EvaluationRule;

public class TestRestEvaService {

	@Test
	public void test1() {
		RestEvaService.setRestUrl("192.168.1.147", 8082);
		String evaRule = RestEvaService.getEvaRule(0, 500, true);
		System.out.println(JSONObject.fromObject(evaRule));
	}

	@Test
	public void testGetEvaRuleList() {
		RestEvaService.setRestUrl("192.168.1.147", 8082);
		List<EvaluationRule> evaRuleList = RestEvaService.getEvaRuleList(0, 500);
		System.out.println(JSONArray.fromObject(evaRuleList));
	}

	@Test
	public void testGetEvaRulePage() {
		//RestEvaService.setRestUrl("192.168.1.147", 8082);
		// 内蒙古上都
		RestEvaService.setRestUrl("10.1.128.222", 8080);
		Map<String, Object> pageInfo = RestEvaService.getEvaRulePage(0, 500);
		int totalElements = (int) pageInfo.get("totalElements");
		System.out.println(totalElements);
		System.out.println(JSONObject.fromObject(pageInfo));
	}

}
