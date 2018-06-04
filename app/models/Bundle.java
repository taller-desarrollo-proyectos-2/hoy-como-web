package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Bundle {

    @Id
    private Long id;

    @OneToOne
    private Plate plate;

    private boolean optionalAvailable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Plate getPlate() {
        return plate;
    }

    public void setPlate(Plate plate) {
        this.plate = plate;
    }

    public boolean isOptionalAvailable() {
        return optionalAvailable;
    }

    public void setOptionalAvailable(boolean optionalAvailable) {
        this.optionalAvailable = optionalAvailable;
    }
}
