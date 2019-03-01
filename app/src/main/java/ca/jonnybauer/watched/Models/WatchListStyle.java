package ca.jonnybauer.watched.Models;


/**
 * This class is used to represent the config options for the Poster Style
 *
 * @author Jonathon Bauer
 * @version 1.0
 */
public enum WatchListStyle {

    // Values
    POSTER(1), LIST(2);

    // Properties
    private int value;

    // Constructor
    private WatchListStyle(int value) {
        this.value = value;
    }

    // Getter for the Value
    public int getValue() {
        return value;
    }


}
