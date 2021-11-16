package net.prasetyomuhdwi.movieapps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieHolder> {

    Context context;
    private final ArrayList<MoviesData> dataList;

    public HomeAdapter(ArrayList<MoviesData> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public HomeAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.MovieHolder holder, int position) {
        String url = "https://themoviedb.org/t/p/w500/"+dataList.get(position).getPoster_path();

        Glide.with(holder.itemView).load(url).into(holder.imgPoster);
        holder.tvTitle.setText(dataList.get(position).getTitle());
        holder.tvDesc.setText(dataList.get(position).getOverview().substring(0,66)+"...");
        holder.tvReleaseDate.setText(dataList.get(position).getReleaseDate());
        holder.tvRating.setText(dataList.get(position).getRating().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Item Selected", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MovieHolder extends RecyclerView.ViewHolder{
        ImageView imgPoster;
        TextView tvTitle,tvDesc,tvReleaseDate,tvRating;

        public MovieHolder(@NonNull View itemView){
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_movie_poster);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_movie_title);
            tvDesc = (TextView) itemView.findViewById(R.id.tv_movie_desc);
            tvReleaseDate = (TextView) itemView.findViewById(R.id.tv_movie_release_date);
            tvRating = (TextView) itemView.findViewById(R.id.tv_movie_rating);
        }
    }
}
