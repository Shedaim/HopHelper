package klevente.hu.hophelper.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.NewBoilingAdapter;
import klevente.hu.hophelper.adapters.NewIngredientsAdapter;
import klevente.hu.hophelper.adapters.NewMashingAdapter;
import klevente.hu.hophelper.constants.Unit;

public class NewBeerActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText styleEditText;
    private EditText batchSizeEditText;
    private EditText descriptionEditText;
    private EditText abvEditText;
    private EditText ogEditText;
    private EditText fgEditText;
    private EditText yeastEditText;

    private EditText maltNameEditText;
    private EditText maltQuantityEditText;
    private Button   addMaltButton;
    private RecyclerView maltsRecyclerView;
    private NewIngredientsAdapter maltsAdapter;

    private EditText hopNameEditText;
    private EditText hopQuantityEditText;
    private Button   addHopButton;
    private RecyclerView hopsRecyclerView;
    private NewIngredientsAdapter hopsAdapter;

    private EditText extraNameEditText;
    private EditText extraQuantityEditText;
    private Button   addextraButton;
    private RecyclerView extrasRecyclerView;
    private NewIngredientsAdapter extrasAdapter;

    private EditText mashTempEditText;
    private EditText mashTimeEditText;
    private Button   addMashTimeButton;
    private RecyclerView mashTimeRecyclerView;
    private NewMashingAdapter mashTimeAdapter;

    private EditText boilNameEditText;
    private EditText boilQuantityEditText;
    private EditText boilTimeEditText;
    private Button   addBoilTimeButton;
    private RecyclerView boilTimeRecyclerView;
    private NewBoilingAdapter boilAdapter;

    private void initRecyclerView(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);
        view.setNestedScrollingEnabled(false);
    }

    private void getContentView() {
        nameEditText = findViewById(R.id.etBeerName);
        styleEditText = findViewById(R.id.etBeerStyle);
        batchSizeEditText = findViewById(R.id.etBeerBatchSize);
        descriptionEditText = findViewById(R.id.etBeerDescription);
        abvEditText = findViewById(R.id.etBeerABV);
        ogEditText = findViewById(R.id.etBeerOG);
        fgEditText = findViewById(R.id.etBeerFG);
        yeastEditText = findViewById(R.id.etBeerYeast);

        maltNameEditText = findViewById(R.id.etBeerMaltName);
        maltQuantityEditText = findViewById(R.id.etBeerMaltQuantity);
        addMaltButton = findViewById(R.id.btnAddMalt);
        maltsRecyclerView = findViewById(R.id.rvNewBeerMalts);
        maltsAdapter = new NewIngredientsAdapter(Unit.KG);
        initRecyclerView(maltsRecyclerView, maltsAdapter);

        hopNameEditText = findViewById(R.id.etBeerHopName);
        hopQuantityEditText = findViewById(R.id.etBeerHopQuantity);
        addHopButton = findViewById(R.id.btnAddHop);
        hopsRecyclerView = findViewById(R.id.rvNewBeerHops);
        hopsAdapter = new NewIngredientsAdapter(Unit.G);
        initRecyclerView(hopsRecyclerView, hopsAdapter);

        extraNameEditText = findViewById(R.id.etBeerExtraName);
        extraQuantityEditText = findViewById(R.id.etBeerExtraQuantity);
        addextraButton = findViewById(R.id.btnAddExtra);
        extrasRecyclerView = findViewById(R.id.rvNewBeerExtras);
        extrasAdapter = new NewIngredientsAdapter(Unit.G);
        initRecyclerView(extrasRecyclerView, extrasAdapter);

        mashTempEditText = findViewById(R.id.etBeerMashTemp);
        mashTimeEditText = findViewById(R.id.etBeerMashTime);
        addMashTimeButton = findViewById(R.id.btnAddMashTime);
        mashTimeRecyclerView = findViewById(R.id.rvNewBeerMashingTemps);
        mashTimeAdapter = new NewMashingAdapter();
        initRecyclerView(mashTimeRecyclerView, mashTimeAdapter);

        boilNameEditText = findViewById(R.id.etBeerHopAdditionName);
        boilQuantityEditText = findViewById(R.id.etBeerHopAdditionQuantity);
        boilTimeEditText = findViewById(R.id.etBeerHopAdditionTime);
        addBoilTimeButton = findViewById(R.id.btnAddBoilTime);
        boilTimeRecyclerView = findViewById(R.id.rvNewBeerHopAdditions);
        boilAdapter = new NewBoilingAdapter();
        initRecyclerView(boilTimeRecyclerView, boilAdapter);
    }

    private EditText isIngredientValid(EditText... text) {
        for (EditText v : text) {
            if (v.getText().toString().isEmpty()) {
                return v;
            }
        }
        return null;
    }

    private void setClickListeners() {
        addMaltButton.setOnClickListener(v -> {
            EditText errorText = isIngredientValid(maltNameEditText, maltQuantityEditText);
            if (errorText == null) {
                try {
                    maltsAdapter.addItem(maltNameEditText.getText().toString(), Double.parseDouble(maltQuantityEditText.getText().toString()));
                } catch (NumberFormatException e) {
                    maltsAdapter.addItem(maltNameEditText.getText().toString(), 0.0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addHopButton.setOnClickListener(v -> {
            EditText errorText = isIngredientValid(hopNameEditText, hopQuantityEditText);
            if (errorText == null) {
                try {
                    hopsAdapter.addItem(hopNameEditText.getText().toString(), Double.parseDouble(hopQuantityEditText.getText().toString()));
                } catch (NumberFormatException e) {
                    hopsAdapter.addItem(hopNameEditText.getText().toString(), 0.0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addextraButton.setOnClickListener(v -> {
            EditText errorText = isIngredientValid(extraNameEditText, extraQuantityEditText);
            if (errorText == null) {
                try {
                    extrasAdapter.addItem(extraNameEditText.getText().toString(), Double.parseDouble(extraQuantityEditText.getText().toString()));
                } catch (NumberFormatException e) {
                    extrasAdapter.addItem(extraNameEditText.getText().toString(), 0.0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabNewBeer);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getContentView();
        setClickListeners();

        fab.hide();
    }

}
