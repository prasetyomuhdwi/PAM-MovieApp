package net.prasetyomuhdwi.movieapps;

import android.content.Context;
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

public class HomeFragment extends Fragment implements HomeAdapter.ItemClickListener {

    private ArrayList<MoviesData> moviesArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String url = "https://api.themoviedb.org/3/movie/550?api_key=434f297aa1bc200c813ea38732f514dd";

        buildDataMovie();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run() {
                initRecyclerView(view);
            }
        },300);
        return view;

    }

    public void setData(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray result = jsonObject.getJSONArray("results");
            moviesArrayList = new ArrayList<>();

            for(int i=0; i<result.length(); i++){
                JSONObject moviesObj = result.getJSONObject(i);
//                Log.d("Check This DATA", String.valueOf(moviesObj));
                MoviesData movies = new MoviesData(
                        moviesObj.getString("title"),
                        moviesObj.getString("overview"),
                        moviesObj.getString("release_date"),
                        "https://themoviedb.org/t/p/w500/" + moviesObj.getString("poster_path"),
                        "https://themoviedb.org/t/p/w500/" + moviesObj.getString("backdrop_path"),
                        moviesObj.getDouble("vote_average"));
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
                moviesData.getBackdrop_path()
                );

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.hide(requireActivity().getSupportFragmentManager().findFragmentByTag("home_fragment"));
        transaction.add(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void buildDataMovie(){
        String jsonData = "{\"page\":1,\"total_results\":10000,\"total_pages\":500,\"results\":"+
                "[{\"popularity\":2234.266,\"vote_count\":182,\"video\":false,\"poster_path\":\""+
                "/9HT9982bzgN5on1sLRmc1GMn6ZC.jpg\",\"id\":671039,\"adult\":false,\"backdrop_path\""+
                ":\"/gnf4Cb2rms69QbCnGFJyqwBWsxv.jpg\",\"original_language\":\"fr\",\"original_title\""+
                ":\"Bronx\",\"genre_ids\":[53,28,18,80],\"title\":\"Rogue City\",\"vote_average\""+
                ":6.1,\"overview\":\"Caught in the crosshairs of police corruption and Marseille’s"+
                " warring gangs, a loyal cop must protect his squad by taking matters into his own hands."+
                "\",\"release_date\":\"2020-10-30\"}]}";

        setData(jsonData);
    }
}