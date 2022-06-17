package it.gov.pagopa.logextractor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import it.gov.pagopa.logextractor.annotation.validator.PeriodOf3MonthsValidator;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodOf3MonthsValidator.class)
public @interface PeriodOf3Months {
	String message() default "End date should be maximum 3 months apart from start date";
	
	Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}