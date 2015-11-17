package cn.oge.sci.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.oge.kdm.rdp.center.evaluation.domain.EvaluationData;
import cn.oge.kdm.rdp.center.evaluation.facade.EvaluationFacade;
import cn.oge.kdm.service.dto.RTDataSet;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;

public class DubboDataloader {

	private static Logger logger = LoggerFactory.getLogger(DubboDataloader.class);
	public static EvaluationFacade evaService;

	private static Map<String, Object> serviceMap = new HashMap<String, Object>();

	private static String dubboUrl = "dubbo://127.0.0.1:20883";

	public static void saveEvaResult(List<EvaluationData> evaResultList) {
		if (evaResultList == null || evaResultList.isEmpty()) {
			logger.debug("转换后的评价结果evaDataList集合为空");
			return;
		}
		try {
			logger.debug("：：：开始写评价结果：" + evaResultList);
			getEvaService().writeDataList(evaResultList);
			logger.info("：：：成功写评价结果！本次写入{}条记录。写入时间{}", evaResultList.size(), evaResultList.get(0).getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取服务
	public static EvaluationFacade getEvaService() {
		System.out.println(DubboDataloader.getDubboUrl());
		if (evaService == null) {
			evaService = getRdpService(DubboDataloader.getDubboUrl(),EvaluationFacade.class);
		}
		return evaService;
	}

	public static String getDubboUrl() {
		return dubboUrl;
	}

	public static void setDubboUrl(String dubboHost, int dubboPort) {
		DubboDataloader.dubboUrl = "dubbo://" + dubboHost + ":" + dubboPort;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getRdpService(String url, Class<T> clazz) {
		String key = url + clazz.getName();
		if (null != serviceMap.get(key)) {
			ReferenceConfig<T> reference = (ReferenceConfig<T>) serviceMap.get(key);
			if (reference.get() != null) {
				return reference.get();
			}
		}

		ReferenceConfig<T> reference = new ReferenceConfig<T>();

		ApplicationConfig application = new ApplicationConfig();
		application.setName("KDM-EVA-DATA");
		reference.setApplication(application);

		reference.setRetries(0); // 重试次数
		reference.setActives(100);// 每服务消费者每服务每方法最大并发调用数

		reference.setInterface(clazz.getName());
		reference.setTimeout(60000);// 1分钟的超时时间
		reference.setUrl(url + "/" + clazz.getName());

		T t = reference.get();
		if (t != null) {
			serviceMap.put(key, reference);
		}
		return reference.get();
	}

	public static List<RTDataSet> getSnapshot(String kks) {
		System.out.println(dubboUrl);
		return null;
	}
}
