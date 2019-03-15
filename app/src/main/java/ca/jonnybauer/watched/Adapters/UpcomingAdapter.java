package ca.jonnybauer.watched.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import ca.jonnybauer.watched.Helpers.DBHelper;
import ca.jonnybauer.watched.Models.Movie;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {
    private ArrayList<Movie> upcomingMovies;
    private Context context;
    private DBHelper dbHelper;


    public UpcomingAdapter(ArrayList<Movie> upcomingMovies, Context context) {
        this.upcomingMovies = upcomingMovies;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }


    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_layout, viewGroup, false);
        UpcomingViewHolder upcomingViewHolder = new UpcomingViewHolder(view, context);
        return upcomingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final UpcomingViewHolder viewHolder, int i) {
        final Movie movie = upcomingMovies.get(i);

        viewHolder.title.setText(movie.getTitle());

        Calendar date = Calendar.getInstance();
        date.setTime(movie.getReleaseDate());
//        String dateString = date.get(Calendar.MONTH) + "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR);
        String dateString = String.format("%1$tb %1$te, %1$tY", date);
//        String.format("Duke's Birthday: %1$tb %1$te, %1$tY", c)
        viewHolder.date.setText(dateString);

        viewHolder.plot.setText(movie.getPlot());

        // Change the add icon if it has been added already and hasn't been deleted
        Movie currentMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());
        if(currentMovie != null && currentMovie.getDeleted() == 0) {
            viewHolder.add.setImageResource(R.drawable.ic_add_circle_black_24dp);
        }
        
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Movie selectedMovie = WatchListTable.getInstance().getMovieWithTmdbID(dbHelper, movie.getTmdbID());
                if(selectedMovie != null) {
                    if(selectedMovie.getDeleted() == 1) {
                        selectedMovie.setDeleted(0);
                        viewHolder.add.setImageResource(R.drawable.ic_add_circle_black_24dp);
                        WatchListTable.getInstance().updateMovie(selectedMovie, dbHelper);
                    } else {
                        selectedMovie.setDeleted(1);
                        viewHolder.add.setImageResource(R.drawable.ic_add_black_24dp);
                        WatchListTable.getInstance().updateMovie(selectedMovie, dbHelper);
                    }
                } else {
                    WatchListTable.getInstance().addMovie(movie, dbHelper);
                    viewHolder.add.setImageResource(R.drawable.ic_add_circle_black_24dp);
                }
            }
        });

        viewHolder.calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return upcomingMovies.size();
    }


    // Custom ViewHolder to be used to hold the List Item
    class UpcomingViewHolder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView date;
        protected TextView plot;
        protected ImageView calendar;
        protected ImageView add;
        protected Context context;


        public UpcomingViewHolder(View view, Context context) {
            super(view);
            this.title = view.findViewById(R.id.upcomingTitle);
            this.date = view.findViewById(R.id.upcomingReleaseDate);
            this.plot = view.findViewById(R.id.upcomingPlot);
            this.calendar = view.findViewById(R.id.upcomingCalendar);
            this.add = view.findViewById(R.id.upcomingAddButton);
            this.context = context;
        }

    }


    }
