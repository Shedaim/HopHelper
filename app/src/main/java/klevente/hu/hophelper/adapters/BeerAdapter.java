package klevente.hu.hophelper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {


    private final List<Beer> items;

    public BeerAdapter() {
        items = new ArrayList<>();
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_beer, viewGroup, false);
        return new BeerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerViewHolder beerViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class BeerViewHolder extends RecyclerView.ViewHolder {
        public BeerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
