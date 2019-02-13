package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.Ingredient;

public class FermentationAdapter extends RecyclerView.Adapter<FermentationAdapter.FermentationViewHolder> {

    private List<Ingredient> fermentationTimes;

    private Context context;

    public FermentationAdapter(Beer beer) {
        fermentationTimes = beer.fermentationTimes;
    }

    @NonNull
    @Override
    public FermentationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_fermentation, parent, false);
        return new FermentationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FermentationViewHolder holder, int position) {
        Ingredient ferment = fermentationTimes.get(position);
        holder.nameTextView.setText(ferment.name);
        holder.quantityTextView.setText(context.getString(R.string.g, ferment.quantity));
        holder.tempTextView.setText(context.getString(R.string.celsius, ferment.temp));
        holder.timeTextView.setText(context.getString(R.string.days, TimeUnit.MILLISECONDS.toDays(ferment.time)));
    }

    @Override
    public int getItemCount() { return fermentationTimes.size(); }

    class FermentationViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView tempTextView;
        TextView timeTextView;

        FermentationViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvNewFermentationName);
            quantityTextView = itemView.findViewById(R.id.tvNewFermentationQuantity);
            tempTextView = itemView.findViewById(R.id.tvNewFermentationTemp);
            timeTextView = itemView.findViewById(R.id.tvNewFermentationTime);
        }
    }
}
