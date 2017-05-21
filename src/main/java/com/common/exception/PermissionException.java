package com.common.exception;


/**
 * 权限检查异常
 */
public class PermissionException extends BaseException {
	private static final long serialVersionUID = 1L;

	/** 功能代码 */
	private String taskCode = "";

	/**
	 * 构建一个默认的权限检查异常实例
	 */
	public PermissionException() {
		super();
	}

	/**
	 * 构建一个默认的权限检查异常实例
	 */
	public PermissionException(String taskCode) {
		super(taskCode);
		this.taskCode = taskCode;

	}

	public String getTaskCode() {
		return taskCode;
	}

}

