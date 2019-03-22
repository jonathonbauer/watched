package ca.jonnybauer.watched.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.Models.WatchListStyle;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

public class WatchListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Movie> watchList;
    private Context context;
    private DBHelper dbHelper;
    private WatchListStyle style;
    private PosterViewHolder posterViewHolder;
    private ListViewHolder listViewHolder;


    public WatchListAdapter(ArrayList<Movie> watchList, Context context, WatchListStyle style) {
        this.watchList = watchList;
        this.context = context;
        this.dbHelper = new DBHelper(context);
        this.style = style;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(style == WatchListStyle.LIST) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.watch_list_text_view, viewGroup, false);
            listViewHolder = new ListViewHolder(view, context);
            return listViewHolder;
        } else {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.watch_list_poster_view, viewGroup, false);
            posterViewHolder = new PosterViewHolder(view, context);
            return posterViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        if(style == WatchListStyle.LIST) {
            final ListViewHolder listViewHolder = (ListViewHolder)viewHolder;
            // Get the current movie
            final Movie movie = watchList.get(listViewHolder.getAdapterPosition());
            Movie currentMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());


            // Set the title
            listViewHolder.title.setText(movie.getTitle());

            // Change the add icon if it has been added already and hasn't been deleted

            if(currentMovie != null && currentMovie.getWatched() == 1) {
                listViewHolder.watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
            } else {
                listViewHolder.watched.setImageResource(R.drawable.ic_check_black_24dp);
            }

            // Change the add icon if it has been added already and hasn't been deleted
            if(currentMovie != null && currentMovie.getFavourite() == 1) {
                listViewHolder.watched.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                listViewHolder.watched.setImageResource(R.drawable.ic_star_border_black_24dp);
            }

            // Event handlers

            // watched button event handler
            listViewHolder.watched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());
                    if(selectedMovie.getWatched() == 0) {
                        listViewHolder.watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        selectedMovie.setWatched(1);
                    } else {
                        listViewHolder.watched.setImageResource(R.drawable.ic_check_black_24dp);
                        selectedMovie.setWatched(0);
                    }
                    WatchListTable.getInstance().updateMovie(selectedMovie, dbHelper);
                }
            });

            // Favourite button event handler
            listViewHolder.watched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());
                    if(selectedMovie.getFavourite() == 0) {
                        listViewHolder.favourite.setImageResource(R.drawable.ic_star_black_24dp);
                        selectedMovie.setFavourite(1);
                    } else {
                        listViewHolder.favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                        selectedMovie.setFavourite(0);
                    }
                    WatchListTable.getInstance().updateMovie(selectedMovie, dbHelper);
                }
            });


        } else if(style == WatchListStyle.POSTER) {
            final PosterViewHolder posterViewHolder = (PosterViewHolder)viewHolder;
            // Get the current movie
            final Movie movie = watchList.get(posterViewHolder.getAdapterPosition());
            Movie currentMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());

            // Set the poster
            Picasso.get().load(currentMovie.getPosterPath()).placeholder(R.drawable.noimagefound).into(posterViewHolder.poster);

            // Change the add icon if it has been added already and hasn't been deleted

            if(currentMovie.getWatched() == 1) {
                posterViewHolder.watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
            } else {
                posterViewHolder.watched.setImageResource(R.drawable.ic_check_black_24dp);
            }

            // Change the add icon if it has been added already and hasn't been deleted
            if(currentMovie.getFavourite() == 1) {
                posterViewHolder.favourite.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                posterViewHolder.favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
            }

            // Event handlers

            // watched button event handler
            posterViewHolder.watched.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());
                    if(selectedMovie.getWatched() == 0) {
                        posterViewHolder.watched.setImageResource(R.drawable.ic_check_circle_black_24dp);
                        selectedMovie.setWatched(1);
                    } else {
                        posterViewHolder.watched.setImageResource(R.drawable.ic_check_black_24dp);
                        selectedMovie.setWatched(0);
                    }
                    WatchListTable.getInstance().updateMovie(selectedMovie, dbHelper);
                    System.out.println("Watched button clicked on: " + selectedMovie.getTitle() + " ID: " + viewHolder.getAdapterPosition());
                }
            });

            // Favourite button event handler
            posterViewHolder.favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie selectedMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());
                    if(selectedMovie.getFavourite() == 0) {
                        posterViewHolder.favourite.setImageResource(R.drawable.ic_star_black_24dp);
                        selectedMovie.setFavourite(1);
                    } else {
                        posterViewHolder.favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                        selectedMovie.setFavourite(0);
                    }
                    WatchListTable.getInstance().updateMovie(selectedMovie, dbHelper);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }


    // Custom ViewHolder to be used to hold the Poster Layout
    class PosterViewHolder extends RecyclerView.ViewHolder {
        protected ImageView poster;
        protected ImageView favourite;
        protected ImageView watched;
        protected Context context;


        public PosterViewHolder(View view, Context context) {
            super(view);
            this.poster = view.findViewById(R.id.watchListPoster);
            this.favourite = view.findViewById(R.id.watchListPosterFavorite);
            this.watched = view.findViewById(R.id.watchListPosterWatched);
            this.context = context;
        }

    }


    // Custom ViewHolder to be used to hold the List Layout
    class ListViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected ImageView favourite;
        protected ImageView watched;
        protected Context context;


        public ListViewHolder(View view, Context context) {
            super(view);
            this.title = view.findViewById(R.id.watchListTextTitle);
            this.favourite = view.findViewById(R.id.watchListTextFavourite);
            this.watched = view.findViewById(R.id.watchListTextWatched);
            this.context = context;
        }

    }


    }
