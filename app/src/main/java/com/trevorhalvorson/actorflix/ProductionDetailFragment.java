package com.trevorhalvorson.actorflix;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Trevor on 8/26/2015.
 */
public class ProductionDetailFragment extends DialogFragment {

    private static final String ARG_PRODUCTION = "production";

    private Production mProduction;
    private ImageView mImageView;
    private TextView mDirectorTextView;
    private TextView mCastTextView;
    private TextView mRatingTextView;
    private TextView mSummaryTextView;
    private View mView;


    public static ProductionDetailFragment newInstance(Production production) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PRODUCTION, production);

        ProductionDetailFragment fragment = new ProductionDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_production_detail, null);

        mProduction = (Production) getArguments().getSerializable(ARG_PRODUCTION);
        mImageView = (ImageView) mView.findViewById(R.id.dialog_poster_image_view);
        mDirectorTextView = (TextView) mView.findViewById(R.id.dialog_director_text_view);
        mCastTextView = (TextView) mView.findViewById(R.id.dialog_showcast_text_view);
        mRatingTextView = (TextView) mView.findViewById(R.id.dialog_rating_text_view);
        mSummaryTextView = (TextView) mView.findViewById(R.id.dialog_summary_text_view);

        Picasso.with(getActivity())
                .load(mProduction.getPoster())
                .placeholder(R.drawable.ic_movie_red)
                .error(R.drawable.ic_movie_red)
                .into(mImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                        Bitmap bitmap = drawable.getBitmap();
                        mImageView.setBackgroundColor(Palette
                                .from(bitmap)
                                .generate()
                                .getVibrantColor(Color.BLACK));
                    }

                    @Override
                    public void onError() {
                        mImageView.setVisibility(View.GONE);
                    }
                });

        mDirectorTextView.setText("Directed By: " + mProduction.getDirector());
        mCastTextView.setText("Starring: " + mProduction.getShowCast());
        mRatingTextView.setText("Rating: " + mProduction.getRating());
        mSummaryTextView.setText(mProduction.getSummary());

        return new AlertDialog.Builder(getActivity())
                .setView(mView)
                .setTitle(mProduction.getShowTitle())
                .setPositiveButton(getString(R.string.dialog_watch_button_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.netflix.com/title/" + mProduction.getShowId()));
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.dialog_close_button_text), null)
                .create();
    }
}
