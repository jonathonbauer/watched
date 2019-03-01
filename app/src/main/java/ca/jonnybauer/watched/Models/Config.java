package ca.jonnybauer.watched.Models;

/**
 *
 * This class is used to represent the users configuration as saved in the database.
 *
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class Config {

    // Properties
    private int id;                           // Unique id for the config set
    private int watchListStyle = 1;           // The watch list style the user has chosen. 1: Poster, 2: List. Default is Poster
    private int saveLocation = 0;             // Whether the user has chosen to save their location, represented as an integer. Default as false (0)

    // Constructors
    public Config(){}

    public Config(int id, int watchListStyle, int saveLocation) {
        this.id = id;
        this.watchListStyle = watchListStyle;
        this.saveLocation = saveLocation;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWatchListStyle() {
        return watchListStyle;
    }

    public void setWatchListStyle(int watchListStyle) {
        this.watchListStyle = watchListStyle;
    }

    public int getSaveLocation() {
        return saveLocation;
    }

    public void setSaveLocation(int saveLocation) {
        this.saveLocation = saveLocation;
    }
}
