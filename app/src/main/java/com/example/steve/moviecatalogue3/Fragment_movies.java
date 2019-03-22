package com.example.steve.moviecatalogue3;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_movies extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    ProgressBar progressBar;
    MoviesAdapter adapter;
    RecyclerView rvCategory;

    public Fragment_movies() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_movies, container, false);


    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view ,savedInstanceState);
        adapter = new MoviesAdapter(getActivity());
        adapter.notifyDataSetChanged();
        progressBar = view.findViewById(R.id.progress_movies);
        progressBar.setVisibility(View.INVISIBLE);
        rvCategory = view.findViewById(R.id.recycle_movies);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        LoaderManager.getInstance(this).initLoader(0,null,this);


    }


    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        return new MyAsyncTaskLoader(getActivity());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        adapter.setListMovies(movies);
        progressBar.setVisibility(View.INVISIBLE);
        rvCategory.setAdapter(adapter);
        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Movie movie = adapter.getListMovies().get(position);
                Intent detail = new Intent(getActivity(),Detail_Activity.class);
                detail.putExtra(Detail_Activity.extraMovies,movie);
                Detail_Activity.isMovie=true;
                startActivity(detail);
            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        adapter.setListMovies(null);
    }
}
