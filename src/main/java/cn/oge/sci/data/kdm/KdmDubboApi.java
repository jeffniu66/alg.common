package cn.oge.sci.data.kdm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.oge.kdm.service.rtdata.api.RTDataService;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;

public class KdmDubboApi extends KdmDubbo {
	
	private static final long serialVersionUID = 8793189731590373284L;
	private static Logger LOG = LoggerFactory.getLogger(KdmDubboApi.class);
	private String dubboUrl;
	
	public KdmDubboApi(String dubboUrl) {
		this.dubboUrl = dubboUrl;
	}
	
	@Override
	public RTDataService setRTDataService() {
		ApplicationConfig application = new ApplicationConfig();
		application.setName("kdm-customer");

		String address = "N/A";
		RegistryConfig registry = new RegistryConfig(address);
		registry.setTimeout(60000);
		// 获取读写接口
		ReferenceConfig<RTDataService> rtdsRef = new ReferenceConfig<RTDataService>();
		rtdsRef.setApplication(application);
		rtdsRef.setRegistry(registry);
		rtdsRef.setTimeout(60000);
		rtdsRef.setUrl(this.dubboUrl);
		rtdsRef.setInterface(RTDataService.class);

		// RTDataService rtdService = rtdsRef.get();
		RTDataService rtdataService = rtdsRef.get();
		if (rtdataService == null) {
			// TODO 显示错误还是重试
			LOG.error("显示错误");
		}
		return rtdataService;
	}

}
