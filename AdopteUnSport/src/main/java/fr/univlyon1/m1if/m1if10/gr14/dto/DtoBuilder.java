package fr.univlyon1.m1if.m1if10.gr14.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DTO Builder class, even if it more a factory than a builder.
 */
public final class DtoBuilder {
    //Name of the method of Dtos used to make new instance
    private static final String BUILD_METHOD = "newInstance";


    /**
     * Returns a new instance of a DTO, according to model and DTO class passed as arguments.
     * Uses Java reflection to call the new instance method of the Dto.
     * @param <Model> Model type
     * @param <Dto> Dto type
     * @param m Model instance
     * @param dtoClass Class of the Dto
     * @return New instance of the DTO, build from the model
     */
    @SuppressWarnings("unchecked")
    public static <Model, Dto> Dto build(final Model m, final Class<Dto> dtoClass) {
        try {
            return (Dto) dtoClass.getMethod(BUILD_METHOD, m.getClass()).invoke(m.getClass(), m);
        }
        catch (Exception e) {
            Logger.getLogger(DtoBuilder.class.getName()).log(
                Level.WARNING, "Building DTO instance: ", e
            );
            return null;
        }
    }


    /**
     * Returns a list of DTOs, according to model list and the DTO class passed as arguments.
     * @param <Model> Model type
     * @param <Dto> Dto type
     * @param mList List of model instances
     * @param dtoClass Class of the Dto
     * @return List of DTOs, build from the model list
     */
    public static <Model, Dto> List<Dto> buildList(
        final List<Model> mList, final Class<Dto> dtoClass) {
        try {
            List<Dto> dtos = new ArrayList<>();
            for (Model m : mList) {
                dtos.add(build(m, dtoClass));
            }
            return dtos;
        }
        catch (Exception e) {
            Logger.getLogger(DtoBuilder.class.getName()).log(
                Level.WARNING, "Building DTO list: ", e
            );
            return null;
        }
    }


    //Final class
    private DtoBuilder() { }
}
