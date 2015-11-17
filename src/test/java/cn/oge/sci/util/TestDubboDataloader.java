package cn.oge.sci.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import cn.oge.kdm.rdp.center.evaluation.constants.EvaluationRuleType;
import cn.oge.kdm.rdp.center.evaluation.constants.EvaluationState;
import cn.oge.kdm.rdp.center.evaluation.domain.EvaluationData;
import cn.oge.kdm.rdp.center.evaluation.domain.EvaluationRule;
import cn.oge.kdm.rdp.center.evaluation.dto.EvaluationDataDTO;
import cn.oge.kdm.rdp.center.evaluation.facade.EvaluationFacade;
import cn.oge.kdm.rdp.center.evaluation.facade.EvaluationRuleFacade;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public class TestDubboDataloader {

	@Test
	public void testSetDubboUrl() {
		String dubboUrl = DubboDataloader.getDubboUrl();
		System.out.println("默认的dubboUrl：" + dubboUrl);
		DubboDataloader.setDubboUrl("192.168.1.121", 20883);
		dubboUrl = DubboDataloader.getDubboUrl();
		System.out.println("修改后dubboUrl：" + dubboUrl);
	}

	@Test
	public void testLoadRule() {
		// 小马机
		DubboDataloader.setDubboUrl("192.168.1.147", 20883);
		EvaluationFacade evaService = DubboDataloader.getEvaService();
		Pageable pageInfo = new PageRequest(0, 1000);
		Page<? extends EvaluationRule> rules1 = evaService.findRulesByType(
				EvaluationRuleType.THRESHOLD, true, pageInfo);
		System.out.println(JSONObject.fromObject(rules1));
		rules1.getTotalElements();
		int pages = rules1.getTotalPages();
		System.out.println(pages);
	}

	@Test
	public void testSaveEvaData() {
		// 石静机做服务器
		DubboDataloader.setDubboUrl("192.168.1.121", 20883);
		// 小马机
		DubboDataloader.setDubboUrl("192.168.1.147", 20883);
		List<EvaluationData> dataList = new ArrayList<EvaluationData>();
		EvaluationDataDTO data = new EvaluationDataDTO();
		data.setKksCode("0000000000000000111");
		data.setRuleId("001");
		data.setState(EvaluationState.A);
		Date time = new Date();
		data.setTime(time);
		Double value = 10.1;
		data.setValue(value);
		dataList.add(data);

		EvaluationDataDTO data2 = new EvaluationDataDTO();
		data2.setKksCode("0000000000000000222");
		data2.setRuleId("002");
		data2.setState(EvaluationState.B);
		Date time2 = new Date();
		data2.setTime(time2);
		Double value2 = 10.1;
		data2.setValue(value2);
		dataList.add(data2);
		DubboDataloader.saveEvaResult(dataList);
	}

	@Test
	public void testEvaService() {
		ApplicationConfig application = new ApplicationConfig();
		application.setName("kdm-customer");

		String address = "N/A";
		RegistryConfig registry = new RegistryConfig(address);
		registry.setTimeout(60000);
		// 获取读写接口
		ReferenceConfig<EvaluationFacade> rtdsRef = new ReferenceConfig<EvaluationFacade>();
		rtdsRef.setApplication(application);
		rtdsRef.setRegistry(registry);
		rtdsRef.setTimeout(60000);
		rtdsRef.setUrl("dubbo://192.168.1.121:20883");
		rtdsRef.setInterface(EvaluationFacade.class);

		// RTDataService rtdService = rtdsRef.get();
		EvaluationFacade evaService = rtdsRef.get();
		Pageable pageInfo = new PageRequest(0, 1000);
		Page<? extends EvaluationRule> rules1 = evaService
				.findRulesByType(EvaluationRuleType.THRESHOLD, true, pageInfo);
		System.out.println(JSONObject.fromObject(rules1));
		rules1.getTotalElements();
		int pages = rules1.getTotalPages();
		System.out.println(pages);
	}

	@Test
	public void testEvaService2() {
		ApplicationConfig application = new ApplicationConfig();
		application.setName("kdm-customer");

		String address = "N/A";
		RegistryConfig registry = new RegistryConfig(address);
		registry.setTimeout(60000);
		// 获取读写接口
		ReferenceConfig<EvaluationRuleFacade> rtdsRef = new ReferenceConfig<EvaluationRuleFacade>();
		rtdsRef.setApplication(application);
		rtdsRef.setRegistry(registry);
		rtdsRef.setTimeout(60000);
		rtdsRef.setUrl("dubbo://192.168.1.121:20883");
		rtdsRef.setInterface(EvaluationFacade.class);

		// RTDataService rtdService = rtdsRef.get();
		EvaluationRuleFacade evaService = rtdsRef.get();
		Pageable pageInfo = new PageRequest(0, 1000);
		Page<? extends EvaluationRule> rules1 = evaService
				.findRulesByType(EvaluationRuleType.THRESHOLD, true, pageInfo);
		System.out.println(JSONObject.fromObject(rules1));
		rules1.getTotalElements();
		int pages = rules1.getTotalPages();
		System.out.println(pages);
	}
}
