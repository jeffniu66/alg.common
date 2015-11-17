package cn.oge.sci.exception;


/**
 * 没有数据，以异常作通知使用
 * 
 * @author jimcoly
 *
 */
@SuppressWarnings("serial")
public class NotTimeSlotException extends Exception {

	public NotTimeSlotException(String msg) {
		super(msg);
	}

}
