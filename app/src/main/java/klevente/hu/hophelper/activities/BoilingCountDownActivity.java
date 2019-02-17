package klevente.hu.hophelper.activities;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.data.Ingredient;
import klevente.hu.hophelper.events.BoilEvent;

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

        FloatingActionMenu fab = findViewById(R.id.fab);
        FloatingActionButton stop_fab = findViewById(R.id.stop_fab);
        FloatingActionButton play_pause_fab = findViewById(R.id.play_pause_fab);;

        play_pause_fab.setOnClickListener(v -> {
            paused = !paused;
            if (paused) {
                play_pause_fab.setImageDrawable(getDrawable(R.drawable.ic_play_arrow_black_24dp));
            } else {
                play_pause_fab.setImageDrawable(getDrawable(R.drawable.ic_pause_black_24dp));
            }
            EventBus.getDefault().post(new BoilEvent("pause", paused));
        });
        stop_fab.setOnClickListener(v -> EventBus.getDefault().post(new BoilEvent("stop", true)));

        boilTextView = findViewById(R.id.tvBoilCountDown);
        TextView nameTextView = findViewById(R.id.tvBoilCountDownName);
        beerIdx = getIntent().getIntExtra(BEER_INDEX, -1);
        nameTextView.setText(BeerList.get(beerIdx).name);
    }

    @Subscribe
    public void onBoilUpdateEvent(Ingredient event) {
        boilTextView.setText(getString(R.string.boiling_for, event.quantity, event.name, MinSecondDateFormat.format(event.time)));
    }

    @Subscribe
    public void onBoilEvent(BoilEvent event) {
        Intent intent;
        switch (event.action) {
            case "stop":
                intent = new Intent(BoilingCountDownActivity.this, BeerDetailActivity.class);
                intent.putExtra(BEER_INDEX, beerIdx);
                startActivity(intent);
                break;
            case "finish":
                intent = new Intent(BoilingCountDownActivity.this, BeerDetailActivity.class);
                intent.putExtra(BEER_INDEX, beerIdx);
                startActivity(intent);
                break;
            case "pause":
                break;
        }
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
