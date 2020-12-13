package com.example.ramish.popularmovies1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    ImageView poster;
    TextView title,releaseDate, rating, description;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        poster = findViewById(R.id.poster_img);
        title = findViewById(R.id.title_tv);
        releaseDate = findViewById(R.id.release_date);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.decription);
        try {
            movie = (Movie) getIntent().getSerializableExtra("movie");

            GlideApp.with(this).asBitmap().load(new URL(movie.getPosterPath())).centerInside().into(poster);

            title.setText(movie.getTitle());
            releaseDate.setText("Release Date: "+ movie.getReleaseDate());
            rating.setText("Rating: " + movie.getVoteAverage() + "/10");
            description.setText(movie.getOverview());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Wrong URL", Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }
}