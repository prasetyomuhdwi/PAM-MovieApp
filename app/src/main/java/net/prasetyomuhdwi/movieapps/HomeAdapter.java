package net.prasetyomuhdwi.movieapps;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MovieHolder> implements Filterable {

    private ArrayList<MoviesData> mDataList;
    private ArrayList<MoviesData> mDataFullList;
    private final ItemClickListener mClickListener;

    public HomeAdapter(ArrayList<MoviesData> dataList,ItemClickListener clickListener) {
        this.mDataList = dataList;
        this.mClickListener = clickListener;
        mDataFullList = new ArrayList<>(dataList);
    }

    @NonNull
    @Override
    public HomeAdapter.MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_card, parent, false);
        return new MovieHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, @SuppressLint("RecyclerView") int position) {
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

        SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd"); //2021-Nov-02
        String mDate = mDataList.get(position).getReleaseDate();
        String releaseDate = null;
        try {
            Date dateObj = DateFor.parse(mDate);
            DateFor = new SimpleDateFormat("dd MMMM yyyy");
            releaseDate = DateFor.format(Objects.requireNonNull(dateObj));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDate = ((releaseDate!=null) ? releaseDate : mDate);
        holder.tvReleaseDate.setText(mDate);

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
            imgPoster = itemView.findViewById(R.id.img_movie_poster);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvDesc = itemView.findViewById(R.id.tv_movie_desc);
            tvReleaseDate = itemView.findViewById(R.id.tv_movie_release_date);
            tvRating = itemView.findViewById(R.id.tv_movie_rating);
        }
    }

    @Override
    public Filter getFilter() {
        return Searched_Filter;
    }

    private Filter Searched_Filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<MoviesData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mDataFullList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MoviesData item : mDataFullList) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataList.clear();
            mDataList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public interface ItemClickListener{
        void onItemClick(MoviesData moviesData);
    }
}
