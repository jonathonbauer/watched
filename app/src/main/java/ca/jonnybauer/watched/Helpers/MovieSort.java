package ca.jonnybauer.watched.Helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ca.jonnybauer.watched.Models.Movie;

public class MovieSort {

    /**
     * This function is used to sort an array of Movie objects by their release date
     * @param movies The list of movies to be sorted.
     * @return An ArrayList of Movie objects sorted by their release date
     */
    public static ArrayList<Movie> sortByDate(ArrayList<Movie> movies, int order) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        if(order == 1) {
            Collections.sort(movies, new Comparator<Movie>(){
                public int compare(Movie movie2, Movie movie1) {
                    return movie1.getReleaseDate().compareTo(movie2.getReleaseDate());
                }
            });
        } else {
            Collections.sort(movies, new Comparator<Movie>(){
                public int compare(Movie movie1, Movie movie2) {
                    return movie1.getReleaseDate().compareTo(movie2.getReleaseDate());
                }
            });
        }

        return movies;

    }

    /**
     * This function is used to sort an array of Movie objects by their title
     * @param movies The list of movies to be sorted.
     * @return An ArrayList of Movie objects sorted by their title
     */
    public static ArrayList<Movie> sortByTitle(ArrayList<Movie> movies, int order) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        if(order == 1) {
            Collections.sort(movies, new Comparator<Movie>(){
                public int compare(Movie movie2, Movie movie1) {
                    return movie1.getTitle().compareTo(movie2.getTitle());
                }
            });
        } else {
            Collections.sort(movies, new Comparator<Movie>(){
                public int compare(Movie movie2, Movie movie1) {
                    return movie2.getTitle().compareTo(movie1.getTitle());
                }
            });
        }

        return movies;
    }

    /**
     * This function is used to sort an array of Movie objects by their date added
     * @param movies The list of movies to be sorted.
     * @return An ArrayList of Movie objects sorted by their date added
     */
    public static ArrayList<Movie> sortByDateAdded(ArrayList<Movie> movies, int order) {
        ArrayList<Movie> sortedMovies = new ArrayList<>();
        if(order == 1) {
            Collections.sort(movies, new Comparator<Movie>(){
                public int compare(Movie movie2, Movie movie1) {
                    return movie1.getDateAdded().compareTo(movie2.getDateAdded());
                }
            });
        } else {
            Collections.sort(movies, new Comparator<Movie>(){
                public int compare(Movie movie2, Movie movie1) {
                    return movie2.getDateAdded().compareTo(movie1.getDateAdded());
                }
            });
        }

        return movies;
    }





}
