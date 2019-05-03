package com.example.steve.moviecatalogue3.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.steve.moviecatalogue3.R;
import com.example.steve.moviecatalogue3.activity.Detail_Activity;
import com.example.steve.moviecatalogue3.adapter.FavAdapter;
import com.example.steve.moviecatalogue3.db.FavHelper;
import com.example.steve.moviecatalogue3.entity.Favourite;
import com.example.steve.moviecatalogue3.entity.Movie;
import com.example.steve.moviecatalogue3.entity.TvSeries;
import com.example.steve.moviecatalogue3.listener.ItemClickSupport;
import com.example.steve.moviecatalogue3.loader.LoadFavCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.steve.moviecatalogue3.db.DatabaseContract.FavouriteColumn.CONTENT_URI;
import static com.example.steve.moviecatalogue3.helper.MappingHelper.mapCursorToArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Favourite_Film extends Fragment implements LoadFavCallback {
    private static final String EXTRA_STATE = "EXTRA_STATE";
    private static HandlerThread handlerThread;
    FavAdapter adapter;
    RecyclerView rvCategory;
    Favourite fav;
    FavHelper favHelper;
    private DataObserver myObserver;


    public Favourite_Film() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_favourite__film, container, false);

    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, this.getContext());
        getContext().getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        favHelper = FavHelper.getInstance(getActivity());
        adapter = new FavAdapter(getActivity());
        adapter.notifyDataSetChanged();
        rvCategory = view.findViewById(R.id.recycle_fav);
        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (savedInstanceState == null) {
            new LoadFavAsync(getContext(), this).execute();
        } else {
            ArrayList<Favourite> list = savedInstanceState.getParcelableArrayList(EXTRA_STATE);
            if (list != null) {
                adapter.setFavList(list);
            }

        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getFavList());
    }

    @Override
    public void preExecute() {

    }

    @Override
    public void PostExecute(Cursor favourites) {
        ArrayList<Favourite> listFav = mapCursorToArrayList(favourites);
        if (listFav.size() > 0) {
            adapter.setFavList(listFav);
        } else {
            adapter.setFavList(new ArrayList<Favourite>());
            Snackbar.make(rvCategory, "List tidak ada ", Snackbar.LENGTH_LONG).show();
        }
        adapter.setFavList(listFav);
        rvCategory.setAdapter(adapter);
        ItemClickSupport.addTo(rvCategory).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (adapter.getFavList().get(position).getIsMovie() == 1) {
                    Movie movie = new Movie();
                    movie.setId(adapter.getFavList().get(position).getId());
                    movie.setOverview(adapter.getFavList().get(position).getOverview());
                    movie.setPath(adapter.getFavList().get(position).getPath());
                    movie.setPopularity(adapter.getFavList().get(position).getPopularity());
                    movie.setRelease_date(adapter.getFavList().get(position).getRelease_date());
                    movie.setTitle(adapter.getFavList().get(position).getTitle());
                    movie.setVote_average(adapter.getFavList().get(position).getVote_average());
                    movie.setVote_count(adapter.getFavList().get(position).getVote_count());
                    Intent detail = new Intent(getActivity(), Detail_Activity.class);
                    detail.putExtra(Detail_Activity.extraMovies, movie);
                    Detail_Activity.isMovie = true;
                    startActivity(detail);

                } else {
                    TvSeries tvSeries = new TvSeries();
                    tvSeries.setId(adapter.getFavList().get(position).getId());
                    tvSeries.setOverview(adapter.getFavList().get(position).getOverview());
                    tvSeries.setPath(adapter.getFavList().get(position).getPath());
                    tvSeries.setPopularity(adapter.getFavList().get(position).getPopularity());
                    tvSeries.setRelease_date(adapter.getFavList().get(position).getRelease_date());
                    tvSeries.setTitle(adapter.getFavList().get(position).getTitle());
                    tvSeries.setVote_average(adapter.getFavList().get(position).getVote_average());
                    tvSeries.setVote_count(adapter.getFavList().get(position).getVote_count());
                    Intent detail = new Intent(getActivity(), Detail_Activity.class);
                    detail.putExtra(Detail_Activity.extraMovies, tvSeries);
                    Detail_Activity.isMovie = false;
                    startActivity(detail);

                }


            }
        });

    }

    @Override
    public void postExecute(ArrayList<Favourite> favourites) {

    }

    public void onResume() {
        super.onResume();
        new LoadFavAsync(getContext(), this).execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.subscribe_menu, menu);


    }

    private static class LoadFavAsync extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadFavCallback> weakCallback;

        private LoadFavAsync(Context context, LoadFavCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }


        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor favourites) {
            super.onPostExecute(favourites);
            weakCallback.get().PostExecute(favourites);

        }
    }

    public static class DataObserver extends ContentObserver {
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

    }
}

