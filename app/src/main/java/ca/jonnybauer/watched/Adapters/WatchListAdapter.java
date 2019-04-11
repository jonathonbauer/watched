package ca.jonnybauer.watched.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.Models.WatchListStyle;
import ca.jonnybauer.watched.Pages.MoviePopUp;
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

    public void setWatchList(ArrayList<Movie> watchList) {
        this.watchList = watchList;
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

            // Change the favourite icon if it has been added already and hasn't been deleted
            if(currentMovie != null && currentMovie.getFavourite() == 1) {
                listViewHolder.favourite.setImageResource(R.drawable.ic_star_black_24dp);
            } else {
                listViewHolder.favourite.setImageResource(R.drawable.ic_star_border_black_24dp);
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
            listViewHolder.favourite.setOnClickListener(new View.OnClickListener() {
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

            // viewholder event handler

            listViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.move_in, R.anim.move_out, R.anim.move_back_in, R.anim.move_back_out)
                            .replace(R.id.main_content, MoviePopUp.newInstance(movie), "Movie Pop Up")
                            .addToBackStack(null).commit();
                }
            });



        } else if(style == WatchListStyle.POSTER) {
            final PosterViewHolder posterViewHolder = (PosterViewHolder)viewHolder;
            // Get the current movie
            final Movie movie = watchList.get(posterViewHolder.getAdapterPosition());
            Movie currentMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());

            // Set the poster
            Picasso.get().load(currentMovie.getPosterPath()).placeholder(R.drawable.noimagefound).into(posterViewHolder.poster);


            posterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.move_in, R.anim.move_out, R.anim.move_back_in, R.anim.move_back_out)
                            .replace(R.id.main_content, MoviePopUp.newInstance(movie), "Movie Pop Up")
                            .addToBackStack(null).commit();
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
        protected Context context;


        public PosterViewHolder(View view, Context context) {
            super(view);
            this.poster = view.findViewById(R.id.watchListPoster);
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
