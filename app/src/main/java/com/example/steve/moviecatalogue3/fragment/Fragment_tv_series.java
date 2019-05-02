package com.example.steve.moviecatalogue3.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.steve.moviecatalogue3.R;
import com.example.steve.moviecatalogue3.activity.Detail_Activity;
import com.example.steve.moviecatalogue3.adapter.TvAdapter;
import com.example.steve.moviecatalogue3.entity.TvSeries;
import com.example.steve.moviecatalogue3.listener.ItemClickSupport;
import com.example.steve.moviecatalogue3.loader.MyAsyncTaskLoaderTv;

import java.util.ArrayList;

import static com.example.steve.moviecatalogue3.fragment.Fragment_movies.EXTRA_STRING;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_tv_series extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<TvSeries>> {
    ProgressBar progressBar;
    TvAdapter adapter;
    RecyclerView rvCategory;
    SearchView searchView;
    ArrayList<TvSeries> tvSeriesArrayList;
    TextView resultTextView;
    private String resultText;


    public Fragment_tv_series() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.option_menu, menu);
        menu.findItem(R.id.search).setIcon(ContextCompat.getDrawable(getActivity(), R.drawable.baseline_search_white_48dp));

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search_hint2));

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
        return inflater.inflate(R.layout.fragment_fragment_tv_series, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TvAdapter(getActivity());
        adapter.notifyDataSetChanged();
        progressBar = view.findViewById(R.id.progress_tv_series);
        progressBar.setVisibility(View.INVISIBLE);
        rvCategory = view.findViewById(R.id.recycle_tv);
        resultTextView = view.findViewById(R.id.topratedtvseries);
        rvCategory.setHasFixedSize(true);
        resultText = getResources().getString(R.string.top_rated_tv_series);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        LoaderManager.getInstance(this).initLoader(0, null, this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<TvSeries>> onCreateLoader(int i, @Nullable Bundle bundle) {
        progressBar.setVisibility(View.VISIBLE);
        rvCategory.setAdapter(null);
        if (bundle != null) {
            return new MyAsyncTaskLoaderTv(getActivity(), bundle);
        } else {
            return new MyAsyncTaskLoaderTv(getActivity(), null);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<TvSeries>> loader, final ArrayList<TvSeries> tvSeries) {
        adapter.setTvSeriesList(tvSeries);
        tvSeriesArrayList = tvSeries;
        resultTextView.setText(resultText);
        progressBar.setVisibility(View.INVISIBLE);
        rvCategory.setAdapter(adapter);
        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                TvSeries tvsSeries = adapter.getTvSeriesList().get(position);
                Intent detail = new Intent(getActivity(), Detail_Activity.class);
                detail.putExtra(Detail_Activity.extraMovies, tvsSeries);
                Detail_Activity.isMovie = false;
                startActivity(detail);
            }
        });
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<TvSeries>> loader) {
        adapter.setTvSeriesList(null);
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
