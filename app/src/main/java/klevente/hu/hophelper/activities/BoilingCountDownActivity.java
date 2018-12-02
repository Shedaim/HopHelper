package klevente.hu.hophelper.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.events.BoilFinishEvent;
import klevente.hu.hophelper.events.BoilPauseEvent;
import klevente.hu.hophelper.events.BoilUpdateEvent;

import static klevente.hu.hophelper.activities.BeerDetailActivity.BEER_INDEX;

public class BoilingCountDownActivity extends AppCompatActivity {

    private TextView boilTextView;
    private boolean paused = false;
    private int beerIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boiling_count_down);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            paused = !paused;
            if (paused) {
                fab.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
            } else {
                fab.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
            }
            EventBus.getDefault().post(new BoilPauseEvent(paused));
        });

        boilTextView = findViewById(R.id.tvBoilCountDown);
        TextView nameTextView = findViewById(R.id.tvBoilCountDownName);

        beerIdx = getIntent().getIntExtra(BEER_INDEX, -1);
        nameTextView.setText(BeerList.get(beerIdx).name);
    }

    @Subscribe
    public void onBoilUpdateEvent(BoilUpdateEvent event) {
        boilTextView.setText(getString(R.string.boiling_for, event.grams, event.name, MinSecondDateFormat.format(event.millis)));
    }

    @Subscribe
    public void onBoilFinishEvent(BoilFinishEvent event) {
        Intent intent = new Intent(BoilingCountDownActivity.this, BeerDetailActivity.class);
        intent.putExtra(BEER_INDEX, beerIdx);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
