package com.example;

import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import jakarta.json.bind.annotation.JsonbProperty;

@JSONMapper
public class Address {

    private String street;

    @JsonbProperty("zip-code")
    private String zipCode;

    private String city;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
