package com.common.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 异常基类
 */
public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<ExceptionCause> causeList = new ArrayList<ExceptionCause>();
	
	private String errorCode;

	public BaseException() {
		super();
	}
	
	public BaseException(String message) {
        super(message);
    }
	
    public BaseException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

	public void addCause(ExceptionCause exceptionCause) {
		this.causeList.add(exceptionCause);
	}

	public List<ExceptionCause> getCauseList() {
		return this.causeList;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
