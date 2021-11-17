package net.prasetyomuhdwi.movieapps;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment implements HomeAdapter.ItemClickListener {

    private ArrayList<MoviesData> moviesArrayList;
    private static final String ARG_DATA_GENRE = "genre";
    private static final String ARG_DATA_MOVIE = "movie";

    private String mDataMovie;
    private String mDataGenre;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String[] responseData) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DATA_MOVIE, responseData[0]);
        args.putString(ARG_DATA_GENRE, responseData[1]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mDataMovie = getArguments().getString(ARG_DATA_MOVIE);
            mDataGenre = getArguments().getString(ARG_DATA_GENRE);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buildDataMovie(mDataMovie,mDataGenre);
        new Handler().postDelayed(() -> initRecyclerView(view),300);
        return view;

    }

    public void setData(String jsonMovieString,String jsonGenreString) {
        try {
            JSONObject genresObject = new JSONObject(jsonGenreString);
            JSONArray genresArr = genresObject.getJSONArray("genres");

            JSONObject moviesObject = new JSONObject(jsonMovieString);
            JSONArray result = moviesObject.getJSONArray("results");

            moviesArrayList = new ArrayList<>();
            for(int i=0; i<result.length(); i++){
                JSONObject moviesObj = result.getJSONObject(i);

                JSONArray movieGenreArray = moviesObj.getJSONArray("genre_ids");
                String[] movieGenres = new String[movieGenreArray.length()];

                // Data Genre in Movie
                for (int j=0; j<movieGenreArray.length();j++){

                    //Data Genres List
                    for(int x=0; x<genresArr.length(); x++){
                        JSONObject genresObj = genresArr.getJSONObject(x);
                        if(movieGenreArray.getInt(j) == genresObj.getInt("id")){
                            movieGenres[j] = genresObj.getString("name");
                        }
                    }
                }

                MoviesData movies = new MoviesData(
                        moviesObj.getString("title"),
                        moviesObj.getString("overview"),
                        moviesObj.getString("release_date"),
                        "https://themoviedb.org/t/p/w500/" + moviesObj.getString("poster_path"),
                        "https://themoviedb.org/t/p/w500/" + moviesObj.getString("backdrop_path"),
                        moviesObj.getDouble("vote_average"),
                        movieGenres
                );
                moviesArrayList.add(movies);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerview_home);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        HomeAdapter adapter = new HomeAdapter(moviesArrayList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(MoviesData moviesData) {
        Fragment fragment = DetailFragment.newInstance(
                moviesData.getTitle(),
                moviesData.getOverview(),
                moviesData.getReleaseDate(),
                String.valueOf(moviesData.getRating()),
                moviesData.getPoster_path(),
                moviesData.getBackdrop_path(),
                moviesData.getGenres()
        );

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(Objects.requireNonNull(requireActivity().getSupportFragmentManager().findFragmentByTag("home_fragment")));
        transaction.add(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void buildDataMovie(String moviesData,String genreData){
        setData(moviesData,genreData);
    }
}