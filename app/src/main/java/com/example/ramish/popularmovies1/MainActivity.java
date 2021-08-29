package com.example.ramish.popularmovies1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemSelectedListener, PagesAdapter.PageSelectedListener {

    RecyclerView recyclerView, pagesRecyclerView;
    MovieAdapter movieAdapter;
    PagesAdapter pagesAdapter;
    Network network;
    private boolean isPopular = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        pagesRecyclerView = findViewById(R.id.pages);

        network = new Network();

        pagesRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this,LinearLayoutManager.HORIZONTAL,false));

        recyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
        network.getPopularMoviesList();

        network.setListener(new Network.NetworkListener() {
            @Override
            public void onSuccess(ArrayList<?> movies) {
                movieAdapter = new MovieAdapter(MainActivity.this, (ArrayList<Movie>) movies);
                movieAdapter.setItemSelectedListener(MainActivity.this);
                recyclerView.setAdapter(movieAdapter);

                pagesAdapter = new PagesAdapter(MainActivity.this);
                pagesAdapter.setPageSelectedListener(MainActivity.this);
                pagesRecyclerView.setAdapter(pagesAdapter);
            }
        });






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.sort_menu,menu);

        try {
            MenuItem itemPopular = menu.findItem(R.id.popularity);
            MenuItem itemTopRated = menu.findItem(R.id.top_rated);
            if (isPopular) {
                itemPopular.setVisible(false);
                itemTopRated.setVisible(true);
            } else {
                itemPopular.setVisible(true);
                itemTopRated.setVisible(false);
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
                break;
            case R.id.top_rated:
                network.getTopRatedMoviesList();
                isPopular = false;
                break;
        }
        invalidateOptionsMenu();
        return true;
    }

    @Override
    public void onItemClicked(Movie movie) {
        Intent i = new Intent(MainActivity.this,DetailActivity.class);
        i.putExtra("movie",movie);
        startActivity(i);
    }

    @Override
    public void onPageClicked(Integer page) {

    }
}