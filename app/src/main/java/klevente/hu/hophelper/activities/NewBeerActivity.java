package klevente.hu.hophelper.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.NewIngredientAdapter;
import klevente.hu.hophelper.constants.Unit;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.Ingredient;

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
    private NewIngredientAdapter maltsAdapter;

    private EditText hopNameEditText;
    private EditText hopQuantityEditText;
    private Button   addHopButton;
    private RecyclerView hopsRecyclerView;
    private NewIngredientAdapter hopsAdapter;

    private EditText extraNameEditText;
    private EditText extraQuantityEditText;
    private Button   addextraButton;
    private RecyclerView extrasRecyclerView;
    private NewIngredientAdapter extrasAdapter;

    private EditText mashTempEditText;
    private EditText mashTimeEditText;
    private Button   addMashTimeButton;
    private RecyclerView mashTimeRecyclerView;
    private NewIngredientAdapter mashTimeAdapter;

    private EditText boilNameEditText;
    private EditText boilQuantityEditText;
    private EditText boilTimeEditText;
    private Button   addBoilTimeButton;
    private RecyclerView boilTimeRecyclerView;
    private NewIngredientAdapter boilAdapter;

    private EditText fermentationTempEditText;
    private EditText fermentationNameEditText;
    private EditText fermentationQuantityEditText;
    private EditText fermentationTimeEditText;
    private Button   addFermentationTimeButton;
    private RecyclerView fermentationTimeRecyclerView;
    private NewIngredientAdapter fermentationAdapter;

    boolean EDITING = false;
    Beer editedBeer;

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
        maltsAdapter = new NewIngredientAdapter("ingredient", Unit.KG);
        initRecyclerView(maltsRecyclerView, maltsAdapter);

        hopNameEditText = findViewById(R.id.etBeerHopName);
        hopQuantityEditText = findViewById(R.id.etBeerHopQuantity);
        addHopButton = findViewById(R.id.btnAddHop);
        hopsRecyclerView = findViewById(R.id.rvNewBeerHops);
        hopsAdapter = new NewIngredientAdapter("ingredient", Unit.G);
        initRecyclerView(hopsRecyclerView, hopsAdapter);

        extraNameEditText = findViewById(R.id.etBeerExtraName);
        extraQuantityEditText = findViewById(R.id.etBeerExtraQuantity);
        addextraButton = findViewById(R.id.btnAddExtra);
        extrasRecyclerView = findViewById(R.id.rvNewBeerExtras);
        extrasAdapter = new NewIngredientAdapter("ingredient", Unit.G);
        initRecyclerView(extrasRecyclerView, extrasAdapter);

        mashTempEditText = findViewById(R.id.etBeerMashTemp);
        mashTimeEditText = findViewById(R.id.etBeerMashTime);
        addMashTimeButton = findViewById(R.id.btnAddMashTime);
        mashTimeRecyclerView = findViewById(R.id.rvNewBeerMashingTemps);
        mashTimeAdapter = new NewIngredientAdapter("mash");
        initRecyclerView(mashTimeRecyclerView, mashTimeAdapter);

        boilNameEditText = findViewById(R.id.etBeerHopAdditionName);
        boilQuantityEditText = findViewById(R.id.etBeerHopAdditionQuantity);
        boilTimeEditText = findViewById(R.id.etBeerHopAdditionTime);
        addBoilTimeButton = findViewById(R.id.btnAddBoilTime);
        boilTimeRecyclerView = findViewById(R.id.rvNewBeerHopAdditions);
        boilAdapter = new NewIngredientAdapter("boil");
        initRecyclerView(boilTimeRecyclerView, boilAdapter);

        fermentationNameEditText = findViewById(R.id.etBeerDryHopAdditionName);
        fermentationQuantityEditText = findViewById(R.id.etBeerDryHopAdditionQuantity);
        fermentationTimeEditText = findViewById(R.id.etBeerDryHopAdditionTime);
        fermentationTempEditText = findViewById(R.id.etBeerDryHopAdditionTemp);
        addFermentationTimeButton = findViewById(R.id.btnAddFermentationTime);
        fermentationTimeRecyclerView = findViewById(R.id.rvNewBeerDryHopAdditions);
        fermentationAdapter = new NewIngredientAdapter("fermentation");
        initRecyclerView(fermentationTimeRecyclerView, fermentationAdapter);
    }

    private EditText isEditTextValid(EditText... text) {
        for (EditText v : text) {
            if (v.getText().toString().isEmpty()) {
                return v;
            }
        }
        return null;
    }

    private void setClickListeners() {
        addMaltButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(maltNameEditText, maltQuantityEditText);
            if (errorText == null) {
                try {
                    maltsAdapter.addItem(maltNameEditText.getText().toString(), Float.parseFloat(maltQuantityEditText.getText().toString()),0,0);
                } catch (NumberFormatException e) {
                    maltsAdapter.addItem(maltNameEditText.getText().toString(), 0,0,0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addHopButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(hopNameEditText, hopQuantityEditText);
            if (errorText == null) {
                try {
                    hopsAdapter.addItem(hopNameEditText.getText().toString(), Float.parseFloat(hopQuantityEditText.getText().toString()),0,0);
                } catch (NumberFormatException e) {
                    hopsAdapter.addItem(hopNameEditText.getText().toString(), 0, 0,0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addextraButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(extraNameEditText, extraQuantityEditText);
            if (errorText == null) {
                try {
                    extrasAdapter.addItem(extraNameEditText.getText().toString(), Float.parseFloat(extraQuantityEditText.getText().toString()),0 ,0);
                } catch (NumberFormatException e) {
                    extrasAdapter.addItem(extraNameEditText.getText().toString(), 0, 0,0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addMashTimeButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(mashTempEditText, mashTimeEditText);
            if (errorText == null) {
                try {
                    mashTimeAdapter.addItem("", 0, Long.parseLong(mashTimeEditText.getText().toString()), Float.parseFloat(mashTempEditText.getText().toString()));
                } catch (NumberFormatException e) {
                    mashTimeAdapter.addItem("", 0, 0, 0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addBoilTimeButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(boilTimeEditText);
            if (boilNameEditText.getText().toString().isEmpty()){
                try {
                    boilAdapter.addItem("No hops", Float.parseFloat(" 0 "), Long.parseLong(boilTimeEditText.getText().toString()), 100);
                }
                catch (NumberFormatException f) {
                    boilAdapter.addItem(boilNameEditText.getText().toString(), 0, 0, 100);
                }
            }
            else if (errorText == null) {
                try {
                    boilAdapter.addItem(boilNameEditText.getText().toString(), Float.parseFloat(boilQuantityEditText.getText().toString()), Long.parseLong(boilTimeEditText.getText().toString()), 100);
                } catch (NumberFormatException e) {
                    boilAdapter.addItem(boilNameEditText.getText().toString(), 0, 0, 100);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addFermentationTimeButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(fermentationNameEditText, fermentationQuantityEditText, fermentationTimeEditText, fermentationTempEditText);
            if (fermentationNameEditText.getText().toString().isEmpty()){
                try {
                    fermentationAdapter.addItem("No hops", Float.parseFloat(" 0 "), Long.parseLong(fermentationTimeEditText.getText().toString()), Integer.parseInt(fermentationTempEditText.getText().toString()));
                }
                catch (NumberFormatException f) {
                    fermentationAdapter.addItem(fermentationNameEditText.getText().toString(), 0, 0, 0);
                }
            }
            else if (errorText == null) {
                try {
                    fermentationAdapter.addItem(fermentationNameEditText.getText().toString(), Float.parseFloat(fermentationQuantityEditText.getText().toString()), Long.parseLong(fermentationTimeEditText.getText().toString()), Integer.parseInt(fermentationTempEditText.getText().toString()));
                } catch (NumberFormatException e) {
                    fermentationAdapter.addItem(fermentationNameEditText.getText().toString(), 0, 0, 0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });
    }

    private boolean isListsValid() {
        if (maltsAdapter.getItemCount() == 0) {
            maltNameEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        if (hopsAdapter.getItemCount() == 0) {
            hopNameEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        if (mashTimeAdapter.getItemCount() == 0) {
            mashTimeEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        if (boilAdapter.getItemCount() == 0) {
            boilNameEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        return true;
    }

    private Beer getBeer() {
        EditText errorText = isEditTextValid(nameEditText, styleEditText, batchSizeEditText, descriptionEditText, abvEditText, ogEditText, fgEditText, yeastEditText);
        if (errorText == null) {
            if (isListsValid()) {
                Beer beer = new Beer();
                beer.name = nameEditText.getText().toString();
                beer.style = styleEditText.getText().toString();
                beer.batchSize = Double.parseDouble(batchSizeEditText.getText().toString());
                beer.description = descriptionEditText.getText().toString();
                beer.abv = Double.parseDouble(abvEditText.getText().toString());
                beer.og = Integer.parseInt(ogEditText.getText().toString());
                beer.fg = Integer.parseInt(fgEditText.getText().toString());
                beer.yeast = yeastEditText.getText().toString();
                beer.malts = maltsAdapter.getIngredientMap();
                beer.hops = hopsAdapter.getIngredientMap();
                beer.extras = extrasAdapter.getIngredientMap();
                beer.mashingTimes = mashTimeAdapter.getMashTimeList();
                beer.boilingTimes = boilAdapter.getHopAdditionList();
                beer.fermentationTimes = fermentationAdapter.getFermentationTimeList();
                return beer;
            }
        } else {
            errorText.setError(getString(R.string.must_not_be_empty));
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getContentView();
        setClickListeners();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            EDITING = true;
            editedBeer = new Beer();
            editedBeer.name = extras.get("name").toString();
            nameEditText.setText(editedBeer.name);
            editedBeer.style = extras.get("style").toString();
            styleEditText.setText(editedBeer.style);
            editedBeer.description = extras.get("description").toString();
            descriptionEditText.setText(editedBeer.description);
            editedBeer.batchSize = (Double) extras.get("batchsize");
            batchSizeEditText.setText(editedBeer.batchSize.toString());
            editedBeer.abv = (Double) extras.get("abv");
            abvEditText.setText(editedBeer.abv.toString());
            editedBeer.og = (Integer) extras.get("og");
            ogEditText.setText(editedBeer.og.toString());
            editedBeer.fg = (Integer) extras.get("fg");
            fgEditText.setText(editedBeer.fg.toString());
            editedBeer.yeast = extras.get("yeast").toString();
            yeastEditText.setText(editedBeer.yeast);

            HashMap<String, Float> malts = (HashMap<String, Float>) extras.getSerializable("malts");
            for (Map.Entry<String, Float> entry : malts.entrySet()) {
                maltsAdapter.addItem(entry.getKey(), Float.parseFloat(String.valueOf(entry.getValue())),0,0);
            }
            editedBeer.malts = maltsAdapter.getIngredientMap();

            HashMap<String, Float> hops = (HashMap<String, Float>) extras.getSerializable("hops");
            for (Map.Entry<String, Float> entry : hops.entrySet()) {
                hopsAdapter.addItem(entry.getKey(), Float.parseFloat(String.valueOf(entry.getValue())),0,0);
            }
            editedBeer.hops = hopsAdapter.getIngredientMap();

            HashMap<String, Float> _extras = (HashMap<String, Float>) extras.getSerializable("extras");
            for (Map.Entry<String, Float> entry : _extras.entrySet()) {
                extrasAdapter.addItem(entry.getKey(), Float.parseFloat(String.valueOf(entry.getValue())),0,0);
            }
            editedBeer.extras = extrasAdapter.getIngredientMap();

            List<Ingredient> mashing_times =  (List<Ingredient>) extras.getSerializable("mashingTimes");
            for (int i=0; i<mashing_times.size(); i++) {
                Ingredient item = mashing_times.get(i);
                mashTimeAdapter.addItem("", 0, Long.parseLong(String.valueOf(item.time)), Float.parseFloat(String.valueOf(item.temp)));
            }
            editedBeer.mashingTimes = mashTimeAdapter.getMashTimeList();

            List<Ingredient> boil_times =  (List<Ingredient>) extras.getSerializable("boilTimes");
            for (int i=0; i<boil_times.size(); i++) {
                Ingredient item = boil_times.get(i);
                boilAdapter.addItem(item.name, Float.parseFloat(String.valueOf(item.quantity)), Long.parseLong(String.valueOf(item.time)), 100);
            }
            editedBeer.boilingTimes = boilAdapter.getHopAdditionList();

            List<Ingredient> ferment_times =  (List<Ingredient>) extras.getSerializable("fermentationTimes");
            for (int i=0; i<ferment_times.size(); i++) {
                Ingredient item = ferment_times.get(i);
                fermentationAdapter.addItem(item.name, Float.parseFloat(String.valueOf(item.quantity)), Long.parseLong(String.valueOf(item.time)), Float.parseFloat(String.valueOf(item.temp)));
            }
            editedBeer.fermentationTimes = fermentationAdapter.getFermentationTimeList();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_beer, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Beer newBeer;
        if (EDITING){
            newBeer = editedBeer;
            EDITING = false;
        }
        else {
            newBeer = getBeer();
        }
        switch (item.getItemId()) {
            case R.id.action_add:
                if (newBeer != null) {
                    Intent result = new Intent();
                    result.putExtra("beer", newBeer);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

}
