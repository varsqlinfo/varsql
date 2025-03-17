package com.varsql.web.common.filter;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.varsql.core.configuration.VarsqlWebConfig;
import com.varsql.core.configuration.beans.web.CorsBean;
import com.vartech.common.utils.StringUtils;

@WebFilter("/*")
public class HttpMethodFilter implements Filter {
	
	private static final String arrowMethod;
	
	static {
		String allowedMethods = VarsqlWebConfig.getInstance().getCorsConfig().getAllowedMethods();
		if(StringUtils.isBlank(allowedMethods) || "*".equals(allowedMethods)) {
			arrowMethod ="*";
		}else {
			arrowMethod = StringUtils.allTrim(allowedMethods).toUpperCase()+CorsBean.END_SPLIT_CHAR;
		}
		
	}
	

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String method = httpRequest.getMethod();
        
        method = method.toUpperCase()+CorsBean.END_SPLIT_CHAR;
        
        if (arrowMethod.indexOf(method) < 0) {
            httpResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405 - Method Not Allowed
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}