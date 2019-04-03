package ca.jonnybauer.watched.Adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        final Theatre theatre = theatres.get(i);
        viewHolder.name.setText(theatre.getName());
        viewHolder.address.setText(theatre.getAddress());

        // Phone Intent
        viewHolder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Phone Clicked");

                // TODO: Request permissions properly

                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},2);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("tel:" + theatre.getPhone()));
                if(intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
                }

            }
        });

        // Map Intent
        viewHolder.directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Request permissions properly

//                Uri location = Uri.parse("geo:0,0?q=" + theatre.getLatitude() + "," + theatre.getLongitude() + "(" + theatre.getName() + ")");
                Uri location = Uri.parse("geo:0,0?q=" + theatre.getName());
                System.out.println(location);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(location);
                if(intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Web Intent
        viewHolder.website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri website = Uri.parse(theatre.getWebsite());
                System.out.println(website);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(website);
                if(intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "An error occurred", Toast.LENGTH_LONG).show();
                }
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
