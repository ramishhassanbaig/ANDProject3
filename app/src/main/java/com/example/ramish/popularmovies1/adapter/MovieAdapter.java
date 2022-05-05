package com.example.ramish.popularmovies1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramish.popularmovies1.GlideApp;
import com.example.ramish.popularmovies1.model.Movie;
import com.example.ramish.popularmovies1.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {


    private ItemSelectedListener itemSelectedListener;
    private ArrayList<Movie> movieArrayList;
    private Context context;

    public MovieAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_recyclerview_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movieArrayList.get(position);

        holder.title_tv.setText(movie.getTitle());

        try {
            GlideApp.with(context).asBitmap().load(new URL(movie.getPosterPath())).centerCrop().into(holder.post_img);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelectedListener.onItemClicked(movie);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView post_img;
        TextView title_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_img = itemView.findViewById(R.id.poster_img);
            title_tv = itemView.findViewById(R.id.title_tv);


        }
    }

    public void setItemSelectedListener(@Nullable ItemSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public interface ItemSelectedListener {
        void onItemClicked(Movie movie);
    }
}
