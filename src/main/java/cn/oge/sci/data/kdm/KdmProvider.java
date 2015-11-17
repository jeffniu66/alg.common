package cn.oge.sci.data.kdm;

import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.oge.kdm.service.rtdata.api.RTDataService;
import cn.oge.sci.util.CfgFileDetector;

public class KdmProvider {

	public static String FILE_ALGORITHM_CONF = "conf.properties";
	public static String FILE_KDM_PROVIDER = "kdm-provider.xml";

	private AbstractXmlApplicationContext context = null;

	public KdmProvider(String filePath) {
		context = new ClassPathXmlApplicationContext(filePath);
		context.start();
	}

	public KdmProvider() {
		String config = CfgFileDetector.getFilePath(FILE_KDM_PROVIDER);
		if (CfgFileDetector.LOAD_FROM_ABS_PATH) {
			context = new FileSystemXmlApplicationContext(config);
		} else {
			context = new ClassPathXmlApplicationContext(config);
		}
		context.start();
	}

	/** 获取实时数据的服务 */
	public RTDataService getRtdataService() {
		return (RTDataService) context.getBean("rtdataService");
	}

	public Object getBean(String beanId) {
		return context.getBean(beanId);
	}

	/**
	 * 停止服务，释放资源
	 */
	public void stopService() {
		if (context != null) {
			context.close();
		}
	}

}
