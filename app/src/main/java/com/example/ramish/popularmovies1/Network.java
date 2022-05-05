package com.example.ramish.popularmovies1;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.ramish.popularmovies1.model.AuthorDetails;
import com.example.ramish.popularmovies1.model.Movie;
import com.example.ramish.popularmovies1.model.MovieReview;
import com.example.ramish.popularmovies1.model.MovieTrailer;

public class Network {

    public static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static String BASE_IMAGE_URL = " http://image.tmdb.org/t/p/w500";
    public static String API_KEY = "MY_API_KEY";
    public static String POPULAR_MOVIES = "popular";
    public static String TOP_RATED_MOVIES = "top_rated";
    public static String TRAILERS = "/videos";
    public static String REVIEWS = "/reviews";

    private String jsonString = "";
    private ArrayList<Movie> movieList;
    private ArrayList<MovieTrailer> movieTrailersList;
    private ArrayList<MovieReview> movieReviewsList;
    private NetworkListener listener;

    public void setListener(NetworkListener listener) {
        this.listener = listener;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    private String getPopularMoviesUrl() {
        return (BASE_URL + POPULAR_MOVIES + "?api_key=" + API_KEY);
    }

    private String getTopRatedMoviesUrl() {
        return (BASE_URL + TOP_RATED_MOVIES + "?api_key=" + API_KEY);
    }

    private String getMovieTrailerUrl(String id){
        return  (BASE_URL + id + TRAILERS + "?api_key=" + API_KEY);
    }

    private String getMovieReviewsUrl(String id){
        return  (BASE_URL + id + REVIEWS + "?api_key=" + API_KEY);
    }

    public class ServiceTask extends AsyncTask<String, Void, String>{

        String networkUrl = "";
        @Override
        protected String doInBackground(String... strings) {
            networkUrl = strings[0];
            return callService(networkUrl);
        }

        @Override
        protected void onPostExecute(String s) {
            jsonString = s;
            movieList = new ArrayList<>();
            movieTrailersList = new ArrayList<>();
            movieReviewsList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieJson = (JSONObject) results.get(i);
                    if (networkUrl.contains(TRAILERS)){
                        movieTrailersList.add(parseJSONToMovieTrailer(movieJson));
                    }
                    else if (networkUrl.contains(REVIEWS)){
                        movieReviewsList.add(parseJSONToMovieReview(movieJson));
                    }
                    else {
                        movieList.add(parseJSONToMovie(movieJson));
                    }
                }

                if (networkUrl.contains(TRAILERS)){
                    listener.onSuccess(movieTrailersList);
                }
                else if (networkUrl.contains(REVIEWS)){
                    listener.onSuccess(movieReviewsList);
                }
                else {
                    listener.onSuccess(movieList);
                }

            }
            catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    private String callService(String serviceUrl){
        try {
            URL url = new URL(serviceUrl);

            String protocol = url.getProtocol();
            if (protocol.equals("http")) {


                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                con.setRequestProperty("Content-Type", "application/json");
                con.setReadTimeout(150000);
                con.setConnectTimeout(150000);
                con.connect();

                int resp = con.getResponseCode();

                if (resp == 200) {
//                        long elapsedTime = System.currentTimeMillis() - startTime;
                    long elapsedTime = System.currentTimeMillis();
//                        AppLog.d("resBeforeElaspedTime", String.valueOf(elapsedTime));
                    con.getInputStream();
                    String message = convertInputStreamToString(con.getInputStream());

                    return message;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return "error";
    }

    public List<Movie> getTopRatedMoviesList(){
        new ServiceTask().execute(getTopRatedMoviesUrl());
        return movieList;
    }

    public List<Movie> getPopularMoviesList(){
        new ServiceTask().execute(getPopularMoviesUrl());
        return movieList;
    }

    public List<MovieTrailer> getMovieTrailersList(String id){
        new ServiceTask().execute(getMovieTrailerUrl(id));
        return movieTrailersList;
    }

    public List<MovieTrailer> getMovieReviewsList(String id){
        new ServiceTask().execute(getMovieReviewsUrl(id));
        return movieTrailersList;
    }

    private Movie parseJSONToMovie(JSONObject movieJson) {
        Movie movie = new Movie();
        try {
            movie.setTitle(movieJson.getString("title"));
            movie.setId(movieJson.getInt("id"));
            movie.setOriginalTitle(movieJson.getString("original_title"));
            movie.setPopularity(movieJson.getDouble("popularity"));
            movie.setVoteCount(movieJson.getInt("vote_count"));
            movie.setVoteAverage(movieJson.getDouble("vote_average"));
            movie.setReleaseDate(movieJson.getString("release_date"));
            movie.setOverview(movieJson.getString("overview"));
            movie.setPosterPath(BASE_IMAGE_URL + movieJson.getString("poster_path"));
            movie.setBackdropPath(BASE_IMAGE_URL + movieJson.getString("backdrop_path"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return movie;
    }

    private MovieTrailer parseJSONToMovieTrailer(JSONObject movieTrailerJson) {
        MovieTrailer movieTrailer = new MovieTrailer();
        try {
            movieTrailer.setId(movieTrailerJson.getString("id"));
            movieTrailer.setName(movieTrailerJson.getString("name"));
            movieTrailer.setSite(movieTrailerJson.getString("site"));
            movieTrailer.setKey(movieTrailerJson.getString("key"));
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return movieTrailer;
    }

    private MovieReview parseJSONToMovieReview(JSONObject movieReivewJson) {
        MovieReview movieReview = new MovieReview();
        try {
            movieReview.setId(movieReivewJson.getString("id"));
            movieReview.setAuthor(movieReivewJson.getString("author"));

            AuthorDetails authorDetails = new AuthorDetails();
            JSONObject authorJson = movieReivewJson.getJSONObject("author_details");
            authorDetails.setUsername(authorJson.getString("username"));
            authorDetails.setRating(authorJson.getInt("rating"));

            movieReview.setAuthor_details(authorDetails);
            movieReview.setContent(movieReivewJson.getString("content"));

//            String dateStr = movieReivewJson.getString("created_at").split("T")[0];
            movieReview.setCreated_at(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(movieReivewJson.getString("created_at").replace("T"," ")));

        }
        catch (JSONException | ParseException e){
            e.printStackTrace();
        }
        return movieReview;
    }


    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result = result.concat(line);
        inputStream.close();
        return result;

    }

    public interface NetworkListener{
        void onSuccess(ArrayList<?> list);
    }

}
