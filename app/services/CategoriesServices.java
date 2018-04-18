package services;

import exceptions.CreationException;
import models.Category;
import models.Commerce;

import java.util.Arrays;

public class CategoriesServices {

    public static void create(Category category, Commerce commerce) throws CreationException{
        if(Category.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(category.getName(), commerce.getId())) != null){
            throw new CreationException("Nombre de categoria ya utilizado");
        }
        category.save();
    }
}
