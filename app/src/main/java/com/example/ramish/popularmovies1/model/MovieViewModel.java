package com.example.ramish.popularmovies1.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.ramish.popularmovies1.R;
import com.example.ramish.popularmovies1.data.MovieRepository;
import com.example.ramish.popularmovies1.ui.DetailActivity;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(getApplication());
    }

    public LiveData<Movie> getSelectedMovie(Movie movie){
        return movieRepository.getMovie(movie);
    }

    public void markFavoriteMovie(Movie movie){
        movieRepository.insertFavoriteMovie(movie);
    }

    public void unmarkFavoriteMovie(Movie movie){
        movieRepository.deleteFavoriteMovie(movie);
    }
}
