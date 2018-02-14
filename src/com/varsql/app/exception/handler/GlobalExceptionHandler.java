package com.varsql.app.exception.handler;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.varsql.app.exception.VarsqlAppException;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.connection.pool.ConnectionCreateException;
import com.varsql.core.connection.pool.ConnectionException;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.VartechUtils;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	/**
	 * 
	 * @Method Name  : sqlExceptionHandle
	 * @Method 설명 : sql exception 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value=SQLException.class)
	public void sqlExceptionHandle(SQLException ex, HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error("sqlExceptionHandle "+ getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ResultConst.CODE.ERROR.toInt());
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result);
	}
	
	/**
	 * 
	 * @Method Name  : runtimeExceptionHandle
	 * @Method 설명 : 실행시 에러 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value=RuntimeException.class)
	public void runtimeExceptionHandle(RuntimeException ex, HttpServletRequest request , HttpServletResponse response){
		
		logger.error("runtimeExceptionHandle : ", getClass().getName(),ex);
		ResponseResult result = new ResponseResult();
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result);
	}
	
	/**
	 * 
	 * @Method Name  : epptlExceptionHandle
	 * @Method 설명 : 포틀릿 에러 처리.
	 * @작성자   : ytkim
	 * @작성일   : 2017. 11. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param response
	 * @return
	 */
	@ExceptionHandler(value=VarsqlAppException.class)
	public void epptlExceptionHandle(VarsqlAppException ex, HttpServletRequest request , HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		result.setStatus(ex.getErrorCode() > 0 ? ex.getErrorCode() : ResultConst.CODE.ERROR.toInt());
		result.setMessageCode(ex.getMessageCode());
		result.setMessage(ex.getErrorMessage());
		
		exceptionRequestHandle(request, response ,result);
	}
	
	/**
	 * 
	 * @Method Name  : connectionExceptionHandle
	 * @Method 설명 : 커넥션 에러.
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=ConnectionException.class)
	public void connectionExceptionHandle(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result,"connError");
	}
	
	/**
	 * @Method Name  : connectionCreateExceptionHandle
	 * @Method 설명 :  커넥션 생성 에러
	 * @작성자   : ytkim
	 * @작성일   : 2018. 2. 13. 
	 * @변경이력  :
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=ConnectionCreateException.class)
	public void connectionCreateExceptionHandle(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result ,"connCreateError");
	}
	
	@ExceptionHandler(value=Exception.class)
	public void exceptionHandle(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result);
	}
	
	private void exceptionRequestHandle(HttpServletRequest request, HttpServletResponse response ,ResponseResult result ) {
		exceptionRequestHandle(request ,response , result  , null);
	}
	
	private void exceptionRequestHandle(HttpServletRequest request, HttpServletResponse response ,ResponseResult result, String pageName) {
		if(VarsqlUtil.isAjaxRequest(request)){
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.OK.value());
			result.setStatus(ResultConst.CODE.ERROR.toInt());
			
			Writer writer=null;
			try {
				writer = response.getWriter();
				writer.write(VartechUtils.objectToString(result));
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(writer!=null){ try {writer.close();} catch (IOException e) {}};
			}
		}else{
			try {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/error/"+pageName);
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
	}
}
