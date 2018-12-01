package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.Unit;

public class NewIngredientsAdapter extends RecyclerView.Adapter<NewIngredientsAdapter.NewIngredientViewHolder> {

    private List<Map.Entry<String, Double>> ingredients;
    private Unit unit;

    private Context context;

    public NewIngredientsAdapter(Unit unit) {
        this.unit = unit;
    }

    @NonNull
    @Override
    public NewIngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_ingredient, parent, false);
        return new NewIngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewIngredientViewHolder holder, int position) {
        Map.Entry<String, Double> ingredient = ingredients.get(position);
        holder.nameTextView.setText(ingredient.getKey());
        switch (unit) {
            case KG: holder.quantityTextView.setText(context.getString(R.string.kg, ingredient.getValue())); break;
            case G:  holder.quantityTextView.setText(context.getString(R.string.g, ingredient.getValue())); break;
            default: holder.quantityTextView.setText(context.getString(R.string.kg, ingredient.getValue())); break;
        }
    }

    @Override
    public int getItemCount() { return ingredients.size(); }

    class NewIngredientViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        ImageButton deleteButton;

        NewIngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvNewIngredientName);
            quantityTextView = itemView.findViewById(R.id.tvNewIngredientQuantity);
            deleteButton = itemView.findViewById(R.id.btnNewIngredientDelete);
        }
    }
}
