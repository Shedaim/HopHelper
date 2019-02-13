package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.Ingredient;

public class NewIngredientAdapter extends RecyclerView.Adapter<NewIngredientAdapter.NewIngredientViewHolder> {

    private List<Ingredient> mashTimes = new ArrayList<>();
    private List<Ingredient> hopAdditions = new ArrayList<>();
    private List<Ingredient> fermentationTimes = new ArrayList<>();

    private Context context;
    public String type;

    public NewIngredientAdapter(String type) {
        this.type = type;
    }

    public List<Ingredient> getHopAdditionList() {
        return hopAdditions;
    }
    public List<Ingredient> getMashTimeList() {
        return mashTimes;
    }
    public List<Ingredient> getFermentationTimeList() {
        return fermentationTimes;
    }

    public void addItem(String name, float quantity, long time, float temp) {
        switch (this.type){
            case "mash": mashTimes.add(new Ingredient(name, quantity, TimeUnit.MINUTES.toMillis(time), temp));
            case "boil": hopAdditions.add(new Ingredient(name, quantity, TimeUnit.MINUTES.toMillis(time), 100));
            case "fermentation": fermentationTimes.add(new Ingredient(name, quantity, TimeUnit.MILLISECONDS.toDays(time), temp));
        }
        notifyDataSetChanged();
    }

    private void removeItem(Ingredient addition) {
        switch (this.type) {
            case "mash":
                mashTimes.remove(addition);
            case "boil":
                hopAdditions.remove(addition);
            case "fermentation":
                fermentationTimes.remove(addition);
            default:
                Toast.makeText(context,"Wrong type", Toast.LENGTH_SHORT).show();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView;
        switch (this.type) {
            case "mash":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_mash, parent, false);
            case "boil":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_boil, parent, false);
            case "fermentation":
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_fermentation, parent, false);
            default:
                itemView = LayoutInflater.from(context).inflate(R.layout.item_new_mash, parent, false); //TODO fix this default
        }
        return new NewIngredientViewHolder(itemView, this.type);
    }

    @Override
    public void onBindViewHolder(@NonNull NewIngredientViewHolder holder, int position) {
        Ingredient addition;
        switch (this.type) {
            case "mash":
                addition = mashTimes.get(position);
            case "boil":
                addition = hopAdditions.get(position);
            case "fermentation":
                addition = fermentationTimes.get(position);
            default:
                addition = mashTimes.get(position); //TODO fix this default
        }
        holder.nameTextView.setText(addition.name);
        holder.quantityTextView.setText(context.getString(R.string.g, addition.quantity));
        holder.timeTextView.setText(MinSecondDateFormat.format(addition.time));
        holder.tempTextView.setText(context.getString(R.string.celsius, addition.temp));
        holder.addition = addition;
    }

    @Override
    public int getItemCount() {
        switch (this.type) {
            case "mash":
                return mashTimes.size();
            case "boil":
                return hopAdditions.size();
            case "fermentation":
                return fermentationTimes.size();
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

        NewIngredientViewHolder(@NonNull View itemView, String type) {
            super(itemView);
            switch(type){
                case "mash":
                    tempTextView = itemView.findViewById(R.id.tvNewMashTemp);
                    timeTextView = itemView.findViewById(R.id.tvNewMashTime);
                    deleteButton = itemView.findViewById(R.id.btnNewMashDelete);
                case "boil":
                    nameTextView = itemView.findViewById(R.id.tvNewBoilName);
                    quantityTextView = itemView.findViewById(R.id.tvNewBoilQuantity);
                    timeTextView = itemView.findViewById(R.id.tvNewBoilTime);
                    deleteButton = itemView.findViewById(R.id.btnNewBoilDelete);
                case "fermentation":
                    nameTextView = itemView.findViewById(R.id.tvNewFermentationName);
                    quantityTextView = itemView.findViewById(R.id.tvNewFermentationQuantity);
                    tempTextView = itemView.findViewById(R.id.tvNewFermentationTemp);
                    timeTextView = itemView.findViewById(R.id.tvNewFermentationTime);
                    deleteButton = itemView.findViewById(R.id.btnNewFermentationDelete);
                default:
                    deleteButton = itemView.findViewById(R.id.btnNewMashDelete);
                    Toast.makeText(context,"Wrong type", Toast.LENGTH_SHORT).show();
            }
            deleteButton.setOnClickListener(v -> removeItem(addition));
        }
    }
}
