package net.prasetyomuhdwi.movieapps;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment implements HomeAdapter.ItemClickListener {

    private ArrayList<MoviesData> moviesArrayList;
    private static final String ARG_RESPONSE_DATA = "data";

    private String mResponseData;

    public HomeFragment() {
        // Required empty public constructor
    }
//
//    public static HomeFragment newInstance() {
//        return new HomeFragment();
//    }

    public static HomeFragment newInstance(String responseData) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RESPONSE_DATA, responseData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mResponseData = getArguments().getString(ARG_RESPONSE_DATA);
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        buildDataMovie(mResponseData);
        new Handler().postDelayed(() -> initRecyclerView(view),300);
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
        transaction.hide(Objects.requireNonNull(requireActivity().getSupportFragmentManager().findFragmentByTag("home_fragment")));
        transaction.add(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void buildDataMovie(String responseData){
        setData(responseData);
    }
}