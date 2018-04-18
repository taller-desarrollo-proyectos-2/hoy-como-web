package models;

import com.avaje.ebean.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import play.db.ebean.Model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class OpeningTime extends Model {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

    @Transient
    @JsonIgnore
    private String from;

    @Transient
    @JsonIgnore
    private String to;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fromHour;

    @Temporal(TemporalType.TIMESTAMP)
    private Date toHour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFromHour() {
        return fromHour;
    }

    public void setFromHour(Date fromHour) {
        this.fromHour = fromHour;
    }

    public Date getToHour() {
        return toHour;
    }

    public void setToHour(Date toHour) {
        this.toHour = toHour;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        try {
            this.fromHour = formatter.parse(from);
        }catch(Exception e){
            System.out.println("Error tratando de formatear la fecha");
        }
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        try {
            this.toHour = formatter.parse(to);
        }catch(Exception e){
            System.out.println("Error tratando de formatear la fecha" +  e.getMessage());
        }
        this.to = to;
    }
}
