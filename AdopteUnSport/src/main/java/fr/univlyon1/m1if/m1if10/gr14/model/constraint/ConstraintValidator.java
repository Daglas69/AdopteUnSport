package fr.univlyon1.m1if.m1if10.gr14.model.constraint;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;


/**
 * Singleton class to make constraint validations on model class fields.
 * Uses the javax.validation API and specification JSR 380.
 */
public final class ConstraintValidator {
    private final Validator validator;


    /**
     * Takes an object as argument and tries to validate its fields constraints.
     * Returns null if all the constraints have been validated, else return
     * the concatenation of the invalidated constraint error messages.
     * @param <Obj> Object type
     * @param object Object to verify
     * @return String containing the error messages, or null
     */
    public <Obj> String validate(final Obj object) {
        try {
            Set<ConstraintViolation<Obj>> violations = validator.validate(object);
            if (violations.size() > 0) {
                StringBuilder errors = new StringBuilder();
                for (ConstraintViolation<Obj> c : violations) {
                    errors.append(c.getMessage() + " ");
                }
                return errors.toString();
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            Logger.getLogger(ConstraintValidator.class.getName()).log(
                Level.WARNING, "Validating: ", e
            );
            //We should never have an exception, except if the object passed
            //can not be handled by the validator. So we return a non null string to
            //dont disturb the service
            return "Une erreur interne est survenue. Veuillez contacter un administrateur système.";
        }
    }


    /**
     * Access point for the unique instance of the class.
     * @return Unique instance of the class.
     */
    public static ConstraintValidator getInstance() {
        return Holder.INSTANCE;
    }

    //Intern private class responsible of the instance init
    private static class Holder {
        //Unique instance not pre-initialized.
        private static final ConstraintValidator INSTANCE = new ConstraintValidator();
    }

    //Private constructor of the class
    //As the class is final
    private ConstraintValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
     }
}
