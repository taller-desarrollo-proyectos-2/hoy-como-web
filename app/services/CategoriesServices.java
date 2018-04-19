package services;

import exceptions.CreationException;
import exceptions.UpdateException;
import models.Category;
import models.Commerce;

import java.util.Arrays;
import java.util.List;

public class CategoriesServices {

    public static void create(Category category, Commerce commerce) throws CreationException{
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new CreationException("Comercio con id inexistente");
        }
        if(Category.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(category.getName(), commerce.getId())) != null){
            throw new CreationException("Nombre de categoria ya utilizado");
        }
        //Set the last order
        category.setNumber(Category.findLastNumberByCommerce(commerce.getId()));
        category.setCommerce(commerce);
        category.save();
    }

    public static void update(Long id, Category category, Commerce commerce) throws UpdateException{
        if(Commerce.findByProperty("id", commerce.getId()) == null){
            throw new UpdateException("Comercio con id inexistente");
        }
        if(Category.findByProperties(Arrays.asList("name", "commerce.id"), Arrays.asList(category.getName(), commerce.getId())) != null){
            throw new UpdateException("Nombre de categoria ya utilizado");
        }
        Category dbCategory = Category.findByProperty("id", id);
        if(dbCategory == null){
            throw new UpdateException("Categoria con id inexistente");
        }
        if(dbCategory.getNumber() != category.getNumber()){
            if(category.getNumber() > Category.findListByProperty("commerce.id", commerce.getId()).size()){
                throw new UpdateException("Numero de categoria incorrecto");
            }
            updateCategoryNumbers(dbCategory.getNumber(), category.getNumber(), commerce.getId());
        }
        category.setId(id);
        category.setCommerce(commerce);
        category.update();
    }

    public static void updateCategoryNumbers(int oldNumber, int newNumber, Long commerceId){
        List<Category> inBetweenCategories = Category.findListWithInBetweenNumbersAndCommerce(oldNumber, newNumber, commerceId);
        List<Category> higherCategories;
        if(oldNumber > newNumber) {
            higherCategories = Category.findListWithBiggerNumbersAndCommerce(oldNumber, commerceId);
            for (Category category : inBetweenCategories) {
                category.setNumber(category.getNumber() + 1);
                category.update();
            }
        }else{
            higherCategories = Category.findListWithBiggerNumbersAndCommerce(newNumber, commerceId);
            for (Category category : inBetweenCategories) {
                category.setNumber(category.getNumber() - 1);
                category.update();
            }
        }
        for(Category category: higherCategories){
            category.setNumber(category.getNumber() + 1);
            category.update();
        }
    }
}
