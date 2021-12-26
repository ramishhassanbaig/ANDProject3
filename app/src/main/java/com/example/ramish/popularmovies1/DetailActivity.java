package com.example.ramish.popularmovies1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramish.popularmovies1.data.MovieRepository;
import com.google.android.material.tabs.TabLayout;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements Network.NetworkListener, TrailerAdapter.TrailerSelectedListener, ReviewAdapter.TrailerSelectedListener {

    ImageView poster, favorite;
    TextView title,releaseDate, rating, description,no_content_found;
    RecyclerView listRecyclerView;
    TabLayout tabLayout;

    Movie movie;
    Network network;
    private TrailerAdapter trailerAdapter;
    private TabLayout.Tab trailerTab, reviewTab;
    private ReviewAdapter reviewAdapter;
    private boolean isMovieMarkedAsFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        setTitle("Movie Detail");
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        poster = findViewById(R.id.poster_img);
        title = findViewById(R.id.title_tv);
        favorite = findViewById(R.id.favorite);
        releaseDate = findViewById(R.id.release_date);
        rating = findViewById(R.id.rating);
        description = findViewById(R.id.decription);
        tabLayout = findViewById(R.id.tabs);
        no_content_found = findViewById(R.id.no_content_found);
        listRecyclerView = findViewById(R.id.list_items);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        try {
            movie = (Movie) getIntent().getSerializableExtra("movie");

            GlideApp.with(this).asBitmap().load(new URL(movie.getPosterPath())).centerInside().into(poster);

            title.setText(movie.getTitle());
            releaseDate.setText("Release Date: "+ movie.getReleaseDate());
            rating.setText("Rating: " + movie.getVoteAverage() + "/10");
            description.setText(movie.getOverview());

            network = new Network();
            network.setListener(this);
            network.getMovieTrailersList(String.valueOf(movie.getId()));

            tabLayout.addTab(tabLayout.newTab().setText("Trailers"),true);
            tabLayout.addTab(tabLayout.newTab().setText("Reviews"));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0){
                        network.getMovieTrailersList(String.valueOf(movie.getId()));
                    }
                    else if (tab.getPosition() == 1){
                        network.getMovieReviewsList(String.valueOf(movie.getId()));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Wrong URL", Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        final MovieRepository movieRepository = new MovieRepository(getApplication());

        movieRepository.getMovie(movie).observe(DetailActivity.this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie savedMovie) {
                if(savedMovie!=null && savedMovie.getId().equals(movie.getId())){
                    isMovieMarkedAsFavorite = true;
                    favorite.setImageResource(R.drawable.star_sel);
                }
                else {
                    isMovieMarkedAsFavorite = false;
                    favorite.setImageResource(R.drawable.star_unsel);
                }
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isMovieMarkedAsFavorite)
                    movieRepository.deleteFavoriteMovie(movie);
                else
                    movieRepository.insertFavoriteMovie(movie);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return true;
    }

    @Override
    public void onSuccess(ArrayList<?> list) {
        if (list.size()>0) {
            if (list.get(0) instanceof MovieTrailer) {
                trailerAdapter = new TrailerAdapter(DetailActivity.this, (ArrayList<MovieTrailer>) list);
                trailerAdapter.setItemSelectedListener(DetailActivity.this);
                listRecyclerView.setAdapter(trailerAdapter);
            }
            else if (list.get(0) instanceof MovieReview){
                reviewAdapter = new ReviewAdapter(DetailActivity.this, (ArrayList<MovieReview>) list);
                reviewAdapter.setItemSelectedListener(DetailActivity.this);
                listRecyclerView.setAdapter(reviewAdapter);
            }
            no_content_found.setVisibility(View.INVISIBLE);
            listRecyclerView.setVisibility(View.VISIBLE);
        }
        else{
            no_content_found.setVisibility(View.VISIBLE);
            listRecyclerView.setVisibility(View.INVISIBLE);
            no_content_found.setText("No Result Found");
        }

    }

    @Override
    public void onItemClicked(MovieTrailer trailer) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer.getTrailerUrl()));
            startActivity(i);
        }
        catch (ActivityNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClicked(MovieReview review) {

    }
}