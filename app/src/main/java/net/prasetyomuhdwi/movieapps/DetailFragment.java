package net.prasetyomuhdwi.movieapps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_RELEASE_DATE = "release date";
    private static final String ARG_POSTER_URL = "poster";
    private static final String ARG_BACKDROP_URL = "backdrop";
    private static final String ARG_RATING = "rating";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mDesc;
    private String mReleaseDate;
    private String mRating;
    private String mPosterUrl;
    private String mBackdropUrl;

    public DetailFragment() {
        // Required empty public constructor
    }
    public static DetailFragment newInstance(String title, String desc,String releaseDate,String rating,String posterUrl,String backdropUrl) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putString(ARG_RELEASE_DATE, releaseDate);
        args.putString(ARG_RATING, rating);
        args.putString(ARG_POSTER_URL, posterUrl);
        args.putString(ARG_BACKDROP_URL, backdropUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mDesc = getArguments().getString(ARG_DESC);
            mReleaseDate = getArguments().getString(ARG_RELEASE_DATE);
            mRating = getArguments().getString(ARG_RATING);
            mPosterUrl = getArguments().getString(ARG_POSTER_URL);
            mBackdropUrl = getArguments().getString(ARG_BACKDROP_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // TODO : set content here
        ImageView imgBackdrop = (ImageView) view.findViewById(R.id.img_detail_backdrop);
        Glide.with(view).load(mBackdropUrl).into(imgBackdrop);
        ImageView imgPoster = (ImageView) view.findViewById(R.id.img_detail_poster);
        Glide.with(view).load(mPosterUrl).into(imgPoster);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_detail_title);
        tvTitle.setText(mTitle);
        TextView tvDesc = (TextView) view.findViewById(R.id.tv_detail_desc);
        tvDesc.setText(mDesc);
//        TODO : make Date Readable DD MMMM YYYY
        TextView tvReleaseDate = (TextView) view.findViewById(R.id.tv_detail_release_date);
        tvReleaseDate.setText(mReleaseDate);
        TextView tvRating = (TextView) view.findViewById(R.id.tv_detail_rating);
        tvRating.setText(mRating);
        return view;
    }
}