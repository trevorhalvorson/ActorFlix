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
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int SPEECH_REQUEST_CODE = 0;

    private FlixApi flixService;
    private CompositeSubscription subscriptions = new CompositeSubscription();

    private List<Production> productionList;
    private ProductionAdapter productionAdapter;
    private ProgressDialog progressDialog;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productionList = new ArrayList<>();
        productionAdapter = new ProductionAdapter(productionList, this);

        recyclerView = (RecyclerView) findViewById(R.id.production_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(productionAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(this, (view, position) -> {
                    Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
                    detailIntent.putExtra(DetailActivity.EXTRA_PARAM, productionList.get(position));

                    Pair imagePair = new Pair<>(view.findViewById(R.id.list_item_poster_image_view), DetailActivity.IMAGE_TRANSITION_NAME);
                    Pair titlePair = new Pair<>(view.findViewById(R.id.list_item_title_text_view), DetailActivity.TITLE_TRANSITION_NAME);
                    Pair backgroundPair = new Pair<>(view.findViewById(R.id.list_item_info_layout), DetailActivity.BACKGROUND_TRANSITION_NAME);

                    ActivityOptionsCompat transitionActivityOptions =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    MainActivity.this, imagePair, titlePair, backgroundPair);

                    ActivityCompat.startActivity(MainActivity.this,
                            detailIntent, transitionActivityOptions.toBundle());
                }));

        flixService = FlixService.createFlixService();

        // Auto-search after startup for testing
        startSearch("Harrison Ford");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (subscriptions == null || subscriptions.isUnsubscribed()) {
            subscriptions = new CompositeSubscription();
        }
    }

    private void startSearch(String query) {
        progressDialog = ProgressDialog.show(this, "Loading Productions",
                "Please wait...", true);

        productionList.clear();

        subscriptions.add(
                flixService.listProductions(query)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<Production>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(List<Production> productions) {
                                progressDialog.dismiss();

                                for (Production p : productions) {
                                    productionList.add(p);
                                }

                                productionAdapter = new ProductionAdapter(productionList, getApplicationContext());
                                recyclerView.setAdapter(productionAdapter);

                                // Alert user if no results are returned from the service
                                if (productionList == null) {
                                    Snackbar.make(recyclerView, "No results found.", Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }));
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
            e.printStackTrace();
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
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_item_search);

        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_view_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
