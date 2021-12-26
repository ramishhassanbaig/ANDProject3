package com.example.ramish.popularmovies1.data;

import android.app.Application;

import com.example.ramish.popularmovies1.Movie;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;

public class MovieRepository {

    private LiveData<List<Movie>> moviesList;
//    private List<Movie> moviesList;
    private MovieDao movieDao;

    public MovieRepository(Application application){
        MovieDatabase database = MovieDatabase.getDatabase(application);
        movieDao = database.movieDao();
        moviesList = movieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getMoviesList() {
//    public List<Movie> getMoviesList() {
        return moviesList;
    }

    public void insertFavoriteMovie(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieDao.insertMovie(movie);
            }
        });
    }

    public LiveData<Movie> getMovie(final Movie movie){
        return movieDao.isMovieExist(movie.getId());
    }

    public void deleteFavoriteMovie(final Movie movie){
        MovieDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieDao.deleteMovie(movie.getId());
            }
        });
    }
}
