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
import klevente.hu.hophelper.events.MashFinishEvent;
import klevente.hu.hophelper.events.MashPauseEvent;
import klevente.hu.hophelper.events.MashUpdateEvent;

import static klevente.hu.hophelper.activities.BeerDetailActivity.BEER_INDEX;

public class MashingCountDownActivity extends AppCompatActivity {

    private TextView mashTextView;
    private boolean paused = false;
    private int beerIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mashing_count_down);
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
            EventBus.getDefault().post(new MashPauseEvent(paused));
        });

        mashTextView = findViewById(R.id.tvMashCountDown);
        TextView nameTextView = findViewById(R.id.tvMashCountDownName);

        beerIdx = getIntent().getIntExtra(BEER_INDEX, -1);
        nameTextView.setText(BeerList.get(beerIdx).name);
    }

    @Subscribe
    public void onMashUpdateEvent(MashUpdateEvent event) {
        mashTextView.setText(getString(R.string.mashing_at, event.temp, MinSecondDateFormat.format(event.millis)));
    }

    @Subscribe
    public void onMashFinishEvent(MashFinishEvent event) {
        Intent intent = new Intent(MashingCountDownActivity.this, BeerDetailActivity.class);
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
