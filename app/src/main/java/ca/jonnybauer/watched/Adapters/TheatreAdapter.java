package ca.jonnybauer.watched.Adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
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
import ca.jonnybauer.watched.Models.Theatre;
import ca.jonnybauer.watched.Pages.MoviePopUp;
import ca.jonnybauer.watched.R;
import ca.jonnybauer.watched.Tables.WatchListTable;

public class TheatreAdapter extends RecyclerView.Adapter<TheatreAdapter.TheatreViewHolder> {
    private ArrayList<Theatre> theatres;
    private Context context;
    private DBHelper dbHelper;


    public TheatreAdapter(ArrayList<Theatre> theatres, Context context) {
        this.theatres = theatres;
        this.context = context;
        this.dbHelper = new DBHelper(context);
    }


    @NonNull
    @Override
    public TheatreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.theatre_view, viewGroup, false);
        TheatreViewHolder theatreViewHolder = new TheatreViewHolder(view, context);
        return theatreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final TheatreViewHolder viewHolder, int i) {
        Theatre theatre = theatres.get(i);
        viewHolder.name.setText(theatre.getName());
        viewHolder.address.setText(theatre.getAddress());

        // Phone Intent
        viewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Map Intent
        viewHolder.directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Web Intent
        viewHolder.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


    }

    @Override
    public int getItemCount() {
        return theatres.size();
    }


    // Custom ViewHolder to be used to hold the List Item
    class TheatreViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView address;
        protected ImageView phone;
        protected ImageView directions;
        protected ImageView website;
        protected Context context;


        public TheatreViewHolder(View view, Context context) {
            super(view);
            this.name = view.findViewById(R.id.theatreName);
            this.address = view.findViewById(R.id.theatreAddress);
            this.phone = view.findViewById(R.id.theatrePhone);
            this.directions = view.findViewById(R.id.theatreDirections);
            this.website = view.findViewById(R.id.theatreWebsite);
            this.context = context;
        }

    }


    }
