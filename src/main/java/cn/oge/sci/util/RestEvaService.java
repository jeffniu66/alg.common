package cn.oge.sci.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.oge.common.util.HttpRequest;

import cn.oge.kdm.rdp.center.evaluation.domain.EvaluationRule;

public class RestEvaService {

	private static String baseUrl = "http://127.0.0.1:8082/kdmService/rest/2.0/evaluation/";

	public static void setRestUrl(String host, int port) {
		baseUrl = "http://" + host + ":" + port + "/kdmService/rest/2.0/evaluation/";
	}

	public static List<EvaluationRule> getEvaRuleList(int pageIndex, int pageSize) {
		String json = getEvaRule(pageIndex, pageSize);
		return KdmJsonUtils.getEvaRuleList(json);
	}

	public static Map<String, Object> getEvaRulePage(int pageIndex, int pageSize) {
		String json = getEvaRule(pageIndex, pageSize);
		return KdmJsonUtils.getEvaRulePage(json);
	}

	public static String getEvaRule(int pageIndex, int pageSize) {
		return getEvaRule(pageIndex, pageSize, true);
	}

	public static String getEvaRule(int pageIndex, int pageSize, boolean enabled) {
		String restUrl = baseUrl + "rules/types/THRESHOLD";
		Map<String, String> params = new HashMap<String, String>();
		params.put("enabled", Boolean.toString(enabled));
		params.put("page", Integer.toString(pageIndex));
		params.put("size", Integer.toString(pageSize));
		return getRdpRest(restUrl, params, false);
	}

	private static String getRdpRest(String restUrl, Map<String, String> params, boolean bPost) {
		String requstParams = "";
		StringBuffer sbParams = new StringBuffer();
		if (params != null) {
			for (String key : params.keySet()) {
				sbParams.append("&").append(key).append("=").append(params.get(key));
			}
			requstParams = sbParams.substring(1);
		}
		// TODO 日志
		// System.out.println(restUrl + "?" + reqParam);
		if (bPost) {
			return HttpRequest.sendPost(restUrl, requstParams);
		}
		String s = HttpRequest.sendGet(restUrl, requstParams);
		return s;
	}

}
