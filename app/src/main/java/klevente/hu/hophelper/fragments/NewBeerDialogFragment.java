package klevente.hu.hophelper.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import klevente.hu.hophelper.R;

public class NewBeerDialogFragment extends DialogFragment {

    public static final String TAG = "NewBeerDialogFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_beer)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    // TODO
                }).setNegativeButton(R.string.cancel, null).create();
    }

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_beer, null);

        return contentView;
    }
}
