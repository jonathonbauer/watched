package ca.jonnybauer.watched.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;


/**
 *
 * This class is used to represent a movie that the user has saved into the database.
 * @author Jonathon Bauer
 * @version 1.0
 *
 */
public class Movie implements Parcelable {

    // Properties
    private int id = 0;                          // Unique ID of the movie
    private int tmdbID;                          // Unique TheMovieDB ID of the movie
    private String title;                        // Title of the movie
    private String posterPath;                   // Path to the movie poster
    private ArrayList<String> credits = null;    // Credits for the movie
    private Date releaseDate;                    // Release date of the movie
    private double rating;                       // Rating of the movie
    private String plot;                         // Short plot of the movie
    private int favourite = 0;                   // Whether or not the movie is marked as a favourite, represented as an integer. Default of false (0)
    private int watched = 0;                     // Whether or not the movie is marked as watched, represented as an integer. Default of false (0)
    private int deleted = 0;                     // Whether or not the movie has been deleted, represented as an integer. Default of false (0)
    private Date dateAdded = new Date();         // The date the user added the movie
    private Date lastUpdated = new Date();       // THe date the movie has been last updated

    // Constructors
    public Movie(){}

    public Movie(int id, int tmdbID, String title, String posterPath, ArrayList<String> credits,  Date releaseDate, double rating,
                 String plot, int favourite, int watched, int deleted, Date dateAdded, Date lastUpdated) {
        this.id = id;
        this.tmdbID = tmdbID;
        this.title = title;
        this.posterPath = posterPath;
        this.credits = credits;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.plot = plot;
        this.favourite = favourite;
        this.watched = watched;
        this.deleted = deleted;
        this.dateAdded = dateAdded;
        this.lastUpdated = lastUpdated;
    }

    public Movie(int tmdbID, String title, String posterPath, Date releaseDate, double rating, String plot) {
        this.tmdbID = tmdbID;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.plot = plot;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(tmdbID);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeStringList(credits);
        dest.writeLong(releaseDate.getTime());
        dest.writeDouble(rating);
        dest.writeInt(favourite);
        dest.writeInt(watched);
        dest.writeInt(deleted);
        dest.writeLong(dateAdded.getTime());
        dest.writeLong(lastUpdated.getTime());
    }


// Getters and Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTmdbID() {
        return tmdbID;
    }

    public void setTmdbID(int tmdbID) {
        this.tmdbID = tmdbID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public ArrayList<String> getCredits() {
        return credits;
    }

    public void setCredits(ArrayList<String> credits) {
        this.credits = credits;
    }

    public String getTopBilling(){
        ArrayList<String> creditsStrings = new ArrayList<>();
        for(int i=0; i < 3; i++) {
            if(credits.size() > i) {
                if(i == 2) {
                    creditsStrings.add(this.credits.get(i));
                } else {
                    creditsStrings.add(this.credits.get(i) + ", ");
                }
            }
        }
        String creditsString = "";
        for(int i=0; i < creditsStrings.size(); i++) {
            creditsString += creditsStrings.get(i);
        }
        return creditsString;

    }


    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
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

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
