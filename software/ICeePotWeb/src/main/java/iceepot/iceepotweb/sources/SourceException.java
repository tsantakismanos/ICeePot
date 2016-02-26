package iceepot.iceepotweb.sources;



public class SourceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	
	private String message;
	
	public SourceException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
}
