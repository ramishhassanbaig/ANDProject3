package com.example.ramish.popularmovies1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ItemSelectedListener, PagesAdapter.PageSelectedListener {

    RecyclerView recyclerView, pagesRecyclerView;
    MovieAdapter movieAdapter;
    PagesAdapter pagesAdapter;
    Network network;

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
            public void onSuccess(ArrayList<Movie> movies) {
                movieAdapter = new MovieAdapter(MainActivity.this, movies);
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
        getMenuInflater().inflate(R.menu.sort_menu,menu);

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

                break;
            case R.id.top_rated:
                network.getTopRatedMoviesList();
                break;
        }
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