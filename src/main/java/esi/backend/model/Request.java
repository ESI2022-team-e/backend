package esi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name= "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String pickup_date;
    private String dropoff_date;
    private String pickup_time;
    private String dropoff_time;
    private String pickup_location;
    private String dropoff_location;
    private Status status; // TODO can we use the same status?
    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;
    // TODO add customer

    public Request() {

    }

    public Request(UUID id, String pickup_date, String dropoff_date, String pickup_time, String dropoff_time, String pickup_location, String dropoff_location, Status status, Car car) {
        this.id = id;
        this.pickup_date = pickup_date;
        this.dropoff_date = dropoff_date;
        this.pickup_time = pickup_time;
        this.dropoff_time = dropoff_time;
        this.pickup_location = pickup_location;
        this.dropoff_location = dropoff_location;
        this.status = status;
        this.car = car;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPickup_date() {
        return pickup_date;
    }

    public void setPickup_date(String pickup_date) {
        this.pickup_date = pickup_date;
    }

    public String getDropoff_date() {
        return dropoff_date;
    }

    public void setDropoff_date(String dropoff_date) {
        this.dropoff_date = dropoff_date;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getDropoff_time() {
        return dropoff_time;
    }

    public void setDropoff_time(String dropoff_time) {
        this.dropoff_time = dropoff_time;
    }

    public String getPickup_location() {
        return pickup_location;
    }

    public void setPickup_location(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
