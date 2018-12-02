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

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.HopAddition;

public class NewBoilingAdapter extends RecyclerView.Adapter<NewBoilingAdapter.NewBoilingViewHolder> {

    List<HopAddition> hopAdditions = new ArrayList<>();

    private Context context;

    public NewBoilingAdapter() {}

    @NonNull
    @Override
    public NewBoilingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_boil, parent, false);
        return new NewBoilingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewBoilingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() { return hopAdditions.size(); }

    class NewBoilingViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView timeTextView;

        NewBoilingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.tvNewBoilName);
            quantityTextView = itemView.findViewById(R.id.tvNewBoilQuantity);
            timeTextView = itemView.findViewById(R.id.tvNewBoilTime);
        }
    }
}
