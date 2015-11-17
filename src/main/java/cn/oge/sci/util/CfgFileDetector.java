package cn.oge.sci.util;

import java.io.File;

/**
 * <h1>配置文件自动检测器</h1>
 * <p>
 * 脚本运行jar包时，使用-D设置JVM变量ogeapp.home。
 * </p>
 * 示例：<br>
 * java -Dogeapp.home=%~dp0 -jar oge-xxx.jar
 * 
 * @author jimcoly
 *
 */
public class CfgFileDetector {

	/**
	 * 使用-D指定ogeapp JVM变量 java -Dogeapp.home=%~dp0 -jar oge-xxx.jar
	 */
	public static String JVM_OGE_APP_HOME_KEY = "ogeapp.home";

	private static String OGE_ALGORTTHM_HOME = null;
	public static boolean LOAD_FROM_ABS_PATH = false;

	static {

		String oge_algorithm_conf = System.getProperty(JVM_OGE_APP_HOME_KEY);

		if (null != oge_algorithm_conf && !"".equals(oge_algorithm_conf.trim())) {
			OGE_ALGORTTHM_HOME = oge_algorithm_conf;
			LOAD_FROM_ABS_PATH = true;
			/** 删除JVM变量，不污染其他应用 */
			System.clearProperty(JVM_OGE_APP_HOME_KEY);

			if (OGE_ALGORTTHM_HOME.lastIndexOf(File.separator) == -1) {
				OGE_ALGORTTHM_HOME += File.separator;
			}
		}

	}

	public static String getFilePath(String fileName) {
		if (fileName == null) {
			return fileName;
		}
		if (LOAD_FROM_ABS_PATH) {
			if (fileName.startsWith("/") || fileName.startsWith("\\")) {
				fileName = fileName.substring(1);
			}
			return OGE_ALGORTTHM_HOME + fileName;
		}
		return fileName;
	}

}
