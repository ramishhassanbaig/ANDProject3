package com.example.ramish.popularmovies1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.ViewHolder> {


    private PageSelectedListener pageSelectedListener;
    private ArrayList<String> pagesArrayList;
    private Context context;

    public PagesAdapter(Context context) {
        this.context = context;
        this.pagesArrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pagesArrayList.add(String.valueOf(i+1));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pages_itelm_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.page.setText(pagesArrayList.get(position));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pageSelectedListener.onPageClicked(Integer.valueOf(pagesArrayList.get(position)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return pagesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView page;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            page = itemView.findViewById(R.id.page);
        }
    }

    public void setPageSelectedListener(@Nullable PageSelectedListener pageSelectedListener) {
        this.pageSelectedListener = pageSelectedListener;
    }

    public interface PageSelectedListener {
        void onPageClicked(Integer page);
    }
}

