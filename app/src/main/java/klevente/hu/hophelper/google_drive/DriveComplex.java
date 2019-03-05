package klevente.hu.hophelper.google_drive;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.services.drive.model.File;
import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;



public class DriveComplex {

    private static final Executor mExecutor = Executors.newSingleThreadExecutor();
    private static String fileID = "";


    public static String createAndSaveFile(DriveServiceHelper mDriveServiceHelper, final String fileName, String folderId, final String fileContent, View viewPager, String TAG, Context context) {
        if (mDriveServiceHelper != null && fileName != null && fileContent != null) {
            mDriveServiceHelper.createFile(folderId)
                    .addOnSuccessListener(fileId -> {
                        mDriveServiceHelper.saveFile(fileId, fileName, fileContent)
                                .addOnSuccessListener(name -> Snackbar.make(viewPager, context.getString(R.string.successful_save, name), Snackbar.LENGTH_SHORT).show())
                                .addOnFailureListener(exception -> {
                                    Log.e(TAG, "Unable to save file to Drive.", exception);
                                    Snackbar.make(viewPager, context.getString(R.string.failed_to_save, fileName), Snackbar.LENGTH_SHORT).show();
                                });
                        fileID = fileId;
                    })
                    .addOnFailureListener(exception -> {
                        Log.e(TAG, "Unable to create a new file.", exception);
                        Snackbar.make(viewPager, context.getString(R.string.failed_to_save, fileName), Snackbar.LENGTH_SHORT).show();
                    });
            return fileID;
        }
        return null;
    }

    /**
     * Saves a beer object into a new json file
     *
     * @param fileName
     * @param fileContent
     */
    public static Task<String> saveBeerToFile(DriveServiceHelper mDriveServiceHelper, Beer beer, final String fileName, String folderId, final String fileContent, View viewPager, String TAG, Context context) {
        return Tasks.call(mExecutor, () -> {
            if (mDriveServiceHelper != null && fileName != null && fileContent != null) {
                Log.d(TAG, "Querying existing files");
                AtomicBoolean duplicate_file = new AtomicBoolean(false);
                mDriveServiceHelper.queryFiles()
                        .addOnSuccessListener(fileList -> {
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

                                    saveAndReplace.setOnClickListener(v1 -> {
                                        dialog.dismiss();
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
                                        Gson gson = new Gson();
                                        String newFileContent = gson.toJson(beer);
                                        String newFileName = beer.name + "-" + beer.version + ".json";
                                        fileID = createAndSaveFile(mDriveServiceHelper, newFileName, folderId, newFileContent, viewPager, TAG, context);
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
                                fileID = createAndSaveFile(mDriveServiceHelper, fileName, folderId, fileContent, viewPager, TAG, context);
                            }
                        })
                        .addOnFailureListener(exception -> Log.d(TAG, "Failed querying files" + exception));
            }
            return fileID;
        });
    }
}

