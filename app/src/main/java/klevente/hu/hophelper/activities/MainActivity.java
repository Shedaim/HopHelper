package klevente.hu.hophelper.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.MainBeerAdapter;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.database.HopHelperDatabase;


public class MainActivity extends AppCompatActivity implements MainBeerAdapter.BeerAdapterListener {
    public static final int ADD_NEW_BEER = 100;

    private MainBeerAdapter adapter;

    private HopHelperDatabase database;

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvMain);
        adapter = new MainBeerAdapter(recyclerView, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadAllBeers();
        // debugInitData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.beer_detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabNewBeerDone);
        fab.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, NewBeerActivity.class);
            startActivityForResult(intent, ADD_NEW_BEER);
        });



        database = Room.databaseBuilder(getApplicationContext(), HopHelperDatabase.class, "hophelper").build();
        initRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NEW_BEER) {
            if (resultCode == RESULT_OK) {
                Beer newBeer = (Beer) data.getSerializableExtra("beer");
                onNewBeerCreated(newBeer);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_export:
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onItemClick(int index) {
        Intent intent = new Intent(MainActivity.this, BeerDetailActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    public void loadAllBeers() {
        new AsyncTask<Void, Void, List<Beer>>() {
            @Override
            protected List<Beer> doInBackground(Void... voids) {
                return database.beerDao().getAll();
            }

            @Override
            protected void onPostExecute(List<Beer> beers) {
                adapter.updateAll(beers);
            }
        }.execute();
    }


    public void onNewBeerCreated(final Beer beer) {
        new AsyncTask<Void, Void, Beer>() {
            @Override
            protected Beer doInBackground(Void... voids) {
                database.beerDao().insert(beer);
                List<Beer> all = database.beerDao().getAll();
                Beer withId = all.get(all.size()-1);
                return withId;
            }

            @Override
            protected void onPostExecute(Beer beer) {
                adapter.addItem(beer);
            }
        }.execute();

    }
}
