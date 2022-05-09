package esi.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private LocalDateTime pickupDatetime;
    private LocalDateTime dropoffDatetime;
    private String pickupLocation;
    private String dropoffLocation;

    private RequestStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    @JsonIgnore
    private Car car;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    public Request() {
    }

    public Request(UUID id, LocalDateTime pickupDatetime, LocalDateTime dropoffDatetime, String pickupLocation, String dropoffLocation, RequestStatus status, Car car, Customer customer) {
        this.id = id;
        this.pickupDatetime = pickupDatetime;
        this.dropoffDatetime = dropoffDatetime;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
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

    public LocalDateTime getPickupDatetime() {
        return pickupDatetime;
    }

    public void setPickupDatetime(LocalDateTime pickupDate) {
        this.pickupDatetime = pickupDate;
    }

    public LocalDateTime getDropoffDatetime() {
        return dropoffDatetime;
    }

    public void setDropoffDatetime(LocalDateTime dropoffDate) {
        this.dropoffDatetime = dropoffDate;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
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
