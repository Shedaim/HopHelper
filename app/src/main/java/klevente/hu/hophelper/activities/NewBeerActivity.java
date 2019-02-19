package klevente.hu.hophelper.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.NewIngredientAdapter;
import klevente.hu.hophelper.constants.Unit;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.Ingredient;

public class NewBeerActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText nameEditText;
    private EditText styleEditText;
    private EditText batchSizeEditText;
    private EditText descriptionEditText;
    private EditText abvEditText;
    private EditText ogEditText;
    private EditText fgEditText;
    private EditText yeastEditText;

    private Spinner maltNameEditText;
    private EditText maltQuantityEditText;
    private Button   addMaltButton;
    private NewIngredientAdapter maltsAdapter;

    private Spinner hopNameEditText;
    private EditText hopQuantityEditText;
    private Button   addHopButton;
    private NewIngredientAdapter hopsAdapter;

    private Spinner extraNameEditText;
    private EditText extraQuantityEditText;
    private Button   addextraButton;
    private NewIngredientAdapter extrasAdapter;

    private EditText mashTempEditText;
    private EditText mashTimeEditText;
    private Button   addMashTimeButton;
    private NewIngredientAdapter mashTimeAdapter;

    private Spinner boilNameEditText;
    private EditText boilQuantityEditText;
    private EditText boilTimeEditText;
    private Button   addBoilTimeButton;
    private NewIngredientAdapter boilAdapter;

    private EditText fermentationTempEditText;
    private Spinner fermentationNameEditText;
    private EditText fermentationQuantityEditText;
    private EditText fermentationTimeEditText;
    private Button   addFermentationTimeButton;
    private NewIngredientAdapter fermentationAdapter;

    boolean EDITING = false;
    Beer editedBeer;
    ArrayAdapter<String> adapter;
    String[] list;

    private void initRecyclerView(RecyclerView view, RecyclerView.Adapter adapter) {
        view.setLayoutManager(new LinearLayoutManager(this));
        view.setAdapter(adapter);
        view.setNestedScrollingEnabled(false);
    }

    private void getContentView() {
        ArrayList<String> items_list;

        nameEditText = findViewById(R.id.etBeerName);
        styleEditText = findViewById(R.id.etBeerStyle);
        batchSizeEditText = findViewById(R.id.etBeerBatchSize);
        descriptionEditText = findViewById(R.id.etBeerDescription);
        abvEditText = findViewById(R.id.etBeerABV);
        ogEditText = findViewById(R.id.etBeerOG);
        fgEditText = findViewById(R.id.etBeerFG);
        yeastEditText = findViewById(R.id.etBeerYeast);

        // Malts list choices
        maltNameEditText = findViewById(R.id.etBeerMaltName);
        list = getResources().getStringArray(R.array.malts_array);
        items_list = new ArrayList<>(Arrays.asList(list));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_list);
        adapter.setNotifyOnChange(true);
        adapter.insert(getResources().getString(R.string.select_malt), 0);
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maltNameEditText.setAdapter(adapter);
        maltNameEditText.setOnItemSelectedListener(this);

        maltQuantityEditText = findViewById(R.id.etBeerMaltQuantity);
        addMaltButton = findViewById(R.id.btnAddMalt);
        RecyclerView maltsRecyclerView = findViewById(R.id.rvNewBeerMalts);
        maltsAdapter = new NewIngredientAdapter("ingredient", Unit.KG);
        initRecyclerView(maltsRecyclerView, maltsAdapter);

        // Hops list choices
        hopNameEditText = findViewById(R.id.etBeerHopName);
        list = getResources().getStringArray(R.array.hops_array);
        items_list = new ArrayList<>(Arrays.asList(list));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_list);
        adapter.setNotifyOnChange(true);
        adapter.insert(getResources().getString(R.string.select_hop), 0);
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hopNameEditText.setAdapter(adapter);
        hopNameEditText.setOnItemSelectedListener(this);

        hopQuantityEditText = findViewById(R.id.etBeerHopQuantity);
        addHopButton = findViewById(R.id.btnAddHop);
        RecyclerView hopsRecyclerView = findViewById(R.id.rvNewBeerHops);
        hopsAdapter = new NewIngredientAdapter("ingredient", Unit.G);
        initRecyclerView(hopsRecyclerView, hopsAdapter);

        // Extras list choices
        extraNameEditText = findViewById(R.id.etBeerExtraName);
        list = getResources().getStringArray(R.array.extras_array);
        items_list = new ArrayList<>(Arrays.asList(list));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_list);
        adapter.setNotifyOnChange(true);
        adapter.insert(getResources().getString(R.string.select_extra), 0);
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        extraNameEditText.setAdapter(adapter);
        extraNameEditText.setOnItemSelectedListener(this);
        extraQuantityEditText = findViewById(R.id.etBeerExtraQuantity);
        addextraButton = findViewById(R.id.btnAddExtra);
        RecyclerView extrasRecyclerView = findViewById(R.id.rvNewBeerExtras);
        extrasAdapter = new NewIngredientAdapter("ingredient", Unit.G);
        initRecyclerView(extrasRecyclerView, extrasAdapter);

        mashTempEditText = findViewById(R.id.etBeerMashTemp);
        mashTimeEditText = findViewById(R.id.etBeerMashTime);
        addMashTimeButton = findViewById(R.id.btnAddMashTime);
        RecyclerView mashTimeRecyclerView = findViewById(R.id.rvNewBeerMashingTemps);
        mashTimeAdapter = new NewIngredientAdapter("mash");
        initRecyclerView(mashTimeRecyclerView, mashTimeAdapter);

        // Hop list choices
        boilNameEditText = findViewById(R.id.etBeerHopAdditionName);
        list = getResources().getStringArray(R.array.hops_array);
        items_list = new ArrayList<>(Arrays.asList(list));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_list);
        adapter.setNotifyOnChange(true);
        adapter.insert(getResources().getString(R.string.select_hop), 0);
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        boilNameEditText.setAdapter(adapter);
        boilNameEditText.setOnItemSelectedListener(this);

        boilQuantityEditText = findViewById(R.id.etBeerHopAdditionQuantity);
        boilTimeEditText = findViewById(R.id.etBeerHopAdditionTime);
        addBoilTimeButton = findViewById(R.id.btnAddBoilTime);
        RecyclerView boilTimeRecyclerView = findViewById(R.id.rvNewBeerHopAdditions);
        boilAdapter = new NewIngredientAdapter("boil");
        initRecyclerView(boilTimeRecyclerView, boilAdapter);

        // Fermentation list choices
        fermentationNameEditText = findViewById(R.id.etBeerDryHopAdditionName);
        list = getResources().getStringArray(R.array.hops_array);
        items_list = new ArrayList<>(Arrays.asList(list));
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items_list);
        adapter.setNotifyOnChange(true);
        adapter.insert(getResources().getString(R.string.select_hop), 0);
        adapter.notifyDataSetChanged();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fermentationNameEditText.setAdapter(adapter);
        fermentationNameEditText.setOnItemSelectedListener(this);

        fermentationQuantityEditText = findViewById(R.id.etBeerDryHopAdditionQuantity);
        fermentationTimeEditText = findViewById(R.id.etBeerDryHopAdditionTime);
        fermentationTempEditText = findViewById(R.id.etBeerDryHopAdditionTemp);
        addFermentationTimeButton = findViewById(R.id.btnAddFermentationTime);
        RecyclerView fermentationTimeRecyclerView = findViewById(R.id.rvNewBeerDryHopAdditions);
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
            EditText errorText = isEditTextValid(maltQuantityEditText);
            if (errorText == null) {
                try {
                    maltsAdapter.addItem(maltNameEditText.getSelectedItem().toString(), Float.parseFloat(maltQuantityEditText.getText().toString()),0,0);
                } catch (NumberFormatException e) {
                    maltsAdapter.addItem(maltNameEditText.getSelectedItem().toString(), 0,0,0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addHopButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(hopQuantityEditText);
            if (errorText == null) {
                try {
                    hopsAdapter.addItem(hopNameEditText.getSelectedItem().toString(), Float.parseFloat(hopQuantityEditText.getText().toString()),0,0);
                } catch (NumberFormatException e) {
                    hopsAdapter.addItem(hopNameEditText.getSelectedItem().toString(), 0, 0,0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addextraButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(extraQuantityEditText);
            if (errorText == null) {
                try {
                    extrasAdapter.addItem(extraNameEditText.getSelectedItem().toString(), Float.parseFloat(extraQuantityEditText.getText().toString()),0 ,0);
                } catch (NumberFormatException e) {
                    extrasAdapter.addItem(extraNameEditText.getSelectedItem().toString(), 0, 0,0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addMashTimeButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(mashTimeEditText);
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
            if (boilNameEditText.getSelectedItem().toString().isEmpty()){
                try {
                    boilAdapter.addItem("No hops", Float.parseFloat(" 0 "), Long.parseLong(boilTimeEditText.getText().toString()), 100);
                }
                catch (NumberFormatException f) {
                    boilAdapter.addItem(boilNameEditText.getSelectedItem().toString(), 0, 0, 100);
                }
            }
            else if (errorText == null) {
                try {
                    boilAdapter.addItem(boilNameEditText.getSelectedItem().toString(), Float.parseFloat(boilQuantityEditText.getText().toString()), Long.parseLong(boilTimeEditText.getText().toString()), 100);
                } catch (NumberFormatException e) {
                    boilAdapter.addItem(boilNameEditText.getSelectedItem().toString(), 0, 0, 100);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });

        addFermentationTimeButton.setOnClickListener(v -> {
            EditText errorText = isEditTextValid(fermentationQuantityEditText, fermentationTimeEditText, fermentationTempEditText);
            if (fermentationNameEditText.getSelectedItem().toString().isEmpty()){
                try {
                    fermentationAdapter.addItem("No hops", Float.parseFloat(" 0 "), Long.parseLong(fermentationTimeEditText.getText().toString()), Integer.parseInt(fermentationTempEditText.getText().toString()));
                }
                catch (NumberFormatException f) {
                    fermentationAdapter.addItem(fermentationNameEditText.getSelectedItem().toString(), 0, 0, 0);
                }
            }
            else if (errorText == null) {
                try {
                    fermentationAdapter.addItem(fermentationNameEditText.getSelectedItem().toString(), Float.parseFloat(fermentationQuantityEditText.getText().toString()), Long.parseLong(fermentationTimeEditText.getText().toString()), Integer.parseInt(fermentationTempEditText.getText().toString()));
                } catch (NumberFormatException e) {
                    fermentationAdapter.addItem(fermentationNameEditText.getSelectedItem().toString(), 0, 0, 0);
                }
            } else {
                errorText.setError(getString(R.string.must_not_be_empty));
            }
        });
    }

    private boolean isListsValid() {
        if (maltsAdapter.getItemCount() == 0) {
            //maltNameEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        if (hopsAdapter.getItemCount() == 0) {
            //hopNameEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        if (mashTimeAdapter.getItemCount() == 0) {
            //mashTimeEditText.setError(getString(R.string.at_least_one_element));
            return false;
        }
        if (boilAdapter.getItemCount() == 0) {
            //boilNameEditText.setError(getString(R.string.at_least_one_element));
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

    private void doEditTextFromOldBeer(){
        nameEditText.setText(editedBeer.name);
        styleEditText.setText(editedBeer.style);
        descriptionEditText.setText(editedBeer.description);
        batchSizeEditText.setText(editedBeer.batchSize.toString());
        abvEditText.setText(editedBeer.abv.toString());
        ogEditText.setText(editedBeer.og.toString());
        fgEditText.setText(editedBeer.fg.toString());
        yeastEditText.setText(editedBeer.yeast);

        for (Map.Entry<String, Float> entry : editedBeer.malts.entrySet()) {
            maltsAdapter.addItem(entry.getKey(), Float.parseFloat(String.valueOf(entry.getValue())),0,0);
        }

        for (Map.Entry<String, Float> entry : editedBeer.hops.entrySet()) {
            hopsAdapter.addItem(entry.getKey(), Float.parseFloat(String.valueOf(entry.getValue())),0,0);
        }

        for (Map.Entry<String, Float> entry : editedBeer.extras.entrySet()) {
            extrasAdapter.addItem(entry.getKey(), Float.parseFloat(String.valueOf(entry.getValue())),0,0);
        }

        for (int i=0; i<editedBeer.mashingTimes.size(); i++) {
            Ingredient item = editedBeer.mashingTimes.get(i);
            mashTimeAdapter.addItem("", 0, TimeUnit.MILLISECONDS.toMinutes(item.time), Float.parseFloat(String.valueOf(item.temp)));
        }

        for (int i=0; i<editedBeer.boilingTimes.size(); i++) {
            Ingredient item = editedBeer.boilingTimes.get(i);
            boilAdapter.addItem(item.name, Float.parseFloat(String.valueOf(item.quantity)), TimeUnit.MILLISECONDS.toMinutes(item.time), 100);
        }

        for (int i=0; i<editedBeer.fermentationTimes.size(); i++) {
            Ingredient item = editedBeer.fermentationTimes.get(i);
            fermentationAdapter.addItem(item.name, Float.parseFloat(String.valueOf(item.quantity)), Long.parseLong(String.valueOf(item.time)), Float.parseFloat(String.valueOf(item.temp)));
        }

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
            editedBeer = (Beer) extras.getSerializable("beer");
            doEditTextFromOldBeer();
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
        newBeer = getBeer();
        Intent result = new Intent();

        if(EDITING){
            EDITING = false;
            result.putExtra("old_beer", editedBeer);
        }

        switch (item.getItemId()) {
            case R.id.action_add:
                if (newBeer != null) {

                    result.putExtra("beer", newBeer);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                }
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position==1) {
            // Set an EditText view to get user input
            final EditText input = new EditText(this);

            new AlertDialog.Builder(this)
                    .setMessage("Enter your product here")
                    .setView(input)
                    .setPositiveButton("Ok", (dialog, whichButton) -> {
                        Editable editable = input.getText();
                        String newString = editable.toString();
                        adapter.setNotifyOnChange(true);
                        adapter.add(newString);
                        adapter.notifyDataSetChanged();
                        boilNameEditText.setSelection(adapter.getPosition(newString));
                     })
                    .setNegativeButton("Cancel", (dialog, whichButton) -> {
                        // Do nothing.
                    }).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
