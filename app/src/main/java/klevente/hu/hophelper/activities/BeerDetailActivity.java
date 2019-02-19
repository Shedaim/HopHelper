package klevente.hu.hophelper.activities;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
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
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.io.Serializable;

import klevente.hu.hophelper.database.HopHelperDatabase;
import klevente.hu.hophelper.fragments.BeerDetailIngrendientsFragment;
import klevente.hu.hophelper.fragments.BeerDetailsBoilingFragment;
import klevente.hu.hophelper.fragments.BeerDetailsFermentationFragment;
import klevente.hu.hophelper.fragments.BeerDetailsMashingFragment;
import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.services.BoilingCountdownService;
import klevente.hu.hophelper.services.MashingCountdownService;

public class BeerDetailActivity extends AppCompatActivity {
    private static final int EDIT_BEER = 101;
    public static final String BEER_INDEX = "index";
    //private static final int REQUEST_CODE_SIGN_IN = 0;
    //private static final int REQUEST_CODE_OPEN_ITEM = 1;

    private Beer beer;
    private int beerIdx;

    private FloatingActionMenu fabMenu;
    private FloatingActionButton fabEdit;
    private FloatingActionButton fabStartMash;
    private FloatingActionButton fabStartBoil;
    private FloatingActionButton fabStartFermentation;

    //private DriveClient driveClient;
    //private DriveResourceClient driveResourceClient;
    //private TaskCompletionSource<DriveId> openItemTaskSource;

    @Override
    protected void onStart() {
        super.onStart();
        //signIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_detail);

        Toolbar toolbar = findViewById(R.id.beer_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.beer_detail_container);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.beer_detail_tabs);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));


        initFabs();

        beerIdx = getIntent().getIntExtra(BEER_INDEX, -1);
        beer = BeerList.get(beerIdx);
        toolbar.setTitle(beer.name);

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                if (resultCode != RESULT_OK) {
                    return;
                }

                Task<GoogleSignInAccount> getAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                if (getAccountTask.isSuccessful()) {
                    initializeDriveClient(getAccountTask.getResult());
                }
                break;
            case REQUEST_CODE_OPEN_ITEM:
                if (resultCode == RESULT_OK) {
                    assert data != null;
                    DriveId driveId = data.getParcelableExtra(
                            OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    openItemTaskSource.setResult(driveId);
                } else {
                    openItemTaskSource.setException(new RuntimeException("Unable to open file"));
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void signIn() {
        Set<Scope> requiredScopes = new HashSet<>(2);
        requiredScopes.add(Drive.SCOPE_FILE);
        requiredScopes.add(Drive.SCOPE_APPFOLDER);
        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null && signInAccount.getGrantedScopes().containsAll(requiredScopes)) {
            initializeDriveClient(signInAccount);
        } else {
            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(Drive.SCOPE_FILE).requestScopes(Drive.SCOPE_APPFOLDER).build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, signInOptions);
            startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
        }
    }

    private void initializeDriveClient(GoogleSignInAccount signInAccount) {
        driveClient = Drive.getDriveClient(getApplicationContext(), signInAccount);
        driveResourceClient = Drive.getDriveResourceClient(getApplicationContext(), signInAccount);
    }
    private void createFileInFolder(final DriveFolder parent) {
        driveResourceClient
                .createContents()
                .continueWithTask(task -> {
                    DriveContents contents = task.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writeContents(writer);
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle(beer.name + ".txt")
                            .setMimeType("text/plain")
                            .build();

                    return driveResourceClient.createFile(parent, changeSet, contents);
                })
                .addOnSuccessListener(this,
                        driveFile -> showMessage(getString(R.string.file_created)))
                .addOnFailureListener(this, e -> {
                    showMessage(getString(R.string.unable));
                });
    }


    private void writeContents(Writer writer) throws IOException {
        StringBuilder builder = new StringBuilder(beer.name + '\n');
        builder.append(beer.style).append('\n').
                append(beer.batchSize).append(" l\n").
                append(beer.description).append('\n').
                append(getString(R.string.abv, beer.abv)).append('\n').
                append("OG: ").append(beer.og).append('\n').
                append("FG").append(beer.fg).append('\n').
                append(beer.yeast).append('\n');

        builder.append("\nIngredients\n");
        for (Map.Entry<String, Double> m : beer.malts.entrySet()) {
            builder.append(m.getKey()).append(": ").append(m.getValue()).append('\n');
        }

        for (Map.Entry<String, Double> m : beer.hops.entrySet()) {
            builder.append(m.getKey()).append(": ").append(m.getValue()).append('\n');
        }

        for (Map.Entry<String, Double> m : beer.extras.entrySet()) {
            builder.append(m.getKey()).append(": ").append(m.getValue()).append('\n');
        }

        builder.append("\nMashing\n");
        for (MashTime m : beer.mashingTimes) {
            builder.append(getString(R.string.celsius, m.temp)).append(": ").append(MinSecondDateFormat.format(m.millis)).append('\n');
        }

        builder.append("\nBoiling\n");
        for (HopAddition h : beer.boilingTimes) {
            builder.append(h.name).append(' ').append(getString(R.string.g, h.grams)).append(": ").append(MinSecondDateFormat.format(h.millis)).append('\n');
        }

        builder.append("\nFermentation\n");
        for (FermentationTime f : beer.fermentationTimes) {
            builder.append(f.dry_hop).append(' ').append(getString(R.string.g, f.grams)).append(": ").append(getString(R.string.celsius, f.temp)).append(TimeUnit.MILLISECONDS.toDays(f.millis)).append('\n');
        }

        writer.write(builder.toString());
    }


    protected void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    private Task<DriveId> pickItem(OpenFileActivityOptions openOptions) {
        openItemTaskSource = new TaskCompletionSource<>();
        driveClient
                .newOpenFileActivityIntentSender(openOptions)
                .continueWith((Continuation<IntentSender, Void>) task -> {
                    startIntentSenderForResult(
                            task.getResult(), REQUEST_CODE_OPEN_ITEM, null, 0, 0, 0);
                    return null;
                });
        return openItemTaskSource.getTask();
    }

    private Task<DriveId> pickFolder() {
        OpenFileActivityOptions openOptions =
                new OpenFileActivityOptions.Builder()
                        .setSelectionFilter(
                                Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE))
                        .setActivityTitle(getString(R.string.select_folder))
                        .build();
        return pickItem(openOptions);
    }

    private void exportToDrive() {
        pickFolder().addOnSuccessListener(this, driveId -> {
            createFileInFolder(driveId.asDriveFolder());
        }).addOnFailureListener(this, e -> {
        });
    }

*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beer_detail, menu);
        return true;
    }
/*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_export:
                    exportToDrive();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
*/

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
        fabMenu = findViewById(R.id.fabMenu);
        fabEdit = findViewById(R.id.fabBeerDetailEdit);
        fabStartMash = findViewById(R.id.fabBeerDetailStartMash);
        fabStartBoil = findViewById(R.id.fabBeerDetailStartBoil);
        fabStartFermentation = findViewById(R.id.fabBeerDetailStartFermentation);

        fabEdit.setOnClickListener(v -> {
            //TODO Add fermentation period to calendar
            Snackbar.make(v, R.string.not_implemented, Snackbar.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_BEER){
            if (resultCode == RESULT_OK){
                Intent result = new Intent();
                result.putExtra("beer", data.getSerializableExtra("beer"));
                result.putExtra("old_beer", data.getSerializableExtra("old_beer"));
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        }
    }
}
