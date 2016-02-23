package iceepot.iceepotweb.sources;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
public class SourceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SourceException(String message) {
		super(message);
		
	}
}
