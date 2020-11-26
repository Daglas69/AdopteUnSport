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
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@NotNull(message = "Le nom d'utilisateur ne peut pas être vide.")
@Size(min = 5, message = "Le nom d'utilisateur est trop court.")
@Size(max = 30, message = "Le nom d'utilisateur est trop long.")
@Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Le nom d'utilisateur doit être alphanumérique.")
@Target({ FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface Username {

    /**
     * Invalidation message.
     * @return Error message.
     */
    String message() default "Le nom d'utilisateur est incorrect.";

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
