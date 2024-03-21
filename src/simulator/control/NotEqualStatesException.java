package simulator.control;

public class NotEqualStatesException extends Exception {
	public NotEqualStatesException() { 
		super(); 
	}
	
	public NotEqualStatesException(String message) { 
		super(message);
	}
	
	public NotEqualStatesException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NotEqualStatesException(Throwable cause) { 
		super(cause); 
	}
	
	public NotEqualStatesException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}


}
