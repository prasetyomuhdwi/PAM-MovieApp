package net.prasetyomuhdwi.movieapps;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class DetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_RELEASE_DATE = "release date";
    private static final String ARG_POSTER_URL = "poster";
    private static final String ARG_BACKDROP_URL = "backdrop";
    private static final String ARG_RATING = "rating";
    private static final String[] ARG_GENRES = new String[]{};

    private String mTitle;
    private String mDesc;
    private String mReleaseDate;
    private String mRating;
    private String mPosterUrl;
    private String mBackdropUrl;
    private String[] mGenres;

    public DetailFragment() {
        // Required empty public constructor
    }
    public static DetailFragment newInstance(
            String title,
            String desc,
            String releaseDate,
            String rating,
            String posterUrl,
            String backdropUrl,
            String[] genres
    ) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putString(ARG_RELEASE_DATE, releaseDate);
        args.putString(ARG_RATING, rating);
        args.putString(ARG_POSTER_URL, posterUrl);
        args.putString(ARG_BACKDROP_URL, backdropUrl);
        args.putStringArray(Arrays.toString(ARG_GENRES), genres);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString("title");
            mDesc = getArguments().getString("overview");
            mReleaseDate = getArguments().getString("releaseDate");
            mRating = getArguments().getString("rating");
            mPosterUrl = getArguments().getString("poster_path");
            mBackdropUrl = getArguments().getString("backdrop_path");
            mGenres = getArguments().getStringArray("genres");
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imgBackdrop = (ImageView) view.findViewById(R.id.img_detail_backdrop);
        Glide.with(view).load(mBackdropUrl).into(imgBackdrop);
        ImageView imgPoster = (ImageView) view.findViewById(R.id.img_detail_poster);
        Glide.with(view).load(mPosterUrl).into(imgPoster);

        String genres = TextUtils.join(",", mGenres);
        TextView tvGenres = (TextView) view.findViewById(R.id.tv_detail_genres);
        tvGenres.setText(genres);

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_detail_title);
        tvTitle.setText(mTitle);
        TextView tvDesc = (TextView) view.findViewById(R.id.tv_detail_desc);
        tvDesc.setText(mDesc);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat DateFor = new SimpleDateFormat("yyyy-MM-dd");
        String mDate = mReleaseDate;
        String releaseDate = null;
        try {
            Date dateObj = DateFor.parse(mDate);
            DateFor = new SimpleDateFormat("dd MMMM yyyy");
            releaseDate= DateFor.format(Objects.requireNonNull(dateObj));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mDate = ((releaseDate!=null) ? releaseDate : mDate);

        TextView tvReleaseDate = (TextView) view.findViewById(R.id.tv_detail_release_date);
        tvReleaseDate.setText(mDate);
        TextView tvRating = (TextView) view.findViewById(R.id.tv_detail_rating);
        tvRating.setText(mRating);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(view);

        BottomNavigationView bottomNavigation = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    navController.navigate(R.id.action_detailFragment_to_homeFragment);
                    break;
                case R.id.nav_setting:
                    navController.navigate(R.id.action_detailFragment_to_settingFragment);
                    break;
                case R.id.nav_profile:
                    navController.navigate(R.id.action_detailFragment_to_profileFragment);
                    break;
                default:
                    return false;
            }

            return true;
        });
    }
}