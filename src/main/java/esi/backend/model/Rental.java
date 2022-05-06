package esi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "rentals")
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private LocalDateTime pickup_datetime;

    private LocalDateTime dropoff_datetime;

    private String pickup_location;

    private String dropoff_location;

    private RentalStatus status;

    @ManyToOne
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;
  
    @OneToOne(mappedBy = "rental")
    private Invoice invoice;


    public Rental() {
    }

    public Rental(UUID id, LocalDateTime pickup_datetime, LocalDateTime dropoff_datetime, String pickup_location, String dropoff_location, RentalStatus status, Car car, Customer customer) {
        this.id = id;
        this.pickup_datetime = pickup_datetime;
        this.dropoff_datetime = dropoff_datetime;
        this.pickup_location = pickup_location;
        this.dropoff_location = dropoff_location;
        this.status = status;
        this.car = car;
        this.customer = customer;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getPickup_datetime() {
        return pickup_datetime;
    }

    public void setPickup_datetime(LocalDateTime pickup_date) {
        this.pickup_datetime = pickup_date;
    }

    public LocalDateTime getDropoff_datetime() {
        return dropoff_datetime;
    }

    public void setDropoff_datetime(LocalDateTime dropoff_date) {
        this.dropoff_datetime = dropoff_date;
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

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}