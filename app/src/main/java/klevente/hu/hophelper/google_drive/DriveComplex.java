package klevente.hu.hophelper.google_drive;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.api.services.drive.model.File;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicBoolean;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.activities.MainActivity;
import klevente.hu.hophelper.data.Beer;



public class DriveComplex {

    private static Gson gson;
    private static String fileContent;

    private static void createAndSaveFile(DriveServiceHelper mDriveServiceHelper, Beer beer, String folderId, View viewPager, String TAG, Context context) {
        if (mDriveServiceHelper != null && beer != null) {
            mDriveServiceHelper.createFile(folderId)
                    .addOnSuccessListener(fileId -> {
                        beer.file_id = fileId;
                        String fileName;
                        if (beer.version != 1) fileName = beer.name+ "-" + beer.version + ".json";
                        else fileName = beer.name + ".json";
                        new AsyncTask<Void, Void, Long>() {
                            @Override
                            protected Long doInBackground(Void... voids) {
                                MainActivity.database.beerDao().update(beer);
                                fileContent = gson.toJson(beer);
                                return beer.id;
                            }

                            @Override
                            protected void onPostExecute(Long aLong) {
                                mDriveServiceHelper.saveFile(fileId, fileName, fileContent)
                                    .addOnSuccessListener(name -> Snackbar.make(viewPager, context.getString(R.string.successful_save, name), Snackbar.LENGTH_SHORT).show())
                                    .addOnFailureListener(exception -> {
                                        Log.e(TAG, "Unable to save file to Drive.", exception);
                                        Snackbar.make(viewPager, context.getString(R.string.failed_to_save, fileName), Snackbar.LENGTH_SHORT).show();
                                    });
                            }
                        }.execute();
                    })
                    .addOnFailureListener(exception -> {
                        Log.e(TAG, "Unable to create a new file.", exception);
                        Snackbar.make(viewPager, context.getString(R.string.failed_to_save, beer.name), Snackbar.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Saves a beer object into a new json file
     */
    public static void saveBeerToFile(DriveServiceHelper mDriveServiceHelper, Beer beer, String folderId, View viewPager, String TAG, Context context) {
        if (mDriveServiceHelper != null && beer != null) {
            Log.d(TAG, "Querying existing files");
            AtomicBoolean duplicate_file = new AtomicBoolean(false);
            mDriveServiceHelper.queryFiles()
                    .addOnSuccessListener(fileList -> {
                        String fileName;
                        if (beer.version != 1) fileName = beer.name+ "-" + beer.version + ".json";
                        else fileName = beer.name + ".json";
                        for (File f : fileList.getFiles()) {
                            if (f.getName().equals(fileName)) {
                                Log.d(TAG, "Found exiting file with the same name");
                                duplicate_file.set(true);
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.save_file_dialog);
                                dialog.setTitle("Save File to Drive...");

                                Button saveAndReplace = dialog.findViewById(R.id.btn_save_and_replace);
                                Button saveAndKeep = dialog.findViewById(R.id.btn_save_and_keep);
                                Button cancel = dialog.findViewById(R.id.btn_cancel);
                                gson = new Gson();

                                saveAndReplace.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                    beer.file_id = f.getId();
                                    fileContent = gson.toJson(beer);
                                    mDriveServiceHelper.saveFile(f.getId(), fileName, fileContent)
                                            .addOnSuccessListener(name -> Snackbar.make(viewPager, context.getString(R.string.successful_save, name), Snackbar.LENGTH_SHORT).show())
                                            .addOnFailureListener(exception -> {
                                                Log.e(TAG, "Unable to save file to Drive.", exception);
                                                Snackbar.make(viewPager, context.getString(R.string.failed_to_save, fileName), Snackbar.LENGTH_SHORT).show();
                                            });
                                });

                                saveAndKeep.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                    beer.version += 1;
                                    fileContent = gson.toJson(beer);
                                    createAndSaveFile(mDriveServiceHelper, beer, folderId, viewPager, TAG, context);
                                });

                                cancel.setOnClickListener(v1 -> {
                                    dialog.dismiss();
                                    Snackbar.make(viewPager, "Save to drive canceled by user", Snackbar.LENGTH_SHORT).show();
                                });

                                dialog.show();
                                break;
                            }
                        }
                        if (!duplicate_file.get()) {
                            createAndSaveFile(mDriveServiceHelper, beer, folderId, viewPager, TAG, context);
                        }
                    })
                    .addOnFailureListener(exception -> Log.d(TAG, "Failed querying files" + exception));
        }
    }
}

