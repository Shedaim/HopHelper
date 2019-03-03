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
import klevente.hu.hophelper.google_drive.DriveServiceHelper;
import klevente.hu.hophelper.services.BoilingCountdownService;
import klevente.hu.hophelper.services.MashingCountdownService;

public class BeerDetailActivity extends AppCompatActivity {
    private static final int EDIT_BEER = 101;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_OPEN_DOCUMENT = 2;
    public static final String BEER_INDEX = "index";

    String TAG = "BeerDetail";
    String fileName, fileContent;
    FileList file_list;
    private Beer beer;
    private int beerIdx;
    private DriveServiceHelper mDriveServiceHelper;
    private volatile String mOpenFileId;
    ViewPager viewPager;

    @Override
    protected void onStart() {
        super.onStart();
        requestSignIn();
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
                })
                .addOnFailureListener(exception -> Log.e(TAG, "Unable to sign in.", exception));
    }

    /**
     * Opens the Storage Access Framework file picker using {@link #REQUEST_CODE_OPEN_DOCUMENT}.
     */
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
     * initiated by {@link #openFilePicker()}.

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

    /**
     * Saves the currently opened file created via {@link #createFile()} if one exists.
     */
    private void saveFile(String fileName, String fileContent) {
        if (mDriveServiceHelper != null && fileName != null && fileContent != null) {
            Log.d(TAG, "Saving " + mOpenFileId);
            mDriveServiceHelper.saveFile(mOpenFileId, fileName, fileContent)
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Unable to save file via REST.", exception));
        }
    }

    /**
     * Queries the Drive REST API for files visible to this app and lists them in the content view.
     */
    private void query() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Querying for files.");
            mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> file_list = fileList)
                    .addOnFailureListener(exception -> Log.e(TAG, "Unable to query files.", exception));
        }
    }

    /**
     * Retrieves the title and content of a file identified by {@code fileId} and populates the UI.
     */
    private void readFile(String fileId) {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Reading file " + fileId);

            mDriveServiceHelper.readFile(fileId)
                    .addOnSuccessListener(nameAndContent -> {
                        fileName = nameAndContent.first;
                        fileContent = nameAndContent.second;
                        mOpenFileId = fileId;
                    })
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't read file.", exception));
        }
    }

    /**
     * Creates a new file via the Drive REST API.
     */
    private void createFile() {
        if (mDriveServiceHelper != null) {
            Log.d(TAG, "Creating a file.");
            mDriveServiceHelper.createFile()
                    .addOnSuccessListener(this::readFile)
                    .addOnFailureListener(exception ->
                            Log.e(TAG, "Couldn't create file.", exception));
        }
    }

    /**
     * Updates the UI to read-only mode.
     */
    private void setReadOnlyMode() {
        //mFileTitleEditText.setEnabled(false);
        //mDocContentEditText.setEnabled(false);
        mOpenFileId = null;
    }

    /**
     * Updates the UI to read/write mode on the document identified by {@code fileId}.
     */
    private void setReadWriteMode(String fileId) {
        //mFileTitleEditText.setEnabled(true);
        //mDocContentEditText.setEnabled(true);
        mOpenFileId = fileId;
    }
    /*
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

    */


    /*
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
            createFile();
            fileName = beer.name + ".json";
            Gson gson = new Gson();
            String fileContent = gson.toJson(beer);
            query();
            Log.d(TAG, String.valueOf(file_list));
            if (file_list.isEmpty()){
                saveFile(fileName, fileContent);
            }
            else{
                for (File file : file_list.getFiles()){
                    // Check if file already exists
                    if (file.getName().equals(fileName)){
                        final Dialog dialog = new Dialog(getApplicationContext());
                        dialog.setContentView(R.layout.save_file_dialog);
                        dialog.setTitle("Save File to Drive...");

                        Button saveAndReplace = dialog.findViewById(R.id.btn_save_and_replace);
                        Button saveAndKeep = dialog.findViewById(R.id.btn_save_and_keep);
                        Button cancel = dialog.findViewById(R.id.btn_cancel);
                        // if button is clicked, close the custom dialog
                        //TODO Add option for user to save on top of old item or update version or cancel
                        saveAndReplace.setOnClickListener(v1 -> {
                            Snackbar.make(v1, R.string.not_implemented, Snackbar.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                        saveAndKeep.setOnClickListener(v1 -> {
                            saveFile(fileName, fileContent);
                            Snackbar.make(v1, R.string.not_implemented, Snackbar.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                        cancel.setOnClickListener(v1 -> {
                            Snackbar.make(v1, "Save to drive canceled", Snackbar.LENGTH_SHORT).show();
                            dialog.dismiss();
                        });
                        dialog.show();
                        break;
                    }
                }
            }
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
            case REQUEST_CODE_SIGN_IN:
                Log.d(TAG, "ResultCode: " + String.valueOf(resultCode) + " Result_OK: " + String.valueOf(Activity.RESULT_OK) );
                if (resultCode == Activity.RESULT_OK && data != null) {
                    handleSignInResult(data);
                }
                break;

            case REQUEST_CODE_OPEN_DOCUMENT:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        //openFileFromFilePicker(uri);
                    }
                }
                break;
        }
    }
}
