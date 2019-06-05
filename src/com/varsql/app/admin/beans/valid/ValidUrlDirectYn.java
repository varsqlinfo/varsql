package com.varsql.app.admin.beans.valid;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UrlDirectYnConstraintValidator.class)
@Target( { ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUrlDirectYn {
 
    String message() default "Invalid field";
    
    String url() default "Invalid url";
    
    String serverip() default "Invalid server ip";
    
    String databaseName() default "Invalid database name";
    
    String port() default "Invalid port";
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
 
}