package models;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author facundocaldora
 */
@Entity
public class Category {

    @Id
    private Long id;

    private String name;

}
