package models;

import play.db.ebean.Model;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "PAYMENT_TYPE")
public class PaymentType extends Model {

    @Id
    private Long id;

}
