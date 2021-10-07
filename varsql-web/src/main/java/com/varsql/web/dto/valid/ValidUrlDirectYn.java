package com.varsql.web.dto.valid;

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

    int port() default 0;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}