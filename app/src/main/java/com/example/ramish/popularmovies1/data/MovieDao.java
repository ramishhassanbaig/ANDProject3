package com.example.ramish.popularmovies1.data;

import com.example.ramish.popularmovies1.model.Movie;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface MovieDao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Query("DELETE FROM MOVIE_TABLE")
    void deleteAll();

    @Query("SELECT * FROM movie_table ORDER BY id ASC")
//    List<Movie> getAllMovies();
    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM MOVIE_TABLE WHERE id= :id")
    void deleteMovie(Integer id);

    @Query("SELECT * FROM MOVIE_TABLE WHERE id= :id")
    LiveData<Movie> isMovieExist(Integer id);
}
