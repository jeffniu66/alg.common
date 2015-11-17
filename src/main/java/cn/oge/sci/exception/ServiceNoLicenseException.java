package cn.oge.sci.exception;

@SuppressWarnings("serial")
public class ServiceNoLicenseException extends Exception {
	public ServiceNoLicenseException(String msg) {
		super(msg);
	}
}
