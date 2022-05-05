package com.example.ramish.popularmovies1.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.ramish.popularmovies1.model.Movie;
import com.example.ramish.popularmovies1.adapter.MovieAdapter;
import com.example.ramish.popularmovies1.Network;
import com.example.ramish.popularmovies1.adapter.PagesAdapter;
import com.example.ramish.popularmovies1.R;
import com.example.ramish.popularmovies1.data.MovieRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemSelectedListener, PagesAdapter.PageSelectedListener {

    private static final String LOCAL_MOVIES_LIST = "moviesList";
    RecyclerView recyclerView, pagesRecyclerView;
    MovieAdapter movieAdapter;
    PagesAdapter pagesAdapter;
    Network network;
    private boolean isPopular = true;
    private boolean isFavorite = false;
    ArrayList<Movie> localMoviesList;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (localMoviesList!=null && localMoviesList.size()>0){
            outState.putSerializable(LOCAL_MOVIES_LIST,localMoviesList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState!=null){
            localMoviesList = (ArrayList<Movie>) savedInstanceState.getSerializable(LOCAL_MOVIES_LIST);
        }

        recyclerView = findViewById(R.id.recyclerview);
        pagesRecyclerView = findViewById(R.id.pages);

        network = new Network();

        pagesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));

        recyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));

        if (localMoviesList!=null && localMoviesList.size()>0){
            movieAdapter = new MovieAdapter(MainActivity.this, (ArrayList<Movie>) localMoviesList);
            movieAdapter.setItemSelectedListener(MainActivity.this);
            recyclerView.setAdapter(movieAdapter);
        }
        else {
            network.getPopularMoviesList();

            network.setListener(new Network.NetworkListener() {
                @Override
                public void onSuccess(ArrayList<?> movies) {
                    try {
                        localMoviesList = (ArrayList<Movie>) movies;
                        movieAdapter = new MovieAdapter(MainActivity.this, (ArrayList<Movie>) movies);
                        movieAdapter.setItemSelectedListener(MainActivity.this);
                        recyclerView.setAdapter(movieAdapter);

                        pagesAdapter = new PagesAdapter(MainActivity.this);
                        pagesAdapter.setPageSelectedListener(MainActivity.this);
                        pagesRecyclerView.setAdapter(pagesAdapter);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu,menu);

        try {
            MenuItem itemPopular = menu.findItem(R.id.popularity);
            MenuItem itemTopRated = menu.findItem(R.id.top_rated);
            MenuItem itemFavorite = menu.findItem(R.id.favorite);
            if (isPopular) {
                itemPopular.setVisible(false);
                itemTopRated.setVisible(true);
                itemFavorite.setVisible(true);
            }
            else if (isFavorite){
                itemPopular.setVisible(true);
                itemTopRated.setVisible(true);
                itemFavorite.setVisible(false);
            }
            else {
                itemPopular.setVisible(true);
                itemTopRated.setVisible(false);
                itemFavorite.setVisible(true);
            }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.popularity:
                network.getPopularMoviesList();
                isPopular = true;
                isFavorite = false;
                break;
            case R.id.top_rated:
                network.getTopRatedMoviesList();
                isPopular = false;
                isFavorite = false;
                break;
            case R.id.favorite:
                getFavoriteMoviesList();
                isPopular = false;
                isFavorite = true;
                break;
        }
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public void onItemClicked(Movie movie) {
        Intent i = new Intent(MainActivity.this, DetailActivity.class);
        i.putExtra("movie",movie);
        startActivity(i);
    }

    @Override
    public void onPageClicked(Integer page) {

    }

    private ArrayList<Movie> getFavoriteMoviesList(){

        try {
            LiveData<List<Movie>> savedMovies;
            final ArrayList<Movie> favoriteMovies = new ArrayList<>();
            MovieRepository movieRepository = new MovieRepository(getApplication());
            savedMovies = movieRepository.getMoviesList();

//            favoriteMovies = new ArrayList<>(savedMovies.getValue());
//            favoriteMovies = new ArrayList<>(savedMovies);

            savedMovies.observe(MainActivity.this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(List<Movie> savedMovies) {
                    if (isFavorite) {
                        favoriteMovies.clear();
                        favoriteMovies.addAll(savedMovies);
                        movieAdapter = new MovieAdapter(MainActivity.this, favoriteMovies);
                        movieAdapter.setItemSelectedListener(MainActivity.this);
                        recyclerView.setAdapter(movieAdapter);

                        pagesAdapter = new PagesAdapter(MainActivity.this);
                        pagesAdapter.setPageSelectedListener(MainActivity.this);
                        pagesRecyclerView.setAdapter(pagesAdapter);
                    }
                }
            });



        return favoriteMovies;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}