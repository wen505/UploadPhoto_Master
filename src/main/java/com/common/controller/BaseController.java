package com.common.controller;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.ParameterizedType;

/**
 * 公共控制类
 * @author liupeng
 *
 * @param <T>
 */
public class BaseController<T> {
	
	private   Class<T> cls;
	/**
	 * 日志
	 */
	protected  Logger logger;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private HttpSession session;
	/**当前页*/
	private String page;
	/**每页显示条数*/
	private String rows;

	protected BaseController() {
		ParameterizedType type = (ParameterizedType) getClass() .getGenericSuperclass();
		cls = (Class<T>) type.getActualTypeArguments()[0];
		logger = Logger.getLogger(cls);
	}

	@ModelAttribute
	public void setReqAndResp(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
		this.session = request.getSession();
		this.rows = request.getParameter("rows");
		this.page = request.getParameter("page");
	}

	protected  String getRealPath(){
		return getSession().getServletContext().getRealPath("");
	}

	public Logger getLogger() {
		return logger;
	}


	
	public HttpServletRequest getRequest() {
		return request;
	}


	public HttpServletResponse getResponse() {
		return response;
	}


	public HttpSession getSession() {
		return session;
	}

	public String getPage() {
		return page;
	}

	public String getRows() {
		return rows;
	}

	
}
