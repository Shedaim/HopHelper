package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private Map<String, Double> ingredients;

    private RecyclerView recyclerView;
    private Context context;

    public IngredientsAdapter(RecyclerView recyclerView, Beer beer) {
        this.recyclerView = recyclerView;
        ingredients = beer.getAllIngredients();
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
