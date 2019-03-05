package klevente.hu.hophelper.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Collections;
import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.MainBeerAdapter;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;
import klevente.hu.hophelper.database.HopHelperDatabase;
import klevente.hu.hophelper.google_drive.DriveComplex;
import klevente.hu.hophelper.google_drive.DriveServiceHelper;


public class MainActivity extends AppCompatActivity implements MainBeerAdapter.BeerAdapterListener {
    public static final int ADD_NEW_BEER = 100;
    private static final int EDIT_BEER = 101;
    private static final int DELETE_BEER = 102;
    private static boolean DELETE = false;
    private static final int REQUEST_CODE_SIGN_IN = 1;

    public static DriveServiceHelper mDriveServiceHelper;
    private MainBeerAdapter adapter;
    private HopHelperDatabase database;
    String TAG = "main";
    public static String folderID;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rvMain);
        adapter = new MainBeerAdapter(recyclerView, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        loadDatabaseBeers();
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.beer_detail_toolbar);
        setSupportActionBar(toolbar);

        database = Room.databaseBuilder(getApplicationContext(), HopHelperDatabase.class, "hophelper").build();

        FloatingActionMenu fab_menu = findViewById(R.id.fabMenu);
        FloatingActionButton fab_add = findViewById(R.id.fabNewBeerDone);
        FloatingActionButton fab_delete = findViewById(R.id.fabDeleteBeer);

        fab_add.setOnClickListener((view) -> {
            Intent intent = new Intent(MainActivity.this, NewBeerActivity.class);
            startActivityForResult(intent, ADD_NEW_BEER);
        });

        fab_delete.setOnClickListener((view) -> {
            DELETE = true;
        });

        requestSignIn();
        sharedPref = getApplicationContext().getSharedPreferences("settings",MODE_PRIVATE);
        editor = sharedPref.edit();
        initRecyclerView();
    }

    private void requestSignIn() {
        Log.d(TAG, "Requesting sign-in");
        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .build();
        GoogleSignInClient client = GoogleSignIn.getClient(this, signInOptions);
        // The result of the sign-in Intent is handled in onActivityResult.
        startActivityForResult(client.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }

    /**
     * Handles the {@code result} of a completed sign-in activity initiated from {@link
     * #requestSignIn()}.
     */
    private void handleSignInResult(Intent result) {
        GoogleSignIn.getSignedInAccountFromIntent(result)
                .addOnSuccessListener(googleAccount -> {
                    Log.d(TAG, "Signed in as " + googleAccount.getEmail());

                    // Use the authenticated account to sign in to the Drive service.
                    GoogleAccountCredential credential =
                            GoogleAccountCredential.usingOAuth2(
                                    this, Collections.singleton(DriveScopes.DRIVE_FILE));
                    credential.setSelectedAccount(googleAccount.getAccount());
                    Drive googleDriveService =
                            new Drive.Builder(
                                    AndroidHttp.newCompatibleTransport(),
                                    new GsonFactory(),
                                    credential)
                                    .setApplicationName("HopHelper")
                                    .build();
                    // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                    // Its instantiation is required before handling any onClick actions.
                    mDriveServiceHelper = new DriveServiceHelper(googleDriveService);
                    // Check if a root folder exists - if not, create one with default name
                    folderID = sharedPref.getString("root_folder", null);
                    if (folderID == null) mDriveServiceHelper.crateFolder(getString(R.string.app_name))
                    .addOnSuccessListener(folderId -> {
                        folderID = folderId;
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("root_folder", folderID);
                        editor.apply();
                    })
                    .addOnFailureListener(exception -> Log.d(TAG, "Could not create root folder " + exception));

                    else {
                        final Boolean[] found_root_folder = {false};
                        mDriveServiceHelper.queryFiles()
                            .addOnSuccessListener(fileList -> {
                                for (File f: fileList.getFiles()){
                                    if (f.getId().equals(folderID)) found_root_folder[0] = true;
                                }
                                if (found_root_folder[0].equals(false)) mDriveServiceHelper.crateFolder(getString(R.string.app_name))
                                    .addOnSuccessListener(folderId -> {
                                        folderID = folderId;
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("root_folder", folderID);
                                        editor.apply();
                                    })
                                    .addOnFailureListener(exception -> Log.d(TAG, "Could not create root folder " + exception));
                            });
                    }
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception))
                .addOnCompleteListener(a -> loadDriveBeers());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        case REQUEST_CODE_SIGN_IN:
            if (resultCode == Activity.RESULT_OK && data != null) {
                handleSignInResult(data);
            }
            break;

        case ADD_NEW_BEER:
            if (resultCode == RESULT_OK) {
                assert data != null;
                Beer newBeer = (Beer) data.getSerializableExtra("beer");
                String fileName = newBeer.name + ".json";
                Gson gson = new Gson();
                String fileContent = gson.toJson(newBeer);
                DriveComplex.saveBeerToFile(mDriveServiceHelper, newBeer, fileName, folderID, fileContent, recyclerView, TAG, MainActivity.this)
                    .addOnSuccessListener(fileID -> {
                        newBeer.file_id = fileID;
                        onNewBeerCreated(newBeer);
                    })
                    .addOnFailureListener(fileID -> Log.d(TAG, "Failed to Save new beer to Drive"));
            }
            break;
        case EDIT_BEER:
            if (resultCode == RESULT_OK) {
                assert data != null;
                Beer newBeer = (Beer) data.getSerializableExtra("beer");
                Beer oldBeer = (Beer) data.getSerializableExtra("old_beer");
                onBeerUpdated(newBeer, oldBeer);
            }
            break;
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
        if(DELETE){
            mDriveServiceHelper.deleteFile(BeerList.get(index).file_id)
                    .addOnSuccessListener(a -> onBeerDelete(BeerList.get(index)))
                    .addOnFailureListener(exception -> Log.d(TAG, "Could not delete file from Drive " + exception));
            DELETE = false;
        }
        else{
            Intent intent = new Intent(MainActivity.this, BeerDetailActivity.class);
            intent.putExtra("index", index);
            startActivityForResult(intent, EDIT_BEER);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void loadDriveBeers() {
        Log.d(TAG, "Loading Drive beers");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Beer> all = database.beerDao().getAll();

                mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> {
                        Log.d(TAG, fileList.getFiles().toString());
                        Boolean beer_already_in_database;
                        if (fileList == null){
                            adapter.updateAll(all);
                            return;
                        }
                        for(File f: fileList.getFiles()){
                            if (!f.getMimeType().equals("text/plain")) continue; //Go only over text files.
                            beer_already_in_database = false;
                            for (Beer b : all)
                                if (b.name.equals(f.getName().replace(".json", "")) ||
                                        (b.name + "-" + b.version).equals(f.getName().replace(".json", "")))
                                    beer_already_in_database = true;
                            // If beer not in database - add to list
                            if (!beer_already_in_database){
                                Log.d(TAG, "Trying to read file " + f.getName());
                                mDriveServiceHelper.readFile(f.getId())
                                    .addOnSuccessListener(content -> {
                                        try {
                                            Log.d(TAG, "Adding file " + f.getName() + " to database and updating adapter");
                                            Beer beer = Beer.fileToBeer(content.second);
                                            onNewBeerCreated(beer);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    })
                                    .addOnFailureListener(exception -> Log.d(TAG, "Could not read file from Drive " + exception));
                            }
                        }
                    })
                    .addOnFailureListener(exception -> Log.d(TAG, "Could not query files from Drive " + exception));
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void loadDatabaseBeers() {
        Log.d(TAG, "Loading database beers");
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

    @SuppressLint("StaticFieldLeak")
    public void onNewBeerCreated(Beer beer) {
        Log.d(TAG, "adding beer");
        new AsyncTask<Void, Void, Beer>() {
            @Override
            protected Beer doInBackground(Void... voids) {
                database.beerDao().insert(beer);
                List<Beer> all = database.beerDao().getAll();
                Beer withId = all.get(all.size() - 1);
                return withId;
            }

            @Override
            protected void onPostExecute(Beer beer) {
                adapter.addItem(beer);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void onBeerUpdated(Beer beer, Beer old_beer) {
        Log.d(TAG, "updating beer");
        new AsyncTask<Void, Void, Beer>() {
            @Override
            protected Beer doInBackground(Void... voids) {
                //database.beerDao().update(beer);
                database.beerDao().insert(beer);
                database.beerDao().delete(old_beer);
                List<Beer> all = database.beerDao().getAll();
                Beer withId = all.get(all.size()-1);
                return withId;
            }

            @Override
            protected void onPostExecute(Beer beer) {
                adapter.updateItem(beer, old_beer);
            }
        }.execute();
        recreate();
    }

    @SuppressLint("StaticFieldLeak")
    public void onBeerDelete(Beer beer) {
        Log.d(TAG, "deleting beer");
        new AsyncTask<Void, Void, Beer>() {
            @Override
            protected Beer doInBackground(Void... voids) {
                database.beerDao().delete(beer);
                List<Beer> all = database.beerDao().getAll();
                Beer withId = all.get(all.size()-1);
                return withId;
            }

            @Override
            protected void onPostExecute(Beer beer) {
                adapter.removeItem(beer);
            }
        }.execute();
        recreate();
    }
}
