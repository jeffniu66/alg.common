package cn.oge.sci.util;

import java.util.List;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;

public class RtdsTimeHelper {

	private List<RTDataSet> rtdsList;
	private Long minTime;
	private Long maxTime;
	private long maxTimeDiff;
	private boolean validate = false;

	private Long lastTimeCache;

	public RtdsTimeHelper(List<RTDataSet> rtdsList) {
		check(rtdsList);
	}

	public void check(List<RTDataSet> rtdsList) {

		this.rtdsList = rtdsList;

		if (rtdsList == null) {
			errorNo = Constant.DATA_NULL;
			return;
		}

		Long minTime = -1L;
		Long maxTime = -1L;
		boolean isFirst = true;

		for (RTDataSet rtds : rtdsList) {
			List<RTValue> rtval = rtds.getRTDataValues();
			if (rtval == null) {
				validate = false;
				errorNo = Constant.DATA_NOT_FULL;
				return;
			}
			long theTime = rtval.get(0).getTime();

			if (isFirst) {
				minTime = theTime;
				maxTime = theTime;
				isFirst = false;
			} else {

				if (minTime > theTime) {
					minTime = theTime;
				}
				if (maxTime < theTime) {
					maxTime = theTime;
				}
			}
		}
		this.minTime = minTime;
		this.maxTime = maxTime;
		this.maxTimeDiff = maxTime - minTime;

		// 与上次缓存时间进行比较
		if (this.lastTimeCache == null) {
			this.lastTimeCache = minTime;
		} else {
			if (minTime.equals(this.lastTimeCache)) {
				errorNo = Constant.NO_NEW_DATA;
				this.validate = false;
				return;
			} else {
				this.lastTimeCache = minTime;
			}
		}

		// 时间差是否过大，数据包里的时间差大于5分钟，认为是无效数据包
		if (maxTimeDiff > Constant.RTDataSet_Valid_TimeDiff) {
			System.out.println(toString());
			errorNo = Constant.INVALID_TIME_DIFF;
			this.validate = false;
			return;
		}

		this.validate = true;
	}

	private int errorNo = 0;

	public int getErrorNo() {
		return this.errorNo;
	}

	public boolean isValid() {
		return this.validate;
	}

	public Long getMaxTime() {
		return maxTime;
	}

	public Long getMinTime() {
		return minTime;
	}

	@Override
	public String toString() {

		StringBuffer sbInfo = new StringBuffer();
		sbInfo.append(String.format("最大[%1d]-最小[%2d]，时间差：%3d(%4d分钟)", maxTime, minTime, maxTimeDiff,
				maxTimeDiff / 60000));
		sbInfo.append("\n");

		for (RTDataSet rtds : rtdsList) {
			List<RTValue> rtval = rtds.getRTDataValues();
			if (rtval == null) {
				continue;
			}
			long theTime = rtval.get(0).getTime();
			sbInfo.append(String.format("编码：%s，时间：%2d[%2$tY-%2$tm-%2$te %2$tT]", rtds.getKksCode(), theTime));
			sbInfo.append("\n");
		}

		return sbInfo.toString();
	}
}
