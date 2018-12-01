package klevente.hu.hophelper.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.database.HopHelperDatabase;
import klevente.hu.hophelper.fragments.NewBeerDialogFragment;

public class MainActivity extends AppCompatActivity implements MainBeerAdapter.BeerAdapterListener, NewBeerDialogFragment.NewBeerDialogListener {

    private RecyclerView recyclerView;
    private MainBeerAdapter adapter;

    private HopHelperDatabase database;

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rvMain);
        adapter = new MainBeerAdapter(recyclerView, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        // loadAllBeers();
        debugInitData();
    }

    @Deprecated
    private void debugInitData() {
        BeerList.debugAddTestBeers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.beer_detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            new NewBeerDialogFragment().show(getSupportFragmentManager(), NewBeerDialogFragment.TAG);
        });



        database = Room.databaseBuilder(getApplicationContext(), HopHelperDatabase.class, "hophelper").build();
        initRecyclerView();
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

    @Override
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

        adapter.addItem(beer);
    }
}
