package ca.jonnybauer.watched.Models;


/**
 *
 * This class is used to represent a theatre that has been loaded from the users location and saved into the database
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class Theatre {

    // Properties
    private int id;                  // Unique ID for the theatre
    private String placesID;
    private String name;             // Name of the theatre
    private String address;          // Address of the theatre
    private Double latitude;
    private Double longitude;
    private String phone;            // Phone number for the theatre
    private String website;          // Website for the theatre
    private int favourite = 0;       // Whether the user has marked the theatre as a favourite represented as an int. Default as false (0)

    // Constructors
    public Theatre(){}

    public Theatre(int id, String placesID, String name, String address, Double latitude, Double longitude, String phone, String website, int favourite) {
        this.id = id;
        this.placesID = placesID;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.website = website;
        this.favourite = favourite;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlacesID() {
        return placesID;
    }

    public void setPlacesID(String placesID) {
        this.placesID = placesID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
