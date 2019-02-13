package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.constants.Unit;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.Ingredient;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> mashingTimes;
    private List<Ingredient> hopAdditions;
    private List<Ingredient> fermentationTimes;
    private List<Map.Entry<String, Float>> ingredients;

    private Unit unit;
    private String type;
    private Context context;

    public IngredientAdapter(String type, Beer beer) {
        this.type = type;
        switch(type){
            case "mash": mashingTimes = beer.mashingTimes; break;
            case "boil": hopAdditions = beer.boilingTimes; break;
            case "fermentation": fermentationTimes = beer.fermentationTimes; break;
        }
    }

    public IngredientAdapter(String type, Map<String, Float> ingredients, Unit unit) {
        this.type = type;
        this.ingredients = new ArrayList<>(ingredients.entrySet());
        this.unit = unit;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView;
        switch(type){
            case "mash":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_mash, parent, false);
                break;
            case "boil":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_boil, parent, false);
                break;
            case "fermentation":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_fermentation, parent, false);
                break;
            case "ingredient":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);
        }
        return new IngredientViewHolder(itemView, this.type);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        switch (this.type) {
            case "mash":
                Ingredient mash = mashingTimes.get(position);
                holder.timeTextView.setText(context.getString(R.string.hourmin, MinSecondDateFormat.format(mash.time)));
                holder.tempTextView.setText(context.getString(R.string.celsius, mash.temp));
                break;
            case "boil":
                Ingredient boil = hopAdditions.get(position);
                holder.nameTextView.setText(boil.name);
                holder.quantityTextView.setText(context.getString(R.string.g, boil.quantity));
                holder.timeTextView.setText(context.getString(R.string.hourmin, MinSecondDateFormat.format(boil.time)));
                break;
            case "fermentation":
                Ingredient ferment = fermentationTimes.get(position);
                holder.nameTextView.setText(ferment.name);
                holder.quantityTextView.setText(context.getString(R.string.g, ferment.quantity));
                holder.tempTextView.setText(context.getString(R.string.celsius, ferment.temp));
                holder.timeTextView.setText(context.getString(R.string.days, TimeUnit.MILLISECONDS.toDays(ferment.time)));
                break;
            case "ingredient":
                Map.Entry<String, Float> ingredient = ingredients.get(position);
                holder.nameTextView.setText(ingredient.getKey());
                switch (this.unit) {
                    case KG: holder.quantityTextView.setText(context.getString(R.string.kg, ingredient.getValue())); break;
                    case G:  holder.quantityTextView.setText(context.getString(R.string.g, ingredient.getValue())); break;
                }
        }
    }

    @Override
    public int getItemCount() {
        switch (this.type) {
            case "mash":
                return mashingTimes.size();
            case "boil":
                return hopAdditions.size();
            case "fermentation":
                return fermentationTimes.size();
            case "ingredient":
                return ingredients.size();
            default:
                return ingredients.size();
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView tempTextView;
        TextView timeTextView;

        IngredientViewHolder(@NonNull View itemView, String type) {
            super(itemView);
            switch(type){
                case "mash":
                    timeTextView = itemView.findViewById(R.id.tvMashTime);
                    tempTextView = itemView.findViewById(R.id.tvMashTemp);
                    break;
                case "boil":
                    nameTextView = itemView.findViewById(R.id.tvBoilName);
                    quantityTextView = itemView.findViewById(R.id.tvBoilQuantity);
                    timeTextView = itemView.findViewById(R.id.tvBoilTime);
                    break;
                case "fermentation":
                    nameTextView = itemView.findViewById(R.id.tvNewFermentationName);
                    quantityTextView = itemView.findViewById(R.id.tvNewFermentationQuantity);
                    tempTextView = itemView.findViewById(R.id.tvNewFermentationTemp);
                    timeTextView = itemView.findViewById(R.id.tvNewFermentationTime);
                    break;
                case "ingredient":
                    nameTextView = itemView.findViewById(R.id.tvIngredientName);
                    quantityTextView = itemView.findViewById(R.id.tvIngredientQuantity);
                    break;
            }
        }
    }
}
