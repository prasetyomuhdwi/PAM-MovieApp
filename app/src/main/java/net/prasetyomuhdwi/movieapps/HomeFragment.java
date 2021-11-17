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

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

//    public static HomeFragment newInstance(String responseData) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_RESPONSE_DATA, responseData);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mResponseData = getArguments().getString(ARG_RESPONSE_DATA);
//        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        String url = "https://api.themoviedb.org/3/movie/popular?"+
                "api_key=434f297aa1bc200c813ea38732f514dd&language=en-US&page=1";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(view.getContext(),getResources().getString(R.string.on_failure_connect),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseData = Objects.requireNonNull(response.body()).string();
            }
        });
//        Log.d("Check This Out", "buildDataMovie: "+responseData);

        buildDataMovie();
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