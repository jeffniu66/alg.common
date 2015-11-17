package cn.oge.sci;

import cn.oge.sci.util.Constant;

public class StatInfo {

	private int errorNumber;

	public String getErrorMsg() {
		int errorNum = this.errorNumber;
		return Constant.desc(errorNum);
	}

	public StatInfo(int errNo) {
		this.errorNumber = errNo;
	}

	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}

	public int getErrorNumber() {
		return errorNumber;
	}
}
