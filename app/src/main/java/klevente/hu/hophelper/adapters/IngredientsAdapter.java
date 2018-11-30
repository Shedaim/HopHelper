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
import java.util.Locale;
import java.util.Map;

import klevente.hu.hophelper.R;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Map.Entry<String, Double>> ingredients;

    private Context context;

    public IngredientsAdapter(Map<String, Double> ingredients) {
        this.ingredients = new ArrayList<>(ingredients.entrySet());
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Map.Entry<String, Double> ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getKey());
        holder.quantityTextView.setText(String.format(Locale.getDefault(), "%.1f %s", ingredient.getValue(), "kg"));

    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvIngredientName);
            quantityTextView = itemView.findViewById(R.id.tvIngredientQuantity);
        }
    }
}
