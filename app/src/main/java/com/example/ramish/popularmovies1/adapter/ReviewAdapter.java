package com.example.ramish.popularmovies1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ramish.popularmovies1.model.MovieReview;
import com.example.ramish.popularmovies1.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {


    private TrailerSelectedListener itemSelectedListener;
    private ArrayList<MovieReview> ReviewArrayList;
    private Context context;

    public ReviewAdapter(Context context, ArrayList<MovieReview> ReviewArrayList) {
        this.context = context;
        this.ReviewArrayList = ReviewArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MovieReview review = ReviewArrayList.get(position);

        try {
            holder.rating.setText("Rating: " + String.valueOf(review.getAuthor_details().getRating()) + "/10");
            holder.content.setText(review.getContent());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            holder.date.setText("Date: "+dateFormat.format(review.getCreated_at()));


//        try {
//            GlideApp.with(context).asBitmap().load(new URL(trailer.getTrailerThumbnailUrl())).centerCrop().into(holder.thumbnail);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelectedListener.onItemClicked(review);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ReviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

//        ImageView thumbnail;
        TextView content, rating, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            thumbnail = itemView.findViewById(R.id.thumbnail);
            content = itemView.findViewById(R.id.content);
            rating = itemView.findViewById(R.id.rating);
            date = itemView.findViewById(R.id.date);


        }
    }

    public void setItemSelectedListener(@Nullable TrailerSelectedListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    public interface TrailerSelectedListener {
        void onItemClicked(MovieReview review);
    }
}
