package com.varsql.web.exception.handler;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.util.NestedServletException;

import com.varsql.core.common.constants.VarsqlConstants;
import com.varsql.core.exception.ConnectionException;
import com.varsql.core.exception.ConnectionFactoryException;
import com.varsql.core.exception.VarsqlRuntimeException;
import com.varsql.web.common.service.CommonLogService;
import com.varsql.web.exception.BlockingUserException;
import com.varsql.web.exception.BoardNotFoundException;
import com.varsql.web.exception.BoardPermissionException;
import com.varsql.web.exception.DataDownloadException;
import com.varsql.web.exception.DatabaseBlockingException;
import com.varsql.web.exception.DatabaseInvalidException;
import com.varsql.web.exception.DatabaseNotFoundException;
import com.varsql.web.exception.VarsqlAccessDeniedException;
import com.varsql.web.exception.VarsqlAppException;
import com.varsql.web.util.SecurityUtil;
import com.varsql.web.util.VarsqlUtils;
import com.vartech.common.app.beans.ResponseResult;
import com.vartech.common.constants.CodeEnumValue;
import com.vartech.common.constants.RequestResultCode;
import com.vartech.common.utils.HttpUtils;
import com.vartech.common.utils.VartechReflectionUtils;
import com.vartech.common.utils.VartechUtils;

/**
 * exception handler
* 
* @fileName	: GlobalExceptionHandler.java
* @author	: ytkim
 */
@ControllerAdvice
public class GlobalExceptionHandler{

	private final Logger logger = LoggerFactory.getLogger("appErrorLog");

	@Autowired
	private CommonLogService commonLogService;

	/**
	 * sql exception 처리.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=SQLException.class)
	public void sqlExceptionHandler(SQLException ex, HttpServletRequest request, HttpServletResponse response){

		insertExceptionLog("sqlExceptionHandler",ex);

		ResponseResult result = new ResponseResult();
		result.setResultCode(RequestResultCode.ERROR);
		result.setMessage(ex.getMessage());

		exceptionRequestHandle(ex, request, response ,result,"connError");
	}


	/**
	 * var sql error 처리.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=VarsqlAppException.class)
	public void varsqlAppExceptionHandler(VarsqlAppException ex, HttpServletRequest request, HttpServletResponse response){

		insertExceptionLog("VarsqlAppException",ex);

		ResponseResult result = new ResponseResult();
		result.setResultCode(ex.getErrorCode());
		result.setMessage(ex.getMessage());

		exceptionRequestHandle(ex, request, response ,result);
	}

	/**
	 * app runtime error
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=VarsqlRuntimeException.class)
	public void varsqlRuntimeExceptionHandler(VarsqlRuntimeException ex, HttpServletRequest request, HttpServletResponse response){

		insertExceptionLog("varsqlRuntimeException",ex);

		ResponseResult result = new ResponseResult();
		result.setResultCode(ex.getErrorCode());
		result.setMessage(ex.getMessage());

		exceptionRequestHandle(ex, request, response ,result);
	}

	/**
	 * 커넥션 에러.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=ConnectionException.class)
	public void connectionExceptionHandler(ConnectionException ex, HttpServletRequest request, HttpServletResponse response){

		insertExceptionLog("connectionException",ex);

		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result,"connError");
	}

	/**
	 * db connection exception
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=ConnectionFactoryException.class)
	public void connectionFactoryExceptionHandler(ConnectionFactoryException ex, HttpServletRequest request, HttpServletResponse response){

		insertExceptionLog("connectionFactoryException",ex);

		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result,"connCreateError");
	}
	
	/**
	 * database not found
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=DatabaseNotFoundException.class)
	public void databaseNotFoundExceptionHandler(DatabaseNotFoundException ex, HttpServletRequest request, HttpServletResponse response){
		
		insertExceptionLog("databaseNotFoundException",ex);
		
		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result,"databaseNotFoundException");
	}

	/**
	 * database invalid exception
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=DatabaseInvalidException.class)
	public void databaseInvalidExceptionHandler(DatabaseInvalidException ex, HttpServletRequest request, HttpServletResponse response){

		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result, "invalidDatabase");
	}

	/**
	 * 접근 체한
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=VarsqlAccessDeniedException.class)
	public void varsqlAccessDeniedExceptionHandler(VarsqlAccessDeniedException ex, HttpServletRequest request, HttpServletResponse response){

		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result,"error403");
	}

	/**
	 * database 차단
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=DatabaseBlockingException.class)
	public void databaseBlockingExceptionHandler(DatabaseBlockingException ex, HttpServletRequest request, HttpServletResponse response){

		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result,"blockingDatabase");
	}

	/**
	 * 차단된 사용자
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=BlockingUserException.class)
	public void blockingUserExceptionHandler(BlockingUserException ex, HttpServletRequest request, HttpServletResponse response){

		ResponseResult result = new ResponseResult();
		exceptionRequestHandle(ex, request, response ,result,"blockingUser");
	}



	/**
	 * runtime 에러 처리.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=RuntimeException.class)
	public void runtimeExceptionHandler(RuntimeException ex, HttpServletRequest request, HttpServletResponse response){

		ResponseResult result = new ResponseResult();
		result.setMessage(ex.getMessage());

		exceptionRequestHandle(ex, request, response ,result);
	}

	/**
	 * class  not found exception 처리.
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value= {ClassNotFoundException.class, NoClassDefFoundError.class})
	public void classExceptionHandler(ClassNotFoundException ex, HttpServletRequest request, HttpServletResponse response){

		insertExceptionLog("classExceptionHandler",ex);

		ResponseResult result = new ResponseResult();
		result.setMessage(ex.getMessage());
		exceptionRequestHandle(ex, request, response ,result);
	}
	
	/**
	 * 로그 추가. 
	 * 
	 * @param string
	 * @param ex
	 */
	private void insertExceptionLog(String string, Throwable ex) {
		commonLogService.insertExceptionLog("sqlExceptionHandler",ex);
	}

	@ExceptionHandler(value=Exception.class)
	public void exceptionHandler(Exception ex, HttpServletRequest request, HttpServletResponse response){

		ResponseResult result = new ResponseResult();

		if(ex instanceof NestedServletException) {
			NestedServletException  nestedServletException= (NestedServletException)ex;

			Throwable throwable= nestedServletException.getRootCause();

			if(throwable instanceof NoClassDefFoundError || throwable instanceof ClassNotFoundException) {
				result.setMessage(ex.getMessage());
			}
		}

		exceptionRequestHandle(ex, request, response ,result);
	}

	/**
	 * file upload error
	 * 
	 * @param request
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MultipartException.class)
    public @ResponseBody ResponseResult multipartexceptionHandler(HttpServletRequest request, Throwable ex) {

        HttpStatus status = getStatus(request);

        ResponseResult result = new ResponseResult();
        result.setResultCode(RequestResultCode.valueOf(status.value()));
        result.setMessage(ex.getMessage());
        return result;
    }

	/**
	 *  404
	 *  
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
    public void noHandlerFoundExceptionHandler(NoHandlerFoundException ex,HttpServletRequest request, HttpServletResponse response) {

		ResponseResult result = new ResponseResult();
		result.setMessage(ex.getMessage());
		exceptionRequestHandle(ex,request, response ,result , "error404");
    }

    /**
     * missing parameter exception
     * 
     * @param ex
     * @param request
     * @param response
     */
	@ExceptionHandler(value=MissingServletRequestParameterException.class)
	public void missingServletRequestParameterExceptionHandler(Exception ex,HttpServletRequest request, HttpServletResponse response){

		logger.error("missingServletRequestParameterExceptionHandler:{}",ex.getMessage() , ex);

		exceptionRequestHandle(ex, request, response, new ResponseResult(), "error403");
	}

	/**
	 * data download exception
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=DataDownloadException.class)
	public void dataDownloadExceptionHandler(DataDownloadException ex, HttpServletRequest request, HttpServletResponse response){
		exceptionRequestHandle(ex, request, response, new ResponseResult(), "dataDownloadError");
	}

	/**
	 * board not found exception
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=BoardNotFoundException.class)
	public void boardNotFoundException(BoardNotFoundException ex, HttpServletRequest request, HttpServletResponse response){
		exceptionRequestHandle(ex, request, response, new ResponseResult(), "boardError");
	}
	
	/**
	 * board permission 
	 * 
	 * @param ex
	 * @param request
	 * @param response
	 */
	@ExceptionHandler(value=BoardPermissionException.class)
	public void boardPermissionException(BoardPermissionException ex, HttpServletRequest request, HttpServletResponse response){
		exceptionRequestHandle(ex, request, response, new ResponseResult(), "boardError");
	}

	private void exceptionRequestHandle(Exception ex, HttpServletRequest request, HttpServletResponse response, ResponseResult result) {
		exceptionRequestHandle(ex, request, response, result, "error500");
	}

	private void exceptionRequestHandle(Exception ex, HttpServletRequest request, HttpServletResponse response, ResponseResult result, String pageName) {

		logger.error("exceptionRequestHandle exception class : {}, url : {}, parameter : {} ", ex.getClass(), request.getRequestURL(), HttpUtils.getServletRequestParam(request));
		logger.error("exceptionRequestHandle {}, {} ",request.getMethod(), ex.getMessage() , ex);

		CodeEnumValue errorCode = RequestResultCode.ERROR;

		if(RequestResultCode.SUCCESS.equals(result.getResultCode())) {
			if(VartechReflectionUtils.hasMethod(ex.getClass(), "getErrorCode")) {
				try {
					Object obj = VartechReflectionUtils.invokeMethod(ex, "getErrorCode", new Object[0]);
					if(obj != null) {
						errorCode = (CodeEnumValue)obj;
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}else {
			errorCode =result.getResultCode();
		}
		
		result.setStatus(RequestResultCode.ERROR.getCode());

		if(VarsqlUtils.isAjaxRequest(request)){
			response.setContentType(VarsqlConstants.JSON_CONTENT_TYPE);
			response.setStatus(HttpStatus.OK.value());
			
			if(ex instanceof DataDownloadException) {
				response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}

			if(errorCode == null) {
				result.setResultCode(RequestResultCode.ERROR);
			}else {
				result.setResultCode(errorCode);
			}

			if(SecurityUtil.isAdmin()) {
				result.setMessage(result.getResultCode() + " :: " + ex.getClass() + ex.getMessage());
			}else {
				if(ex instanceof DataDownloadException) {
					result.setMessage(ex.getMessage());
				}else {
					result.setMessage(result.getResultCode() + " :: " + ex.getClass());
				}
			}
			
			try (Writer writer= response.getWriter()){
				writer.write(VartechUtils.objectToJsonString(result));
			} catch (IOException e) {
				logger.error("exceptionRequestHandle Cause :" + e.getMessage() ,e);
			}
		}else{
			if(SecurityUtil.isAdmin()) {
				request.setAttribute("errorMessage", result.getResultCode() + " :: " + ex.getClass() + ex.getMessage());
			}else {
				if(ex instanceof DataDownloadException) {
					request.setAttribute("errorMessage", ex.getMessage());
				}else {
					request.setAttribute("errorMessage", result.getResultCode() + " :: " + ex.getClass());
				}
			}
			
			try {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/error/"+pageName);
				dispatcher.forward(request, response);
			} catch (ServletException | IOException e1) {
				logger.error("exceptionRequestHandle Cause :" + e1.getMessage() ,e1);
			}
		}
	}

	 private HttpStatus getStatus(HttpServletRequest request) {
	        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
	        if (statusCode == null) {
	            return HttpStatus.INTERNAL_SERVER_ERROR;
	        }
	        return HttpStatus.valueOf(statusCode);
	    }
}
