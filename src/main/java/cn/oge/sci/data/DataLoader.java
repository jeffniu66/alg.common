package cn.oge.sci.data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.sci.exception.GetRtDataException;
import cn.oge.sci.exception.NotRtDataNotice;

/**
 * @author jimcoly
 *
 */
public interface DataLoader extends Serializable {

	/**
	 * 获取时刻值
	 * 
	 * @param kksCodes
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<RTDataSet> getTimeSlots(String kksCodes, Date startTime, Date endTime);

	public List<RTDataSet> getTimeSlots(String kksCodes, Date startTime, Date endTime, Integer machineNum,
			boolean bShowResult) throws Exception;

	/**
	 * 获取块数据
	 * 
	 * @param kksCodes
	 * @param time
	 * @return
	 * @throws GetRtDataException
	 * @throws NotRtDataNotice
	 */
	public List<RTDataSet> getBlockData(String kksCodes, Date time) throws GetRtDataException, NotRtDataNotice;

	/**
	 * 获取块数据
	 * 
	 * @param kksCodes
	 * @param time
	 * @param machineNum
	 * @param bShowResult
	 * @return
	 */
	public List<RTDataSet> getBlockData(String kksCodes, Date time, Integer machineNum, boolean bShowResult);

	/**
	 * 获取实时数据
	 * 
	 * @param kksCodes
	 * @param startTime
	 * @param endTime
	 * @param machineNum
	 * @param bShowResult
	 * @return
	 */
	public List<RTDataSet> getRtData(String kksCodes, Date startTime, Date endTime, Integer machineNum,
			boolean bShowResult);

	/**
	 * 获取实时数据
	 * 
	 * @param kksCodes
	 * @param startTime
	 * @param endTime
	 * @param resultType
	 * @return
	 * @throws GetRtDataException
	 * @throws NotRtDataNotice
	 */
	public List<RTDataSet> getRtData(String kksCodes, Date startTime, Date endTime) throws GetRtDataException,
			NotRtDataNotice;

	/**
	 * 获取最新的数据
	 * 
	 * @param kksCodes
	 * @return
	 * @throws GetRtDataException
	 * @throws NotRtDataNotice
	 */
	public List<RTDataSet> getRTDataSnapshot(String kksCodes) throws GetRtDataException, NotRtDataNotice;

	/**
	 * 获取时刻值列表
	 * 
	 * @param kksCode
	 * @param startTime
	 * @param endTime
	 * @return 长整形时刻值List
	 */
	public List<Long> getTimeSlotsList(String kksCode, Date startTime, Date endTime);

	public List<Long> getTimeSlotsList(String kksCode, Date startTime, Date endTime, Integer machineNo, boolean bShow);
	
	public Map<String, Object> writeRtds(List<RTDataSet> rtdsList);
}
