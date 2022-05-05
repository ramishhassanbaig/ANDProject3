package com.example.ramish.popularmovies1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramish.popularmovies1.GlideApp;
import com.example.ramish.popularmovies1.model.MovieTrailer;
import com.example.ramish.popularmovies1.R;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {


    private TrailerSelectedListener itemSelectedListener;
    private ArrayList<MovieTrailer> trailerArrayList;
    private Context context;

    public TrailerAdapter(Context context, ArrayList<MovieTrailer> trailerArrayList) {
        this.context = context;
        this.trailerArrayList = trailerArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MovieTrailer trailer = trailerArrayList.get(position);

        holder.title_tv.setText(trailer.getName());

        try {
            GlideApp.with(context).asBitmap().load(new URL(trailer.getTrailerThumbnailUrl())).centerCrop().into(holder.thumbnail);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelectedListener.onItemClicked(trailer);
            }
        });

    }

    @Override
    public int getItemCount() {
        return trailerArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView thumbnail;
        TextView title_tv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title_tv = itemView.findViewById(R.id.trailer_name);


        }
    }

    public void setItemSelectedListener(@Nullable TrailerSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public interface TrailerSelectedListener {
        void onItemClicked(MovieTrailer trailer);
    }
}
