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
import klevente.hu.hophelper.data.Ingredient;

public class NewFermentationAdapter extends RecyclerView.Adapter<NewFermentationAdapter.NewFermentationViewHolder> {

    private List<Ingredient> fermentationTimes = new ArrayList<>();

    private Context context;

    public NewFermentationAdapter() {}

    public List<Ingredient> getFermentationTimeList() {
        return fermentationTimes;
    }

    public void addItem(String dry_hop, float grams, long days, float temp) {
        fermentationTimes.add(new Ingredient(dry_hop, grams, TimeUnit.DAYS.toMillis(days), temp));
        notifyDataSetChanged();
    }

    private void removeItem(Ingredient ferment) {
        fermentationTimes.remove(ferment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewFermentationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_fermentation, parent, false);
        return new NewFermentationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewFermentationViewHolder holder, int position) {
        Ingredient ferment = fermentationTimes.get(position);
        holder.nameTextView.setText(ferment.name);
        holder.quantityTextView.setText(context.getString(R.string.g, ferment.quantity));
        holder.tempTextView.setText(context.getString(R.string.celsius, ferment.temp));
        holder.timeTextView.setText(context.getString(R.string.days, TimeUnit.MILLISECONDS.toDays(ferment.time)));
        holder.ferment = ferment;
    }

    @Override
    public int getItemCount() { return fermentationTimes.size(); }

    class NewFermentationViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView tempTextView;
        TextView timeTextView;
        ImageButton deleteButton;

        Ingredient ferment;

        NewFermentationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvNewFermentationName);
            quantityTextView = itemView.findViewById(R.id.tvNewFermentationQuantity);
            tempTextView = itemView.findViewById(R.id.tvNewFermentationTemp);
            timeTextView = itemView.findViewById(R.id.tvNewFermentationTime);
            deleteButton = itemView.findViewById(R.id.btnNewFermentationDelete);

            deleteButton.setOnClickListener(v -> removeItem(ferment));

        }
    }
}
