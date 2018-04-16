package models;

import com.avaje.ebean.annotation.EnumValue;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class OpeningTime extends Model {

    public enum Day{
        @EnumValue("LUNES")
        LUNES,
        @EnumValue("MARTES")
        MARTES,
        @EnumValue("MIERCOLES")
        MIERCOLES,
        @EnumValue("JUEVES")
        JUEVES,
        @EnumValue("VIERNES")
        VIERNES,
        @EnumValue("SABADO")
        SABADO,
        @EnumValue("DOMINGO")
        DOMINGO
    }

    @Id
    private Long id;

    private Day day;

    @Temporal(TemporalType.TIMESTAMP)
    private Date from;

    @Temporal(TemporalType.TIMESTAMP)
    private Date to;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
