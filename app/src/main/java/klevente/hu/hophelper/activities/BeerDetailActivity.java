package klevente.hu.hophelper.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gson.Gson;
import java.util.Collections;
import klevente.hu.hophelper.fragments.BeerDetailIngrendientsFragment;
import klevente.hu.hophelper.fragments.BeerDetailsBoilingFragment;
import klevente.hu.hophelper.fragments.BeerDetailsFermentationFragment;
import klevente.hu.hophelper.fragments.BeerDetailsMashingFragment;
import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.google_drive.DriveComplex;
import klevente.hu.hophelper.google_drive.DriveServiceHelper;
import klevente.hu.hophelper.services.BoilingCountdownService;
import klevente.hu.hophelper.services.MashingCountdownService;

public class BeerDetailActivity extends AppCompatActivity {
    private static final int EDIT_BEER = 101;
    public static final String BEER_INDEX = "index";

    String TAG = "BeerDetail";
    String fileName, fileContent;
    boolean duplicate_file;
    ViewPager viewPager;
    private String mFolderId = MainActivity.folderID;
    private Beer beer;
    private int beerIdx;
    private DriveServiceHelper mDriveServiceHelper = MainActivity.mDriveServiceHelper;


    @Override
    protected void onStart() {
        super.onStart();
        //requestSignIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        Toolbar toolbar = findViewById(R.id.beer_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.beer_detail_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.beer_detail_tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        initFabs();

        beerIdx = getIntent().getIntExtra(BEER_INDEX, -1);
        beer = BeerList.get(beerIdx);
        toolbar.setTitle(beer.name);
    }

    /**
     * Opens the Storage Access Framework file picker using {@link }.
     *
    private void openFilePicker() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening file picker.");

            Intent pickerIntent = mDriveServiceHelper.createFilePickerIntent();

            // The result of the SAF Intent is handled in onActivityResult.
            startActivityForResult(pickerIntent, REQUEST_CODE_OPEN_DOCUMENT);
        }
    }

    /**
     * Opens a file from its {@code uri} returned from the Storage Access Framework file picker
     * initiated by {@link ()}.

    private void openFileFromFilePicker(Uri uri) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Opening " + uri.getPath());

            mDriveServiceHelper.openFileUsingStorageAccessFramework(getContentResolver(), uri)
                    .addOnSuccessListener(nameAndContent -> {
                        String name = nameAndContent.first;
                        String content = nameAndContent.second;

                        // Files opened through SAF cannot be modified, except by retrieving the
                        // fileId from its metadata and updating it via the REST API. To modify
                        // files not created by your app, you will need to request the Drive
                        // Full Scope and submit your app to Google for review.
                        setReadOnlyMode();
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to open file from picker.", exception));
        }
    }
     */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_export:
                Snackbar.make(viewPager, R.string.not_implemented, Snackbar.LENGTH_SHORT).show();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
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
                case 3:  return BeerDetailsFermentationFragment.newInstance(beerIdx);
                default: return BeerDetailIngrendientsFragment.newInstance(beerIdx);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    private void initFabs() {
        FloatingActionMenu fabMenu = findViewById(R.id.fabMenu);
        FloatingActionButton fabEdit = findViewById(R.id.fabBeerDetailEdit);
        FloatingActionButton fabStartMash = findViewById(R.id.fabBeerDetailStartMash);
        FloatingActionButton fabStartBoil = findViewById(R.id.fabBeerDetailStartBoil);
        FloatingActionButton fabStartFermentation = findViewById(R.id.fabBeerDetailStartFermentation);
        FloatingActionButton fabSaveToDrive = findViewById(R.id.save_btn);

        fabEdit.setOnClickListener(v -> {
            Intent intent = new Intent(BeerDetailActivity.this, NewBeerActivity.class);
            intent.putExtra("beer", beer);
            startActivityForResult(intent, EDIT_BEER);
        });

        fabStartMash.setOnClickListener(v -> {
            Intent intent = new Intent(BeerDetailActivity.this, MashingCountdownService.class);
            intent.putExtra(MashingCountdownService.BEER_INDEX, beerIdx);
            startService(intent);

            Intent intent1 = new Intent(BeerDetailActivity.this, MashingCountDownActivity.class);
            intent1.putExtra(BEER_INDEX, beerIdx);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent1);
        });

        fabStartBoil.setOnClickListener(v -> {
            Intent intent = new Intent(BeerDetailActivity.this, BoilingCountdownService.class);
            intent.putExtra(BoilingCountdownService.BEER_INDEX, beerIdx);
            startService(intent);

            Intent intent1 = new Intent(BeerDetailActivity.this, BoilingCountDownActivity.class);
            intent1.putExtra(BEER_INDEX, beerIdx);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent1);
        });

        fabStartFermentation.setOnClickListener(v -> {
            //TODO Add fermentation period to calendar
            Snackbar.make(v, R.string.not_implemented, Snackbar.LENGTH_SHORT).show();
        });

        fabSaveToDrive.setOnClickListener(v -> {
            fileName = beer.name + ".json";
            Gson gson = new Gson();
            fileContent = gson.toJson(beer);
            DriveComplex.saveBeerToFile(mDriveServiceHelper, beer, fileName, mFolderId, fileContent, viewPager, TAG, BeerDetailActivity.this);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case EDIT_BEER:
                if (resultCode == RESULT_OK){
                    Intent result = new Intent();
                    assert data != null;
                    result.putExtra("beer", data.getSerializableExtra("beer"));
                    result.putExtra("old_beer", data.getSerializableExtra("old_beer"));
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
                break;
        }
    }
}
