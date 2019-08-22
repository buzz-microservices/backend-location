package com.buzzmicroservices.locationservice.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GeoLocation {
    
    private static final String COUNTRY_NAME = "country_name";
    private static final String COUNTRY_CODE = "country_code";
    private String ip;
    private String city;
    @JsonProperty(COUNTRY_NAME)
    private String country;
    @JsonProperty(COUNTRY_CODE)
    private String countryCode;
    private String latitude;
    private String longitude;
    private String timeZoneId;
    

    public GeoLocation() {
    }

    public GeoLocation(String ip, String city, String country, String countryCode, String latitude, String longitude, String timeZoneId) {
        this.ip = ip;
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timeZoneId = timeZoneId;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTimeZoneId() {
        return this.timeZoneId;
    }

    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }

    public GeoLocation ip(String ip) {
        this.ip = ip;
        return this;
    }

    public GeoLocation city(String city) {
        this.city = city;
        return this;
    }

    public GeoLocation country(String country) {
        this.country = country;
        return this;
    }

    public GeoLocation countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public GeoLocation latitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public GeoLocation longitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public GeoLocation timeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GeoLocation)) {
            return false;
        }
        GeoLocation geoLocation = (GeoLocation) o;
        return Objects.equals(ip, geoLocation.ip) && Objects.equals(city, geoLocation.city) && Objects.equals(country, geoLocation.country) && Objects.equals(countryCode, geoLocation.countryCode) && Objects.equals(latitude, geoLocation.latitude) && Objects.equals(longitude, geoLocation.longitude) && Objects.equals(timeZoneId, geoLocation.timeZoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, city, country, countryCode, latitude, longitude, timeZoneId);
    }

    @Override
    public String toString() {
        return "{" +
            " ip='" + getIp() + "'" +
            ", city='" + getCity() + "'" +
            ", country='" + getCountry() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            ", latitude='" + getLatitude() + "'" +
            ", longitude='" + getLongitude() + "'" +
            ", timeZoneId='" + getTimeZoneId() + "'" +
            "}";
    }

    
    

}