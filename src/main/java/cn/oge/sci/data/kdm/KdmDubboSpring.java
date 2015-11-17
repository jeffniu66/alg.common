package cn.oge.sci.data.kdm;

import cn.oge.kdm.service.rtdata.api.RTDataService;

public class KdmDubboSpring extends KdmDubbo {
	private static final long serialVersionUID = -8303463676939854302L;
	private KdmProvider kdmProvider;

	public KdmDubboSpring(KdmProvider kdmProvider) {
		this.kdmProvider = kdmProvider;
	}

	@Override
	public RTDataService setRTDataService() {
		return this.kdmProvider.getRtdataService();
	}

}
