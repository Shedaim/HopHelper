package klevente.hu.hophelper.fragments;

/*import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;

public class NewBeerDialogFragment extends DialogFragment {

    public static final String TAG = "NewBeerDialogFragment";

    public interface NewBeerDialogListener {
        void onNewBeerCreated(Beer beer);
    }

    private NewBeerDialogListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewBeerDialogListener) {
            listener = (NewBeerDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement NewBeerDialogListener!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_beer)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    if (isValid()) {
                        listener.onNewBeerCreated(getBeer());
                    }
                }).setNegativeButton(R.string.cancel, null).create();
    }

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText styleEditText;
    private EditText abvEditText;
    private EditText ogEditText;
    private EditText fgEditText;

    private View getContentView() {
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_beer, null);
        nameEditText = contentView.findViewById(R.id.etBeerName);
        descriptionEditText = contentView.findViewById(R.id.etBeerDescription);
        styleEditText = contentView.findViewById(R.id.etBeerStyle);
        abvEditText = contentView.findViewById(R.id.etBeerABV);
        ogEditText = contentView.findViewById(R.id.etBeerOG);
        fgEditText = contentView.findViewById(R.id.etBeerFG);

        return contentView;
    }

    private boolean isValid() {
        return true;
    }

    private Beer getBeer() {
        Beer item = new Beer();
        item.name = nameEditText.getText().toString();
        item.description = descriptionEditText.getText().toString();
        item.style = styleEditText.getText().toString();
        try {
            item.abv = Double.parseDouble(abvEditText.getText().toString());
        } catch (NumberFormatException e) {
            item.abv = 0.0;
        }
        try {
            item.og = Integer.parseInt(ogEditText.getText().toString());
        } catch (NumberFormatException e) {
            item.og = 1000;
        }
        try {
            item.fg = Integer.parseInt(fgEditText.getText().toString());
        } catch (NumberFormatException e) {
            item.fg = 1000;
        }


        return item;
    }
}*/
