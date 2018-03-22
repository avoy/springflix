package com.lightningbolt.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HttpStatusCodeException extends Exception {
	protected static final long serialVersionUID = 1;
	private String response;
	private int code;
    protected final Log logger = LogFactory.getLog(getClass());


	public HttpStatusCodeException(String response, int code) {
		super(response);
		this.response = response;
		this.code = code;
	}

	public String getResponse() {
		return response;
	}

	public int getCode() {
		return code;
	}

	public void printError() {
		logger.error(String.format("HTTP Status Code: %d Message: %s",
				code, response));
	}
}
