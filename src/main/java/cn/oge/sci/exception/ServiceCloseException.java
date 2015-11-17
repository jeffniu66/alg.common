package cn.oge.sci.exception;

@SuppressWarnings("serial")
public class ServiceCloseException extends Exception {
	public ServiceCloseException(String msg) {
		super(msg);
	}
}
