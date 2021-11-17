package net.prasetyomuhdwi.movieapps;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieHolder> {

    private final ArrayList<MoviesData> mDataList;
    private final ItemClickListener mClickListener;

    public HomeAdapter(ArrayList<MoviesData> dataList,ItemClickListener clickListener) {
        this.mDataList = dataList;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public HomeAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.MovieHolder holder, @SuppressLint("RecyclerView") int position) {
        String url = mDataList.get(position).getPoster_path();

        String desc = mDataList.get(position).getOverview();
        String title = mDataList.get(position).getTitle();

        Glide.with(holder.itemView).load(url).into(holder.imgPoster);
        holder.tvTitle.setText(title);
        if (desc.length()>50 && title.length()<17) {
            holder.tvDesc.setText(String.format("%s...", desc.substring(0, 50)));
        }else if (desc.length()>50 && title.length()>17) {
            holder.tvDesc.setText(String.format("%s...", desc.substring(0, 50)));
            holder.tvTitle.setTextSize(14);
       }else if (desc.length()<50 && title.length()>17) {
            holder.tvDesc.setText(String.format("%s...", desc.substring(0, (desc.length() / 2))));
        }else{
            holder.tvTitle.setTextSize(18);
            holder.tvDesc.setText(String.format("%s...", desc));
        }
        holder.tvReleaseDate.setText(mDataList.get(position).getReleaseDate());
        holder.tvRating.setText(String.valueOf(mDataList.get(position).getRating()));
        holder.itemView.setOnClickListener(v -> mClickListener.onItemClick(mDataList.get(position)));
    }

    @Override
    public int getItemCount() {
        return (mDataList != null) ? mDataList.size() : 0;
    }

    public static class MovieHolder extends RecyclerView.ViewHolder{
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

    public interface ItemClickListener{
        void onItemClick(MoviesData moviesData);
    }
}
