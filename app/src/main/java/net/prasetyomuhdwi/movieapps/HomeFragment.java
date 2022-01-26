package net.prasetyomuhdwi.movieapps;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements HomeAdapter.ItemClickListener {

    private HomeAdapter adapter;
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

        String local = String.valueOf(Locale.getDefault().toLanguageTag()); // en-EN
        String[] url = {"https://api.themoviedb.org/3/movie/popular?api_key=434f297aa1bc200c813ea38732f514dd&language="+local+"&page=1",
                "https://api.themoviedb.org/3/genre/movie/list?api_key=434f297aa1bc200c813ea38732f514dd&language="+local};

        new HomeTask().execute(url);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        BottomNavigationView bottomNavigation = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.nav_profile:
                    navController.navigate(R.id.action_homeFragment_to_profileFragment);
                    break;
                case R.id.nav_setting:
                    navController.navigate(R.id.action_homeFragment_to_settingFragment);
                    break;
                default:
                    return false;
            }

            return true;
        });
    }

    public void setData(String jsonMovieString, String jsonGenreString) {
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
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        adapter = new HomeAdapter(moviesArrayList,this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(MoviesData moviesData) {

        Bundle dataMovie = new Bundle();
        dataMovie.putString("title", moviesData.getTitle());
        dataMovie.putString("overview", moviesData.getOverview());
        dataMovie.putString("releaseDate", moviesData.getReleaseDate());
        dataMovie.putString("rating", String.valueOf(moviesData.getRating()));
        dataMovie.putString("poster_path", moviesData.getPoster_path());
        dataMovie.putString("backdrop_path", moviesData.getBackdrop_path());
        dataMovie.putStringArray("genres", moviesData.getGenres());

        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_homeFragment_to_detailFragment,dataMovie);

    }

    public void buildDataMovie(String moviesData,String genreData){
        setData(moviesData,genreData);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("newText1",query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("newText",newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private class HomeTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... url) {
            OkHttpClient client = new OkHttpClient();

            // Movies Data
            Request request = new Request.Builder().url(String.valueOf(url[0])).build();
            String moviesData = null;
            try (Response response = client.newCall(request).execute()) {
                moviesData = Objects.requireNonNull(response.body()).string();
            }catch (Exception e){e.printStackTrace();}

            // Data Genres List
            request = new Request.Builder().url(String.valueOf(url[1])).build();
            String genreData = null;
            try (Response response = client.newCall(request).execute()) {
                genreData = Objects.requireNonNull(response.body()).string();
            }catch (Exception e){e.printStackTrace();}

            if (moviesData != null && genreData != null){
                return new String[]{moviesData,genreData};
            }else{
                return new String[]{getResources().getString(R.string.on_failure_connect)};
            }
        }

        @Override
        protected void onPostExecute(String[] responseData) {
            super.onPostExecute(responseData);
            buildDataMovie(responseData[0],responseData[1]);
            new Handler().postDelayed(() -> initRecyclerView(requireView()),300);
        }
    }
}