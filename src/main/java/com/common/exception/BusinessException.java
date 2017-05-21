package com.common.exception;


/**
 * 业务逻辑异常 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = 1L;

	public BusinessException(String message, String errorCode){
		super(message, errorCode);
	}
	
	public BusinessException(String message){
		super(message);
	}
	
	/**
	 * 构建默认的业务辑异常实	 */
	public BusinessException() {
		super();
	}

	/**
	 * 根据资源文件Key和参数构建业务逻辑异常实例
	 * 
	 * @param messageKey
	 *            资源文件Key
	 * @param arg
	 *            参数	 */
	public BusinessException(String messageKey, Object arg) {
		this(messageKey, new Object[] { arg });
	}

	/**
	 * 根据资源文件Key和参数数组构建业务逻辑异常实例
	 * 
	 * @param messageKey
	 *            资源文件Key
	 * @param args
	 *            参数值数	 */
	public BusinessException(String messageKey, Object[] args) {
		super();
		ExceptionCause cause = new ExceptionCause(messageKey, args);
		addCause(cause);
	}

	/**
	 * 根据 资源文件Key/直接文本 构建业务逻辑异常实例
	 * 
	 * @param messageKey
	 *            资源文件Key/直接文本
	 * @param isResource
	 *            是否资源文件
	 */
	public BusinessException(String messageKey, boolean isResource) {
		super(messageKey);
		ExceptionCause cause = new ExceptionCause(messageKey, isResource);
		addCause(cause);
	}
}
