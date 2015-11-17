package cn.oge.sci.util;

import java.util.List;

import org.oge.common.util.HttpRequest;

import cn.oge.kdm.service.dto.KKSEva;
import cn.oge.kdm.service.dto.RTDataSet;

/**
 * 通过Rest接口获取RDP数据
 *
 */
public class RestDataloader {

	public static List<RTDataSet> getSnapshot(String[] kks, String host, int port) {
		return getRTData(KdmUtils.arr2str(kks), host, port, "getRTDataSnapshot");
	}

	public static List<RTDataSet> getSnapshotPost(String kks, String host, int port) {
		return getRTDataPost(kks, host, port, "getRTDataSnapshot");
	}

	public static List<RTDataSet> getSnapshot(String kks, String host, int port) {
		return getRTData(kks, host, port, "getRTDataSnapshot");
	}

	public static List<RTDataSet> getRTDataHistory(String kks, long startTime, long endTime, String host, int port) {
		return getRTData(kks, startTime, endTime, host, port, "getRTDataHistory", 2);
	}

	public static List<RTDataSet> getRTDataHisZip(String kks, long startTime, long endTime, String host, int port) {
		return getRTData(kks, startTime, endTime, host, port, "getRTDataHistoryZip", 2);
	}

	public static List<RTDataSet> getRTDataTimes(String kks, long startTime, long endTime, String host, int port) {
		return getRTData(kks, startTime, endTime, host, port, "getRTDataHistory", 1);
	}

	public static List<RTDataSet> getRTData(String kks, long startTime, long endTime, String host, int port,
			String method, int resultType) {
		return getRTData(kks, startTime, endTime, host, port, method, resultType, false);
	}

	public static List<RTDataSet> getRTDataPost(String kks, long startTime, long endTime, String host, int port,
			String method, int resultType) {
		return getRTData(kks, startTime, endTime, host, port, method, resultType, true);
	}

	public static List<RTDataSet> getRTData(String kks, long startTime, long endTime, String host, int port,
			String method, int resultType, boolean bPost) {
		String params = "kksCodes=" + kks;
		params += "&startTime=" + startTime + "&endTime=" + endTime;
		params += "&resultType=" + resultType;
		String s = getRdpRest(params, host, port, "kksData", method, bPost);
		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(s);
		return rtdsList;
	}

	public static List<RTDataSet> getRTDataPost(String kks, String host, int port, String method) {
		String s = getRdpRestByKks(kks, host, port, "kksData", method, true);
		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(s);
		return rtdsList;
	}

	public static List<RTDataSet> getRTData(String kks, String host, int port, String method) {

		String s = getRdpRestByKks(kks, host, port, "kksData", method);
		List<RTDataSet> rtdsList = KdmJsonUtils.getRTDataSet(s);
		return rtdsList;
	}

	public static List<KKSEva> getEvaList(String host, int port) {
		String json = getRdpRest(null, host, port, "kksEva", "searchKKSEvas");
		return KdmJsonUtils.getEvaList(json);
	}

	public static String getEvaJson(String host, int port) {
		return getRdpRest(null, host, port, "kksEva", "searchKKSEvas");
	}

	public static String getEvaJson(String query, String host, int port) {
		return getRdpRest(query, host, port, "kksEva", "searchKKSEvas");
	}
	
	public static String getRdpRestByKks(String kks, String host, int port, String module, String method) {
		return getRdpRestByKks(kks, host, port, module, method, false);
	}

	public static String getRdpRestByKks(String kks, String host, int port, String module, String method, boolean bPost) {
		String params = "kksCodes=" + kks;
		String s = getRdpRest(params, host, port, module, method, bPost);
		return s;
	}

	/**
	 * 最基本的Rest请求方法
	 * 
	 * @param params
	 * @param host
	 * @param port
	 * @param module
	 * @param method
	 * @return
	 */
	public static String getRdpRest(String params, String host, int port, String module, String method) {
		return getRdpRest(params, host, port, module, method, false);
	}

	private static String RestVersion = "1.0";
	private static String RestContext = "/kdmService/rest/";

	public static String getRdpRest(String params, String host, int port, String module, String method, boolean bPost) {
		return getRdpRest(params, host, port, RestVersion, module, method, bPost);
	}

	public static String getRdpRest(String params, String host, int port, String restVersion, String module,
			String method, boolean bPost) {
		String url = "http://" + host + ":" + port + RestContext + restVersion;
		return getRdpRest(url, params, module, method, bPost);
	}

	public static String getRdpRest(String restUrl, String params, String module, String method, boolean bPost) {
		restUrl += "/" + module;
		String reqParam = "method=" + method;
		if (params != null) {
			reqParam += "&" + params;
		}
		// TODO 日志
		// System.out.println(restUrl + "?" + reqParam);
		if (bPost) {
			return HttpRequest.sendPost(restUrl, reqParam);
		}
		String s = HttpRequest.sendGet(restUrl, reqParam);
		return s;
	}

}
