package ca.jonnybauer.watched.Models;

import java.util.Date;


/**
 *
 * This class is used to represent a movie that the user has saved into the database.
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class Movie {

    // Properties
    private int id;                 // Unique ID of the movie
    private String title;           // Title of the movie
    private Date release_date;      // Release date of the movie
    private int rating;             // Rating of the movie
    private String plot;            // Short plot of the movie
    private int favourite = 0;      // Whether or not the movie is marked as a favourite, represented as an integer. Default of false (0)
    private int watched = 0;        // Whether or not the movie is marked as watched, represented as an integer. Default of false (0)
    private int deleted = 0;        // Whether or not the movie has been deleted, represented as an integer. Default of false (0)
    private Date date_added;        // The date the user added the movie
    private Date last_updated;      // THe date the movie has been last updated

    // Constructors
    public Movie(){}

    public Movie(int id, String title, Date release_date, int rating, String plot, int favourite, int watched, int deleted, Date date_added, Date last_updated) {
        this.id = id;
        this.title = title;
        this.release_date = release_date;
        this.rating = rating;
        this.plot = plot;
        this.favourite = favourite;
        this.watched = watched;
        this.deleted = deleted;
        this.date_added = date_added;
        this.last_updated = last_updated;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public Date getDate_added() {
        return date_added;
    }

    public void setDate_added(Date date_added) {
        this.date_added = date_added;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Date last_updated) {
        this.last_updated = last_updated;
    }
}
