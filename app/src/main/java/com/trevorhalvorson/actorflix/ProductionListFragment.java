package com.trevorhalvorson.actorflix;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Trevor on 8/20/2015.
 */
public class ProductionListFragment extends Fragment {
    private static final String TAG = ProductionListFragment.class.getSimpleName();
    public static final String ENDPOINT = "http://netflixroulette.net";

    private String mQuery;
    private ArrayList<Production> mProductions = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProductionAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_production_list, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mQuery = getArguments().getString("query_string");
        findProductions(mQuery);

        return view;
    }

    private void findProductions(String query) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT).build();
        Api api = restAdapter.create(Api.class);
        api.getProductions(query, new Callback<ArrayList<Production>>() {
            @Override
            public void success(ArrayList<Production> productions, Response response) {
                Log.i(TAG, "API Response: " + response.getUrl());
                mProductions.clear();
                mProductions.addAll(productions);
                mAdapter = new ProductionAdapter(mProductions);
                mRecyclerView.setAdapter(mAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "API Response: " + error.getMessage() + " from " + error.getUrl());
                Snackbar.make(getView(), error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private class ProductionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Production mProduction;
        private TextView mTitleTextView;
        private TextView mCategoryTextView;
        private TextView mYearTextView;


        public ProductionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_title_text_view);
            mCategoryTextView = (TextView)
                    itemView.findViewById(R.id.list_item_category_text_view);
            mYearTextView = (TextView)
                    itemView.findViewById(R.id.list_item_year_text_view);
        }

        public void bindProduction(Production production) {
            mProduction = production;
            mTitleTextView.setText(mProduction.getShowTitle());
            mCategoryTextView.setText(mProduction.getCategory());
            mYearTextView.setText(mProduction.getReleaseYear());
        }

        @Override
        public void onClick(View v) {
            //TODO Launch Production Detail screen
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.netflix.com/title/" + mProduction.getShowId()));
            startActivity(intent);
        }
    }

    private class ProductionAdapter extends RecyclerView.Adapter<ProductionHolder> {
        private ArrayList<Production> mProductions;

        public ProductionAdapter(ArrayList<Production> productions) {
            mProductions = productions;

        }

        @Override
        public ProductionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater
                    .inflate(R.layout.list_item_production, parent, false);

            return new ProductionHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductionHolder holder, int position) {
            Production production = mProductions.get(position);
            holder.bindProduction(production);

        }

        @Override
        public int getItemCount() {
            return mProductions.size();
        }
    }
}
