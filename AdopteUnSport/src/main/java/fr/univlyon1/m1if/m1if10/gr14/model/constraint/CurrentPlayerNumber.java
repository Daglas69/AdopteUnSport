package fr.univlyon1.m1if.m1if10.gr14.model.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@NotNull(message = "Le nombre courant de joueurs ne peut pas être vide.")
@Digits(fraction = 0, integer = 3, message = "Le nombre courant de joueurs n'est pas valide.")
@Min(value = 0, message = "Le nombre courant de joueurs ne peut pas être négatif.")
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface CurrentPlayerNumber {

    /**
     * Invalidation message.
     * @return Error message.
     */
    String message() default "Le nombre courant de joueurs est incorrect.";

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
