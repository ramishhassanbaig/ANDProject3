package com.example.ramish.popularmovies1;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Network {

    public static String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static String BASE_IMAGE_URL = " http://image.tmdb.org/t/p/w500";
    public static String API_KEY = "MY_API_KEY";
    public static String POPULAR_MOVIES = "popular";
    public static String TOP_RATED_MOVIES = "top_rated";

    private String jsonString = "";
    private List<Movie> movieList;
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

    public class ServiceTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return callService(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            jsonString = s;
            movieList = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray results = jsonObject.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieJson = (JSONObject) results.get(i);
                    movieList.add(parseJSONToMovie(movieJson));
                }

                listener.onSuccess((ArrayList<Movie>) movieList);
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

    private Movie parseJSONToMovie(JSONObject movieJson) {
        Movie movie = new Movie();
        try {
            movie.setTitle(movieJson.getString("title"));
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
        void onSuccess(ArrayList<Movie> movies);
    }

}
