package klevente.hu.hophelper.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import klevente.hu.hophelper.fragments.BeerDetailIngrendientsFragment;
import klevente.hu.hophelper.fragments.BeerDetailsBoilingFragment;
import klevente.hu.hophelper.fragments.BeerDetailsMashingFragment;
import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.services.CountdownService;

public class BeerDetailActivity extends AppCompatActivity {

    private SectionsPagerAdapter sectionsPagerAdapter;

    private ViewPager viewPager;

    private Beer beer;
    private int beerIdx;

    private FloatingActionButton fabEdit;
    private FloatingActionButton fabStartMash;
    private FloatingActionButton fabStartBoil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        Toolbar toolbar = findViewById(R.id.beer_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.beer_detail_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.beer_detail_tabs);
        tabLayout.addOnTabSelectedListener(onTabSelectedListener);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        viewPager.addOnPageChangeListener(onPageChangeListener);

        initFabs();

        beerIdx = getIntent().getIntExtra("index", -1);
        beer = BeerList.get(beerIdx);
        toolbar.setTitle(beer.name);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_detail, menu);
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:  return BeerDetailIngrendientsFragment.newInstance(beerIdx);
                case 1:  return BeerDetailsMashingFragment.newInstance(beerIdx);
                case 2:  return BeerDetailsBoilingFragment.newInstance(beerIdx);
                default: return BeerDetailIngrendientsFragment.newInstance(beerIdx);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    private void initFabs() {
        fabEdit = findViewById(R.id.fabBeerDetailEdit);
        fabStartMash = findViewById(R.id.fabBeerDetailStartMash);
        fabStartBoil = findViewById(R.id.fabBeerDetailStartBoil);

        fabEdit.show();
        fabStartMash.hide();
        fabStartBoil.hide();

        fabStartMash.setOnClickListener(v -> {
            Intent intent = new Intent(BeerDetailActivity.this, CountdownService.class);
            intent.putExtra("time", 30000);
            startService(intent);
        });
    }

    private void animateFab(int position) {
        switch (position) {
            case 0:
                fabEdit.show();
                fabStartMash.hide();
                fabStartBoil.hide();
                break;
            case 1:
                fabStartMash.show();
                fabEdit.hide();
                fabStartBoil.hide();
                break;
            case 2:
                fabStartBoil.show();
                fabEdit.hide();
                fabStartMash.hide();
                break;
            default:
                fabEdit.show();
                fabStartMash.hide();
                fabStartBoil.hide();
                break;
        }
    }

    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) { animateFab(tab.getPosition()); }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {}

        @Override
        public void onPageSelected(int i) { animateFab(i); }

        @Override
        public void onPageScrollStateChanged(int i) {}
    };
}
