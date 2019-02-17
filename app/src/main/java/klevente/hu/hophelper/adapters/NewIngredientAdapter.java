package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.constants.Unit;
import klevente.hu.hophelper.data.Ingredient;


public class NewIngredientAdapter extends RecyclerView.Adapter<NewIngredientAdapter.NewIngredientViewHolder> {
    String TAG = "IngredientAdapter";
    private List<Ingredient> mashTimes = new ArrayList<>();
    private List<Ingredient> hopAdditions = new ArrayList<>();
    private List<Ingredient> fermentationTimes = new ArrayList<>();
    private List<Pair<String, Float>> ingredients = new ArrayList<>();

    private Context context;
    public Unit unit;
    public String type;

    public NewIngredientAdapter(String type) {
        Log.d("NewIngredientAdapter", "created ingredient of type: " + this.type);
        this.type = type;
    }

    public NewIngredientAdapter(String type, Unit unit) {
        Log.d("NewIngredientAdapter", "created ingredient of type: " + this.type + " and unit: " + this.unit);
        this.type = type;
        this.unit = unit;
    }

    public List<Ingredient> getHopAdditionList() {
        return hopAdditions;
    }
    public List<Ingredient> getMashTimeList() {
        return mashTimes;
    }
    public List<Ingredient> getFermentationTimeList() { return fermentationTimes; }
    public Map<String, Float> getIngredientMap() {
        Map<String, Float> map = new HashMap<>(ingredients.size());
        for (Pair<String, Float> i : ingredients) {
            map.put(i.first, i.second);
        }
        return map;
    }

    public void addItem(String name, float quantity, long time, float temp) {
        Log.d(TAG, "Adding item of type: " + this.type);
        switch (this.type){
            case "mash":
                mashTimes.add(new Ingredient(name, quantity, TimeUnit.MINUTES.toMillis(time), temp));
                break;
            case "boil":
                hopAdditions.add(new Ingredient(name, quantity, TimeUnit.MINUTES.toMillis(time), 100));
                break;
            case "fermentation":
                fermentationTimes.add(new Ingredient(name, quantity, time, temp));
                break;
            case "ingredient":
                ingredients.add(new Pair<>(name, quantity));
        }
        notifyDataSetChanged();
    }

    private void removeItem(Ingredient addition) {
        Log.d(TAG, "Removing item of type: " + this.type);
        switch (this.type) {
            case "mash":
                mashTimes.remove(addition);
                break;
            case "boil":
                hopAdditions.remove(addition);
                break;
            case "fermentation":
                fermentationTimes.remove(addition);
                break;
        }
        notifyDataSetChanged();
    }
    private void removeItem(Pair<String, Float> ingredient) {
        Log.d(TAG, "Removing item of type: " + this.type);
        ingredients.remove(ingredient);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView;
        Log.d(TAG, "Creating view for ingredient of type: " + this.type);
        switch (this.type) {
            case "mash":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_mash, parent, false);
                break;
            case "boil":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_boil, parent, false);
                break;
            case "fermentation":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_fermentation, parent, false);
                break;
            case "ingredient":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_ingredient, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_mash, parent, false); //TODO fix this default
        }
        return new NewIngredientViewHolder(itemView, this.type);
    }

    @Override
    public void onBindViewHolder(@NonNull NewIngredientViewHolder holder, int position) {
        Ingredient addition;
        Log.d(TAG, "Binding view for ingredient of type: " + this.type);
        switch (this.type) {
            case "mash":
                addition = mashTimes.get(position);
                holder.timeTextView.setText(MinSecondDateFormat.format(addition.time));
                holder.tempTextView.setText(context.getString(R.string.celsius, addition.temp));
                holder.addition = addition;
                break;
            case "boil":
                addition = hopAdditions.get(position);
                holder.nameTextView.setText(addition.name);
                holder.quantityTextView.setText(context.getString(R.string.g, addition.quantity));
                holder.timeTextView.setText(MinSecondDateFormat.format(addition.time));
                holder.addition = addition;
                break;
            case "fermentation":
                addition = fermentationTimes.get(position);
                holder.nameTextView.setText(addition.name);
                holder.quantityTextView.setText(context.getString(R.string.g, addition.quantity));
                holder.timeTextView.setText(context.getString(R.string.days, addition.time));
                holder.tempTextView.setText(context.getString(R.string.celsius, addition.temp));
                holder.addition = addition;
                break;
            case "ingredient":
                Pair<String, Float> ingredient = ingredients.get(position);
                holder.nameTextView.setText(ingredient.first);
                switch (this.unit) {
                    case KG:
                        holder.quantityTextView.setText(context.getString(R.string.kg, ingredient.second));
                        break;
                    case G:
                        holder.quantityTextView.setText(context.getString(R.string.g, ingredient.second));
                        break;
                }
                holder.ingredient = ingredient;
                break;
            default:
                addition = mashTimes.get(position); //TODO fix this default
                holder.addition = addition;
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Getting item count for type: " + this.type);
        switch (this.type) {
            case "mash":
                return mashTimes.size();
            case "boil":
                return hopAdditions.size();
            case "fermentation":
                return fermentationTimes.size();
            case "ingredient":
                return ingredients.size();
            default:
                return mashTimes.size();
        }
    }

    class NewIngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView timeTextView;
        TextView tempTextView;
        ImageButton deleteButton;

        Ingredient addition;
        Pair<String, Float> ingredient;

        NewIngredientViewHolder(@NonNull View itemView, String type) {
            super(itemView);
            Log.d(TAG, "Creating viewholder for ingredient of type: " + type);
            switch(type){
                case "mash":
                    tempTextView = itemView.findViewById(R.id.tvNewMashTemp);
                    timeTextView = itemView.findViewById(R.id.tvNewMashTime);
                    deleteButton = itemView.findViewById(R.id.btnNewMashDelete);
                    deleteButton.setOnClickListener(v -> removeItem(addition));
                    break;
                case "boil":
                    nameTextView = itemView.findViewById(R.id.tvNewBoilName);
                    quantityTextView = itemView.findViewById(R.id.tvNewBoilQuantity);
                    timeTextView = itemView.findViewById(R.id.tvNewBoilTime);
                    deleteButton = itemView.findViewById(R.id.btnNewBoilDelete);
                    deleteButton.setOnClickListener(v -> removeItem(addition));
                    break;
                case "fermentation":
                    nameTextView = itemView.findViewById(R.id.tvNewFermentationName);
                    quantityTextView = itemView.findViewById(R.id.tvNewFermentationQuantity);
                    tempTextView = itemView.findViewById(R.id.tvNewFermentationTemp);
                    timeTextView = itemView.findViewById(R.id.tvNewFermentationTime);
                    deleteButton = itemView.findViewById(R.id.btnNewFermentationDelete);
                    deleteButton.setOnClickListener(v -> removeItem(addition));
                    break;
                case "ingredient":
                    nameTextView = itemView.findViewById(R.id.tvNewIngredientName);
                    quantityTextView = itemView.findViewById(R.id.tvNewIngredientQuantity);
                    deleteButton = itemView.findViewById(R.id.btnNewIngredientDelete);
                    deleteButton.setOnClickListener(v -> removeItem(ingredient));
                    break;
                default:
                    deleteButton = itemView.findViewById(R.id.btnNewMashDelete);
                    Toast.makeText(context,"Wrong type", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
