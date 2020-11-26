package fr.univlyon1.m1if.m1if10.gr14.model.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@NotNull
@Size(min = 1, max = 50)
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Text {

    /**
     * Invalidation message.
     * @return Error message.
     */
    String message() default "Le texte est incorrect.";

    /**
     * Groups.
     * @return Class groups.
     */
    Class<?>[] groups() default {};

    /**
     * Payload.
     * @return Payload class.
     */
    Class<? extends Payload>[] payload() default {};
}
