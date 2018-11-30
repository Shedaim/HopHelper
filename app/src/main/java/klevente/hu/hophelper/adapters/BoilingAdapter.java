package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.HopAddition;

public class BoilingAdapter extends RecyclerView.Adapter<BoilingAdapter.BoilViewHolder> {

    List<HopAddition> hopAdditions;

    private Context context;

    public BoilingAdapter(Beer beer) {
        hopAdditions = beer.boilingTimes;
    }

    @NonNull
    @Override
    public BoilViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_boil, parent, false);
        return new BoilViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoilViewHolder holder, int position) {
        HopAddition addition = hopAdditions.get(position);
        holder.nameTextView.setText(addition.name);
        holder.quantityTextView.setText(String.format(Locale.getDefault(), "%d g", addition.grams));
        holder.timeTextView.setText(String.format(Locale.getDefault(), "%d min", addition.minutes));
    }

    @Override
    public int getItemCount() { return hopAdditions.size(); }

    class BoilViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView timeTextView;

        public BoilViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvBoilName);
            quantityTextView = itemView.findViewById(R.id.tvBoilQuantity);
            timeTextView = itemView.findViewById(R.id.tvBoilTime);
        }
    }
}
