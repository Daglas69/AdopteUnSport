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


@NotNull(message = "La description ne peut pas être vide.")
@Size(min = 10, message = "La description n'est pas assez longue.")
@Size(max = 1500, message = "La description est trop longue.")
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Description {

    /**
     * Invalidation message.
     * @return Error message.
     */
    String message() default "La desription est incorrecte.";

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
