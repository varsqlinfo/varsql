package com.varsql.web.dto.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.varsql.web.dto.db.DBConnectionRequestDTO;

public class UrlDirectYnConstraintValidator  implements ConstraintValidator<ValidUrlDirectYn, DBConnectionRequestDTO> {

    @Override
    public void initialize(final ValidUrlDirectYn arg0) {

    }

    @Override
    public boolean isValid(final DBConnectionRequestDTO vtConnection, final ConstraintValidatorContext context) {

    	if("Y".equals(vtConnection.getUrlDirectYn())) {
    		String  url = vtConnection.getVurl();

    		if(url ==null || "".equals(url.trim())) {
    			context.disableDefaultConstraintViolation();
    			context.buildConstraintViolationWithTemplate( "url not valid"  ).addConstraintViolation();

    			return false;
    		}
    	}else {
    		String serverip = vtConnection.getVserverip();
    		if(serverip ==null || "".equals(serverip.trim())) {
    			context.disableDefaultConstraintViolation();
    			context.buildConstraintViolationWithTemplate( "serverip not valid" ).addConstraintViolation();
    			return false;
    		}

    		String port = vtConnection.getVport();
    		if(port !=null && !"".equals(port.trim())) {
    			int portVal = Integer.parseInt(port);

    			if(portVal < 1) {
    				context.disableDefaultConstraintViolation();
        			context.buildConstraintViolationWithTemplate( "port not valid" ).addConstraintViolation();
        			return false;
    			}
    		}
    	}

    	String databaseName = vtConnection.getVdatabasename();

		if(databaseName ==null || "".equals(databaseName.trim())) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate( "databaseName not valid" ).addConstraintViolation();
			return false;
		}

        return true;
    }

}