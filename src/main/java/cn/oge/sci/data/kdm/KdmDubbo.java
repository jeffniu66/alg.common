package cn.oge.sci.data.kdm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.oge.kdm.common.util.ServiceUtil;
import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;
import cn.oge.kdm.service.rtdata.api.RTDataService;
import cn.oge.sci.data.DataLoader;
import cn.oge.sci.exception.GetRtDataException;
import cn.oge.sci.exception.NotRtDataNotice;

public abstract class KdmDubbo implements DataLoader {

	private static final long serialVersionUID = 824897614949017814L;

	private static Logger LOG = LoggerFactory.getLogger(KdmDubbo.class);

	public static int RTDAT_RESULT_TYPE_TIME = 1;
	public static int RTDAT_RESULT_TYPE_TIME_DATA = 2;

	private String noDataMsg = "no data";
	private RTDataService rtdataService;

	public KdmDubbo() {
	}

	public abstract RTDataService setRTDataService();

	public RTDataService getRTDataService() {
		if (rtdataService != null) {
			return rtdataService;
		}
		return setRTDataService();
	}

	public List<RTDataSet> getTimeSlots(String kksCodes, Date startTime, Date endTime) {
		return getData(kksCodes, startTime, endTime, null, false, RTDAT_RESULT_TYPE_TIME);
	}

	public List<RTDataSet> getTimeSlots(String kksCodes, Date startTime, Date endTime, Integer machineNum,
			boolean bShowResult) {
		return getData(kksCodes, startTime, endTime, machineNum, bShowResult, RTDAT_RESULT_TYPE_TIME);
	}

	public List<RTDataSet> getBlockData(String kksCodes, Date time) {
		return getBlockData(kksCodes, time, null, false);
	}

	public List<RTDataSet> getBlockData(String kksCodes, Date time, Integer machineNum, boolean bShowResult) {
		return getData(kksCodes, time, time, machineNum, bShowResult, RTDAT_RESULT_TYPE_TIME_DATA);
	}

	@Override
	public List<RTDataSet> getRtData(String kksCodes, Date startTime, Date endTime) {
		return getData(kksCodes, startTime, endTime, null, false, RTDAT_RESULT_TYPE_TIME_DATA);
	}

	public List<RTDataSet> getRtData(String kksCodes, Date startTime, Date endTime, Integer machineNum,
			boolean bShowResult) {
		return getData(kksCodes, startTime, endTime, machineNum, bShowResult, RTDAT_RESULT_TYPE_TIME_DATA);
	}

	public List<RTDataSet> getData(String kksCodes, Date startTime, Date endTime, Integer machineNum,
			boolean bShowResult, int returnType) {
		if (machineNum == null) {
			machineNum = 0;
		}
		try {
			List<RTDataSet> blockData = getData(kksCodes, startTime, endTime, returnType);
			if (blockData == null || blockData.size() == 0) {
				bShowResult = false;
				LOG.debug("{}机组没有数据！", machineNum);
			} else {
				LOG.debug("{}机组有数据！", machineNum);
			}
			if (bShowResult) {
				LOG.debug(JSONArray.fromObject(blockData).toString());
			}
			return blockData;
		} catch (Exception ex) {
			LOG.error("获取失败{}机组块数据失败！", machineNum, ex);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<RTDataSet> getData(String kksCodes, Date startTime, Date endTime, int returnType)
			throws GetRtDataException, NotRtDataNotice {

		RTDataService rtdataService = getRTDataService();

		// 采样步长
		int sampled = 0;
		Map<String, Object> rtBlockDataTimes = rtdataService.getRTDataHistory(kksCodes, startTime.getTime(),
				endTime.getTime(), sampled, returnType);

		if (ServiceUtil.isError(rtBlockDataTimes)) {
			String errorMsg = ServiceUtil.getErrorMessage(rtBlockDataTimes);
			if (noDataMsg.equals(errorMsg)) {
				throw new NotRtDataNotice("本次时间段没有数据");
			} else {
				LOG.error("获取不到时间段，出错信息：{}", rtBlockDataTimes.get("errorMessage"));
				throw new GetRtDataException("获取实时数据异常");
			}
		}
		Object object = rtBlockDataTimes.get("data");
		if (object instanceof List) {
			return (List<RTDataSet>) object;
		}

		throw new GetRtDataException("获取实时数据异常");
	}

	@SuppressWarnings("unchecked")
	public List<RTDataSet> getRTDataSnapshot(String kksCodes) throws GetRtDataException, NotRtDataNotice {

		RTDataService rtdataService = getRTDataService();
		Map<String, Object> respMap = rtdataService.getRTDataSnapshot(kksCodes);

		if (ServiceUtil.isError(respMap)) {
			String errorMsg = ServiceUtil.getErrorMessage(respMap);
			if (noDataMsg.equals(errorMsg)) {
				throw new NotRtDataNotice("本次时间段没有数据");
			} else {
				LOG.error("获取不到时间段，出错信息：{}", respMap.get("errorMessage"));
				throw new GetRtDataException("获取实时数据异常");
			}
		}
		Object object = respMap.get("data");
		if (object instanceof List) {
			return (List<RTDataSet>) object;
		}

		throw new GetRtDataException("获取实时数据异常");
	}

	public List<Long> getTimeSlotsList(String kksCode, Date startTime, Date endTime) {
		return getTimeSlotsList(kksCode, startTime, endTime, null, false);
	}

	public List<Long> getTimeSlotsList(String kksCode, Date startTime, Date endTime, Integer machineNo, boolean bShow) {

		// 新接口，时刻值为long型，而不再试字符串格式
		List<RTDataSet> timeSlotsWrap = getTimeSlots(kksCode, startTime, endTime, machineNo, bShow);
		if (timeSlotsWrap == null || timeSlotsWrap.size() == 0) {
			return new ArrayList<Long>();
		}
		List<RTValue> tsList = timeSlotsWrap.get(0).getRTDataValues();
		if (tsList == null || tsList.size() == 0) {
			return new ArrayList<Long>();
		}
		List<Long> ltsList = new ArrayList<Long>();
		for (RTValue rtValue : tsList) {
			ltsList.add(rtValue.getTime());
		}
		return ltsList;
	}

	public Map<String, Object> writeRtds(List<RTDataSet> rtdsList) {
		RTDataService rtdService = getRTDataService();
		return rtdService.writeRTDataListHis(rtdsList);
	}
}
