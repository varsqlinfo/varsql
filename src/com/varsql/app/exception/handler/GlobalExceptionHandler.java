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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.NestedServletException;

import com.varsql.app.common.service.CommonServiceImpl;
import com.varsql.app.exception.DatabaseInvalidException;
import com.varsql.app.exception.VarsqlAppException;
import com.varsql.app.util.VarsqlUtil;
import com.varsql.core.exception.ConnectionException;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.ResultConst;
import com.vartech.common.utils.VartechUtils;

/**
 * 
*-----------------------------------------------------------------------------
* @PROJECT	: varsql
* @NAME		: GlobalExceptionHandler.java
* @DESC		: exception handler 
* @AUTHOR	: ytkim
*-----------------------------------------------------------------------------
  DATE			AUTHOR			DESCRIPTION
*-----------------------------------------------------------------------------
* 2019. 4. 16. 			ytkim			최초작성

*-----------------------------------------------------------------------------
 */
@ControllerAdvice
public class GlobalExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger("appErrorLog");
	
	@Autowired
	private CommonServiceImpl commonServiceImpl;
	
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
	public void sqlExceptionHandler(SQLException ex, HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error("sqlExceptionHandle "+ getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		result.setResultCode(ResultConst.CODE.ERROR.toInt());
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result,"connError");
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
	public void varsqlAppExceptionHandler(VarsqlAppException ex, HttpServletRequest request , HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		commonServiceImpl.insertExceptionLog("VarsqlAppException",ex);
		
		ResponseResult result = new ResponseResult();
		result.setResultCode(ex.getErrorCode() > 0 ? ex.getErrorCode() : ResultConst.CODE.ERROR.toInt());
		result.setMessageCode(ex.getMessageCode());
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result);
	}
	
	/**
	 * 
	 * @Method Name  : varsqlRuntimeException
	 * @Method 설명 : runtime error
	 * @작성자   : ytkim
	 * @작성일   : 2018. 4. 5. 
	 * @변경이력  :
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=VarsqlRuntimeException.class)
	public void varsqlRuntimeExceptionHandler(VarsqlRuntimeException ex, HttpServletRequest request , HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		commonServiceImpl.insertExceptionLog("varsqlRuntimeException",ex);
		
		ResponseResult result = new ResponseResult();
		result.setResultCode(ex.getErrorCode() > 0 ? ex.getErrorCode() : ResultConst.CODE.ERROR.toInt());
		result.setMessageCode(ex.getMessageCode());
		result.setMessage(ex.getMessage());
		
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
	public void connectionExceptionHandler(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		commonServiceImpl.insertExceptionLog("connectionException",ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result,"connError");
	}
	
	/**
	 * 
	 * @Method Name  : connectionFactoryException
	 * @Method 설명 : db connection exception
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 15. 
	 * @변경이력  :
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=ConnectionFactoryException.class)
	public void connectionFactoryExceptionHandler(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		commonServiceImpl.insertExceptionLog("connectionFactoryException",ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result,"connCreateError");
	}
	
	/**
	 * 
	 * @Method Name  : databaseInvalidExceptionHandle
	 * @Method 설명 : database invalid exception
	 * @작성자   : ytkim
	 * @작성일   : 2019. 4. 12. 
	 * @변경이력  :
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=DatabaseInvalidException.class)
	public void databaseInvalidExceptionHandler(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result,"invalidDatabasePage");
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
	public void runtimeExceptionHandler(RuntimeException ex, HttpServletRequest request , HttpServletResponse response){
		
		logger.error("runtimeExceptionHandle : ", getClass().getName(),ex);
		ResponseResult result = new ResponseResult();
		result.setMessage(ex.getMessage());
		
		exceptionRequestHandle(request, response ,result);
	}
	
	/**
	 * 
	 * @Method Name  : classNotFoundExceptionHandler
	 * @Method 설명 : class  not found exception 처리. 
	 * @작성자   : ytkim
	 * @작성일   : 2019. 9. 7. 
	 * @변경이력  :
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value= {ClassNotFoundException.class, NoClassDefFoundError.class})
	public void classExceptionHandler(RuntimeException ex, HttpServletRequest request , HttpServletResponse response){
		
		logger.error("classExceptionHandler : ", getClass().getName(),ex);
		ResponseResult result = new ResponseResult();
		result.setMessage(ex.getMessage());
		exceptionRequestHandle(request, response ,result);
	}
	
	@ExceptionHandler(value=Exception.class)
	public void exceptionHandler(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error("exceptionHandler : {}",getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		
		if(ex instanceof NestedServletException) {
			NestedServletException  nestedServletException= (NestedServletException)ex;
			
			Throwable throwable= nestedServletException.getRootCause();
			
			if(throwable instanceof NoClassDefFoundError || throwable instanceof ClassNotFoundException) {
				result.setMessage(ex.getMessage());
			}
		}
		
		exceptionRequestHandle(request, response ,result);
	}
	
	@ExceptionHandler(value=MissingServletRequestParameterException.class)
	public void missingServletRequestParameterExceptionHandle(Exception ex,HttpServletRequest request ,  HttpServletResponse response){
		
		logger.error(getClass().getName(),ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(request, response ,result, "error403");
	}
	
	private void exceptionRequestHandle(HttpServletRequest request, HttpServletResponse response ,ResponseResult result ) {
		exceptionRequestHandle(request ,response , result  , "error500");
	}
	
	private void exceptionRequestHandle(HttpServletRequest request, HttpServletResponse response ,ResponseResult result, String pageName) {
		if(VarsqlUtil.isAjaxRequest(request)){
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(HttpStatus.OK.value());
			result.setResultCode(ResultConst.CODE.ERROR.toInt());
			
			Writer writer=null;
			try {
				writer = response.getWriter();
				writer.write(VartechUtils.objectToString(result));
			} catch (IOException e) {
				logger.error("exceptionRequestHandle Cause :" + e.getMessage() ,e);
			}finally{
				if(writer!=null){ try {writer.close();} catch (IOException e) {}};
			}
		}else{
			try {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/error/"+pageName);
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e1) {
				logger.error("exceptionRequestHandle Cause :" + e1.getMessage() ,e1);
			}
		}
	}
}
