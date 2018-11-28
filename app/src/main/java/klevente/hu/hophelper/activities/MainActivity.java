package klevente.hu.hophelper.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.BeerAdapter;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.HopHelperDatabase;
import klevente.hu.hophelper.fragments.NewBeerDialogFragment;

public class MainActivity extends AppCompatActivity implements BeerAdapter.BeerAdapterListener, NewBeerDialogFragment.NewBeerDialogListener {

    private RecyclerView recyclerView;
    private BeerAdapter adapter;

    private HopHelperDatabase database;

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.MainRecyclerView);
        adapter = new BeerAdapter(recyclerView, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(int index) {
        Intent intent = new Intent(MainActivity.this, BeerDetailActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    @Override
    public void onNewBeerCreated(Beer beer) {
        
    }
}
