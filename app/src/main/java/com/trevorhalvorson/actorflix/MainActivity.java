package com.trevorhalvorson.actorflix;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SPEECH_REQUEST_CODE = 0;

    private List<Production> mProductions;
    private FlixService mService;
    private ProductionAdapter mProductionAdapter;
    private ProgressDialog mProgressDialog;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProductionAdapter = new ProductionAdapter(mProductions, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.production_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mProductionAdapter);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(this, new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                        detailIntent.putExtra(DetailActivity.EXTRA_PARAM, mProductions.get(position));

                        Pair imagePair = new Pair<>(view.findViewById(R.id.list_item_poster_image_view), DetailActivity.IMAGE_TRANSITION_NAME);
                        Pair titlePair = new Pair<>(view.findViewById(R.id.list_item_title_text_view), DetailActivity.TITLE_TRANSITION_NAME);
                        Pair backgroundPair = new Pair<>(view.findViewById(R.id.list_item_info_layout), DetailActivity.BACKGROUND_TRANSITION_NAME);

                        ActivityOptionsCompat transitionActivityOptions =
                                ActivityOptionsCompat.makeSceneTransitionAnimation(
                                        MainActivity.this, imagePair, titlePair, backgroundPair);

                        ActivityCompat.startActivity(MainActivity.this,
                                detailIntent, transitionActivityOptions.toBundle());
                    }
                }));

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://netflixroulette.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mService = retrofit.create(FlixService.class);

        // Auto-search after startup for testing
        startSearch("Harrison Ford");
    }

    private void startSearch(String query) {
        mProgressDialog = ProgressDialog.show(this, "Loading Productions",
                "Please wait...", true);

        Call<List<Production>> productions = mService.listProductions(query);
        productions.enqueue(new Callback<List<Production>>() {
            @Override
            public void onResponse(Response<List<Production>> response, Retrofit retrofit) {
                mProductions = response.body();

                mProductionAdapter = new ProductionAdapter(mProductions, getApplicationContext());
                mRecyclerView.setAdapter(mProductionAdapter);

                mProgressDialog.dismiss();

                // Alert user if no results are returned from the service
                if (mProductions == null) {
                    Snackbar.make(mRecyclerView, "No results found.", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void voiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.menu_voice_prompt));
        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SPEECH_REQUEST_CODE:
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    startSearch(result.get(0));

                    Log.i(TAG, "onActivityResult " + result);
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        mSearchView = (SearchView) searchItem.getActionView();
        mSearchView.setQueryHint(getString(R.string.search_view_hint));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim().length() > 0) {
                    startSearch(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_voice:
                voiceSearch();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
