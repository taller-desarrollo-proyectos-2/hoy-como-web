package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;

@Entity
public class CommerceCategory extends Model {

    protected static final Finder<Long, CommerceCategory> FIND = new Finder<>(Long.class, CommerceCategory.class);

    private static final List<String> categories = Arrays.asList("Arepas","Bebidas", "Cafetería","Calzones", "Carnes","Celíacos","Comida Árabe" ,
            "Comida Armenia" , "Comida China" , "Comida Hindú" , "Comida Internacional" , "Comida Japonesa" , "Comida Mexicana" ,
            "Comida Peruana" , "Comida Vegana" , "Comida Vegetariana" , "Crepes" , "Cupcakes" , "Desayunos" , "Empanadas" ,
            "Ensaladas" , "Hamburguesas" , "Helados" , "Licuados y Jugos" , "Lomitos" , "Menú del día" , "Milanesas" ,
            "Minimercado" , "Papas Fritas" , "Parrilla" , "Pastas" , "Pescados y Mariscos" , "Picadas" , "Pizzas" ,
            "Pollo" , "Postres" , "Sándwiches" , "Sopas" , "Sushi" , "Tartas" , "Tortilla" , "Viandas y Congelados" ,
            "Waffles" , "Woks");

    @Id
    private Long id;

    private String name;

    public CommerceCategory(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static void initializeCategories(){
        if(FIND.all().isEmpty()){
            for(String category: categories){
                new CommerceCategory(category).save();
            }
        }
    }
}
