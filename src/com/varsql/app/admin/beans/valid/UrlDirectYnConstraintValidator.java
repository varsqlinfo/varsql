package com.varsql.app.admin.beans.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.varsql.app.admin.beans.Vtconnection;

public class UrlDirectYnConstraintValidator  implements ConstraintValidator<ValidUrlDirectYn, Vtconnection> {

    @Override
    public void initialize(final ValidUrlDirectYn arg0) {

    }

    @Override
    public boolean isValid(final Vtconnection vtConnection, final ConstraintValidatorContext context) {
    	
    	if (!(vtConnection instanceof Vtconnection)) {
            throw new IllegalArgumentException("Illegal method signature, expected parameter of type Vtconnection.");
        }
    	
    	if("Y".equals(vtConnection.getUrlDirectYn())) {
    		String  url = vtConnection.getVurl();
    		
    		if(url ==null || "".equals(url.trim())) {
    			context.disableDefaultConstraintViolation();
    			context.buildConstraintViolationWithTemplate( "{com.varsql.app.admin.beans.valid.ValidUrlDirectYn.url}"  ).addConstraintViolation();
                
    			return false; 
    		}
    	}else {
    		String serverip = vtConnection.getVserverip(); 
    		if(serverip ==null || "".equals(serverip.trim())) {
    			context.disableDefaultConstraintViolation();
    			context.buildConstraintViolationWithTemplate( "{com.varsql.app.admin.beans.valid.ValidUrlDirectYn.serverip}" ).addConstraintViolation();
    			return false; 
    		}
    		
    		String port = vtConnection.getVport(); 
    		if(!(serverip ==null || "".equals(serverip.trim()))) {
    			int portVal = Integer.parseInt(port);
    			
    			if(portVal < 1) {
    				context.disableDefaultConstraintViolation();
        			context.buildConstraintViolationWithTemplate( "{com.varsql.app.admin.beans.valid.ValidUrlDirectYn.port}" ).addConstraintViolation();
    			}
    			return false; 
    		}
    		
    		String databaseName = vtConnection.getVdatabasename(); 
    		if(databaseName ==null || "".equals(databaseName.trim())) {
    			context.disableDefaultConstraintViolation();
    			context.buildConstraintViolationWithTemplate( "{com.varsql.app.admin.beans.valid.ValidUrlDirectYn.databaseName}" ).addConstraintViolation();
    			return false; 
    		}
    	}
    	
        return true;
    }

}