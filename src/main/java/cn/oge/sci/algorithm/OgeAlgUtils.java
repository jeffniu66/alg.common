package cn.oge.sci.algorithm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.oge.common.decompress.MasterWaveDecompress;
import org.oge.common.decompress.model.MasterHeaderModel;
import org.oge.common.decompress.model.MasterModel;

import cn.oge.kdm.service.dto.RTDataSet;
import cn.oge.kdm.service.dto.RTValue;

public class OgeAlgUtils {

	public static MasterModel[] getMasterModels(List<RTDataSet> rtdsList) throws DataFormatException, IOException {
		int len = rtdsList.size();
		MasterModel[] models = new MasterModel[len];
		for (int i = 0; i < len; i++) {
			RTDataSet rtds = rtdsList.get(i);
			if (rtds == null) {
				models[i] = null;
				continue;
			}
			List<RTValue> rtvalues = rtds.getRTDataValues();
			if (rtvalues == null || rtvalues.isEmpty()) {
				// TODO 做些记录标识之类的
				// 如果要求12个量，获取的数据都要全的话，这里就要给以标识
				System.out.println("[OgeAlgUtils::getMasterModels]获取不到数据 --> kks:" + rtds.getKksCode() + ",tag:"
						+ rtds.getTag());
				continue;
			}
			MasterModel model = getMasterModel(rtvalues.get(0));
			if (model != null) {
				models[i] = model;
				MasterHeaderModel header = model.getHeader();
				if (header == null) {
					System.out.println("获取头部错误");
				}
				// int keyPhaseCounter = header.getKeyPhaseCounter();
			} else {
				System.out.println("转换波形对象错误===");
			}
		}
		return models;
	}

	private static final MasterWaveDecompress decompress = new MasterWaveDecompress();

	public static MasterModel getMasterModel(RTValue values) throws DataFormatException, IOException {
		byte[] waves = (byte[]) values.getValue();
		MasterModel model = decompress.execute(waves);

		// 这里需要
		defendMaster(model); // 需要整理键相吗？
		return model;
	}

	public static MasterModel getMasterModel(List<Double> values) throws DataFormatException, IOException {
		byte[] waves = new byte[values.size()];
		for (int j = 0; j < waves.length; j++) {
			waves[j] = values.get(j).byteValue();
		}
		MasterModel model = decompress.execute(waves);

		// 这里需要
		defendMaster(model); // 需要整理键相吗？
		return model;
	}

	/***
	 * 整理键相，保证键相的合理性
	 * 
	 * @param master
	 */
	private static void defendMaster(MasterModel master) {

		final int waveTimeLen = master.getHeader().getWaveTimeLen();

		// 1、如果小于0，或者如果大于等于包时长，则扔掉
		List<Integer> keyPhases = new ArrayList<Integer>();
		for (int i = 0; i < master.getHeader().getKeyPhaseCounter(); i++) {
			int keyPhase = master.getHeader().getKeyPhaseOffset()[i];
			if (keyPhase < 0) {
				continue;
			}
			if (keyPhase >= waveTimeLen) {
				continue;
			}
			keyPhases.add(keyPhase);
		}
		// 2、如果后一个比前一个大，则留下
		List<Integer> newKeyPhases = new ArrayList<Integer>();
		int max = 0;
		if (keyPhases.size() > 0) {
			newKeyPhases.add(keyPhases.get(0));
			max = keyPhases.get(0);
		}
		for (int i = 0; i < keyPhases.size() - 1; i++) {
			int firstKeyPhase = keyPhases.get(i);
			int secondKeyPhase = keyPhases.get(i + 1);
			if (firstKeyPhase > max) {
				max = firstKeyPhase;
			}
			if (secondKeyPhase > max) {
				newKeyPhases.add(secondKeyPhase);
			}
		}

		// 3、回到键相
		master.getHeader().setKeyPhaseCounter(newKeyPhases.size());
		int[] realKeyPhases = new int[98];
		for (int i = 0; i < newKeyPhases.size(); i++) {
			realKeyPhases[i] = newKeyPhases.get(i);
		}
		master.getHeader().setKeyPhaseOffset(realKeyPhases);

	}
}
