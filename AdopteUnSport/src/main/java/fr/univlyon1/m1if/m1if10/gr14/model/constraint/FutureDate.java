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


@NotNull(message = "La date ne peut pas être vide.")
//@Future(message = "La date est déjà passée.") //Create Exceptions
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface FutureDate {

    /**
     * Invalidation message.
     * @return Error message.
     */
    String message() default "La date est incorrecte.";

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
