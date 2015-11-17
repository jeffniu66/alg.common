package cn.oge.sci.util;

public class Constant {

	public static final int KKS_ISNULL = -10;
	/** 数据为Null */
	public static final int DATA_NULL = -11;
	public static final int RTVALUELIST_ISNULL = -20;
	public static final int ALG_RESULT_ISNULL = -30;
	public static final int CACL_ERROR = -40;
	/** 数据包里时间差无效 */
	public static final int INVALID_TIME_DIFF = -50;
	/** 数据不完整 */
	public static final int DATA_NOT_FULL = -60;

	/** 没有新的数据 */
	public static final int NO_NEW_DATA = -1;

	public static final int CALC_SUCCESS = 10;
	public static final int WRITE_SUCCESS = 20;

	public static Long RTDataSet_Valid_TimeDiff = 5 * 60 * 1000L;// 60*1000表示一分钟，有效时间差5分钟

	public static String desc(int msgNo) {
		String msg = "未知错误";
		switch (msgNo) {
		case KKS_ISNULL:
			msg = "kks为空！";
			break;
		case DATA_NULL:
			msg = "数据为空！";
			break;
		case RTVALUELIST_ISNULL:
			msg = "RtValueList为空！";
			break;
		case ALG_RESULT_ISNULL:
			msg = "算法结果为Null！";
			break;
		case CACL_ERROR:
			msg = "计算结果错误！";
			break;
		case INVALID_TIME_DIFF:
			msg = "数据包内时间差大过指定时间！";
			break;
		case DATA_NOT_FULL:
			msg = "指定输入量数据不完整！";
			break;
		case CALC_SUCCESS:
			msg = "计算成功！";
			break;
		case WRITE_SUCCESS:
			msg = "写入成功！";
			break;
		case NO_NEW_DATA:
			msg = "没有新数据！";
		default:
			break;
		}
		return msg;
	}

}
