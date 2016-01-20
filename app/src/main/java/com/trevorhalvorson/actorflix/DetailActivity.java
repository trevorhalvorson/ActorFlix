package com.trevorhalvorson.actorflix;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_PARAM = "extra_production";
    public static final String IMAGE_TRANSITION_NAME = "image_transition";
    public static final String TITLE_TRANSITION_NAME = "title_transition";
    public static final String BACKGROUND_TRANSITION_NAME = "background_transition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Production production = (Production) getIntent().getSerializableExtra(EXTRA_PARAM);

        final ImageView posterImageView = (ImageView) findViewById(R.id.poster_image_view);
        TextView titleTextView = (TextView) findViewById(R.id.title_text_view);
        TextView descriptionTextView = (TextView) findViewById(R.id.description_text_view);
        final LinearLayout backgroundLinearLayout = (LinearLayout) findViewById(R.id.background_linear_layout);

        ViewCompat.setTransitionName(posterImageView, IMAGE_TRANSITION_NAME);
        ViewCompat.setTransitionName(titleTextView, TITLE_TRANSITION_NAME);
        ViewCompat.setTransitionName(backgroundLinearLayout, BACKGROUND_TRANSITION_NAME);

        Picasso.with(this).load(production.getPoster())
                .placeholder(R.drawable.ic_movie_red)
                .resize(240, 240)
                .centerCrop()
                .into(posterImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = loadBitmap(posterImageView);
                        Palette.from(bitmap).generate(
                                new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        backgroundLinearLayout.setBackgroundColor(palette.getDarkVibrantColor(Color.BLACK));
                                        posterImageView.setBackgroundColor(palette.getVibrantColor(Color.WHITE));
                                    }
                                }
                        );
                    }

                    @Override
                    public void onError() {

                    }
                });

        titleTextView.setText(production.getShowTitle());
        descriptionTextView.setText(getDescription(production));
    }

    private Bitmap loadBitmap(ImageView imageView) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

        return bitmapDrawable.getBitmap();
    }

    @NonNull
    private String getDescription(Production production) {
        return production.getSummary() +
                "\n\n Released: " + production.getReleaseYear() +
                "\n\n Rating: " + production.getRating() +
                "\n\n Directed by: " + production.getDirector() +
                "\n\n Cast: " + production.getShowCast() +
                "\n\n Runtime: " + production.getRuntime();
    }
}
