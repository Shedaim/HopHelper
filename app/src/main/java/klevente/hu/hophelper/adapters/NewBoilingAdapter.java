package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.Ingredient;

public class NewBoilingAdapter extends RecyclerView.Adapter<NewBoilingAdapter.NewBoilingViewHolder> {

    private List<Ingredient> hopAdditions = new ArrayList<>();

    private Context context;

    public NewBoilingAdapter() {}

    public List<Ingredient> getHopAdditionList() {
        return hopAdditions;
    }

    public void addItem(String name, float grams, long minutes) {
        hopAdditions.add(new Ingredient(name, grams, TimeUnit.MINUTES.toMillis(minutes), 100));
        notifyDataSetChanged();
    }

    private void removeItem(Ingredient addition) {
        hopAdditions.remove(addition);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewBoilingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_boil, parent, false);
        return new NewBoilingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewBoilingViewHolder holder, int position) {
        Ingredient addition = hopAdditions.get(position);
        holder.nameTextView.setText(addition.name);
        holder.quantityTextView.setText(context.getString(R.string.g, addition.quantity));
        holder.timeTextView.setText(MinSecondDateFormat.format(addition.time));

        holder.addition = addition;
    }

    @Override
    public int getItemCount() { return hopAdditions.size(); }

    class NewBoilingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView timeTextView;
        ImageButton deleteButton;

        Ingredient addition;

        NewBoilingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvNewBoilName);
            quantityTextView = itemView.findViewById(R.id.tvNewBoilQuantity);
            timeTextView = itemView.findViewById(R.id.tvNewBoilTime);
            deleteButton = itemView.findViewById(R.id.btnNewBoilDelete);

            deleteButton.setOnClickListener(v -> removeItem(addition));
        }
    }
}
