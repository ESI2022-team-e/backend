package esi.backendnew.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cars")
public class Car{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column(name = "mark")
    private String mark;
    @Column(name = "model")
    private String model;
    @Column(name = "nr_of_seats")
    private int nr_of_seats;
    private String transmission_type;
    private String fuel_type;
    private double daily_cost;
    private int year;
    private String licence_plate;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "car", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Rental> rentals = new HashSet<>();

    public Car() {
    }

    public Car(UUID id, String mark, String model, int nr_of_seats, String transmission_type, String fuel_type, double daily_cost, int year, String licence_plate) {
        this.id = id;
        this.mark = mark;
        this.model = model;
        this.nr_of_seats = nr_of_seats;
        this.transmission_type = transmission_type;
        this.fuel_type = fuel_type;
        this.daily_cost = daily_cost;
        this.year = year;
        this.licence_plate = licence_plate;
    }

    public UUID getId() {
        return id;
    }

    public String getMark() {
        return mark;
    }

    public String getModel() {
        return model;
    }

    public int getNr_of_seats() {
        return nr_of_seats;
    }

    public String getTransmission_type() {
        return transmission_type;
    }

    public String getFuel_type() {
        return fuel_type;
    }

    public double getDaily_cost() {
        return daily_cost;
    }

    public int getYear() {
        return year;
    }

    public String getLicence_plate() {
        return licence_plate;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setNr_of_seats(int nr_of_seats) {
        this.nr_of_seats = nr_of_seats;
    }

    public void setTransmission_type(String transmission_type) {
        this.transmission_type = transmission_type;
    }

    public void setFuel_type(String fuel_type) {
        this.fuel_type = fuel_type;
    }

    public void setDaily_cost(double daily_cost) {
        this.daily_cost = daily_cost;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setLicence_plate(String licence_plate) {
        this.licence_plate = licence_plate;
    }

    public Set<Rental> getRentals() {
        return rentals;
    }

    public void setRentals(Set<Rental> rentals) {
        this.rentals = rentals;
    }
}