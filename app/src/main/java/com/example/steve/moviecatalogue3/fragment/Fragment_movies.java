package com.example.steve.moviecatalogue3.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.steve.moviecatalogue3.R;
import com.example.steve.moviecatalogue3.activity.Detail_Activity;
import com.example.steve.moviecatalogue3.adapter.MoviesAdapter;
import com.example.steve.moviecatalogue3.entity.Movie;
import com.example.steve.moviecatalogue3.listener.ItemClickSupport;
import com.example.steve.moviecatalogue3.loader.MyAsyncTaskLoader;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_movies extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {
    ProgressBar progressBar;
    MoviesAdapter adapter;
    RecyclerView rvCategory;
    public static final String EXTRA_STRING = "extra_string";
    public String resultText;
    TextView topRated;
    FragmentActivity fragmentActivity;
    ArrayList<Movie> movieArrayList;
    private MenuItem mSearchItem;
    private SearchView searchView;


    public Fragment_movies() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.search).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.baseline_search_white_48dp));

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.isEmpty()) {
                    LoadFilter(null);
                    searchView.clearFocus();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_STRING, s);
                    LoadFilter(bundle);
                    searchView.clearFocus();
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    LoadFilter(null);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString(EXTRA_STRING, s);
                    LoadFilter(bundle);
                }

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_fragment_movies, container, false);


    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        topRated = view.findViewById(R.id.topratemovies);
        adapter = new MoviesAdapter(getActivity());
        adapter.notifyDataSetChanged();
        resultText = getResources().getString(R.string.top_rated_movies);
        progressBar = view.findViewById(R.id.progress_movies);
        progressBar.setVisibility(View.INVISIBLE);
        topRated.setText(getResources().getString(R.string.top_rated_movies));
        rvCategory = view.findViewById(R.id.recycle_movies);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LoaderManager.getInstance(getActivity()).initLoader(0, null, this);


    }


    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        rvCategory.setAdapter(null);
        if (bundle != null) {
            return new MyAsyncTaskLoader(getActivity(), bundle);
        } else {
            return new MyAsyncTaskLoader(getActivity(), null);
        }

    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        adapter.setListMovies(movies);
        movieArrayList = movies;
        topRated.setText(resultText);
        progressBar.setVisibility(View.INVISIBLE);
        rvCategory.setAdapter(adapter);
        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Movie movie = adapter.getListMovies().get(position);
                Intent detail = new Intent(getActivity(), Detail_Activity.class);
                detail.putExtra(Detail_Activity.extraMovies, movie);
                Detail_Activity.isMovie = true;
                startActivity(detail);
            }
        });

    }


    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {
        adapter.setListMovies(null);

    }

    public void LoadFilter(@Nullable Bundle bundle) {
        if (bundle != null) {
            if (this.getActivity() != null) {
                LoaderManager.getInstance(this).restartLoader(0, bundle, this);
                resultText = getResources().getString(R.string.search_result) + bundle.getString(EXTRA_STRING);
            }

        } else {
            if (this.getActivity() != null) {
                LoaderManager.getInstance(this).restartLoader(0, null, this);
                resultText = getResources().getString(R.string.top_rated_movies);
            }
        }
    }
}
