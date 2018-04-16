package models;

import com.avaje.ebean.annotation.EnumValue;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Time;
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

    @Temporal(TemporalType.TIME)
    private Time fromHour;

    @Temporal(TemporalType.TIME)
    private Time toHour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getFromHour() {
        return fromHour;
    }

    public void setFromHour(Time fromHour) {
        this.fromHour = fromHour;
    }

    public Time getToHour() {
        return toHour;
    }

    public void setToHour(Time toHour) {
        this.toHour = toHour;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
