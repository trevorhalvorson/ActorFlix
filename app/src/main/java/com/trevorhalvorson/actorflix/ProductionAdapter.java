package com.trevorhalvorson.actorflix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Trevor Halvorson on 1/1/2016.
 */
public class ProductionAdapter extends RecyclerView.Adapter<ProductionAdapter.Holder> {

    private List<Production> mProductions;
    private Context mContext;

    public ProductionAdapter(List<Production> productions, Context context) {
        mProductions = productions;
        mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_production, parent, false);

        return new Holder(itemView);
    }

    @Override
    public int getItemCount() {
        return (mProductions != null ? mProductions.size() : 0);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Production production = mProductions.get(position);
        holder.bindProduction(production);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private Production mProduction;
        private ImageView mPosterImageView;
        private TextView mTitleTextView;
        private LinearLayout mItemBackground;

        public Holder(View itemView) {
            super(itemView);
            mPosterImageView = (ImageView) itemView.findViewById(R.id.list_item_poster_image_view);
            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_title_text_view);
            mItemBackground = (LinearLayout) itemView.findViewById(R.id.list_item_info_layout);
        }

        private void bindProduction(Production production) {
            mProduction = production;

            Picasso.with(mPosterImageView.getContext()).load(mProduction.getPoster())
                    .placeholder(R.drawable.ic_movie_red)
                    .resize(240, 240)
                    .centerCrop()
                    .into(mPosterImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = loadBitmap(mPosterImageView);
                            Palette.from(bitmap).generate(
                                    new Palette.PaletteAsyncListener() {
                                        @Override
                                        public void onGenerated(Palette palette) {
                                            mItemBackground.setBackgroundColor(palette.getDarkVibrantColor(Color.BLACK));
                                            mPosterImageView.setBackgroundColor(palette.getVibrantColor(Color.WHITE));
                                        }
                                    }
                            );
                        }

                        @Override
                        public void onError() {

                        }
                    });

            mTitleTextView.setText(mProduction.getShowTitle());
        }
    }

    private Bitmap loadBitmap(ImageView imageView) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();

        return bitmapDrawable.getBitmap();
    }
}
