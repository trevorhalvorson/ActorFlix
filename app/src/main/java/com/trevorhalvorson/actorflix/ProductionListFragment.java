package com.trevorhalvorson.actorflix;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
    private static final String DIALOG_DETAIL = "com.trevorhalvorson.ProductionDetailFragment";

    private String mQuery;
    private ArrayList<Production> mProductions = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ProductionAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_production_list, container, false);
        mAdapter = new ProductionAdapter(mProductions);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_list_recycler_view);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mQuery = getArguments().getString("query_string");
        if (savedInstanceState == null) {
            findProductions(mQuery);
        }

        return view;
    }

    private void findProductions(String query) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT).build();
        FlixAPI api = restAdapter.create(FlixAPI.class);
        api.getProductions(query, new Callback<ArrayList<Production>>() {
            @Override
            public void success(ArrayList<Production> productions, Response response) {
                Log.i(TAG, "FlixAPI Response: " + response.getUrl());
                mProductions.clear();
                mProductions.addAll(productions);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(mRecyclerView, error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private class ProductionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Production mProduction;
        private LinearLayout mLinearLayout;
        private LinearLayout mInfoLinearLayout;
        private ImageView mPosterImageView;
        private TextView mTitleTextView;
        private TextView mRatingTextView;
        private TextView mCategoryTextView;
        private TextView mRuntimeTextView;

        private Bitmap loadBitmap(ImageView imageView) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

            return bitmapDrawable.getBitmap();
        }


        public ProductionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mLinearLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_layout);
            mInfoLinearLayout = (LinearLayout)
                    itemView.findViewById(R.id.list_item_info_layout);
            mPosterImageView = (ImageView)
                    itemView.findViewById(R.id.list_item_poster);
            mTitleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_title_text_view);
            mRatingTextView = (TextView)
                    itemView.findViewById(R.id.list_item_rating_text_view);
            mCategoryTextView = (TextView)
                    itemView.findViewById(R.id.list_item_category_text_view);
            mRuntimeTextView = (TextView)
                    itemView.findViewById(R.id.list_item_runtime_text_view);
        }

        public void bindProduction(Production production) {
            mProduction = production;
            mTitleTextView.setText(mProduction.getShowTitle());
            mRatingTextView.setText(mProduction.getRating());
            mCategoryTextView.setText(mProduction.getCategory());
            mRuntimeTextView.setText(mProduction.getRuntime());
            Picasso.with(getActivity()).load(mProduction.getPoster())
                    .placeholder(R.drawable.ic_movie_red)
                    .resize(240, 240)
                    .centerCrop()
                    .into(mPosterImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = loadBitmap(mPosterImageView);
                            mLinearLayout.setBackgroundColor(Palette
                                    .from(bitmap)
                                    .generate()
                                    .getLightVibrantColor(Color.BLACK));
                            mInfoLinearLayout.setBackgroundColor(Palette
                                    .from(bitmap)
                                    .generate()
                                    .getVibrantColor(Color.BLACK));
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }

        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            ProductionDetailFragment dialog = ProductionDetailFragment
                    .newInstance(mProduction);
            dialog.show(fragmentManager, DIALOG_DETAIL);
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
        public void onBindViewHolder(final ProductionHolder holder, int position) {
            Production production = mProductions.get(position);
            holder.bindProduction(production);

        }


        @Override
        public int getItemCount() {
            return mProductions.size();
        }
    }
}
