package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;

public class BeerAdapter extends RecyclerView.Adapter<BeerAdapter.BeerViewHolder> {


    private final List<Beer> items;
    private RecyclerView recyclerView;
    private Context context;

    public BeerAdapter(RecyclerView recyclerView) {
        items = new ArrayList<>();
        items.add(new Beer(0, "Uradalmi Intro", "nagyon finom", "IPA", 1.041, 1.012, 5.2));
        items.add(new Beer(1, "Ugar Stróman", "hazy af", "New England IPA", 1.053, 1.020, 5.6));


        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beer, parent, false);
        itemView.setOnClickListener((view) -> {
            int itemPosition = recyclerView.getChildLayoutPosition(itemView);
            Toast.makeText(context, "" + itemPosition, Toast.LENGTH_LONG).show();
        });
        return new BeerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerViewHolder holder, int position) {
        Beer item = items.get(position);
        holder.nameTextView.setText(item.name);
        holder.styleTextView.setText(item.style);
        holder.abvTextView.setText(String.format(Locale.getDefault(), "%s: %.1f%s", context.getString(R.string.abv), item.abv, "%"));
        holder.ogTextView.setText(String.format(Locale.getDefault(), "%s: %.3f", context.getString(R.string.og), item.og));
        holder.fgTextView.setText(String.format(Locale.getDefault(), "%s: %.3f", context.getString(R.string.fg), item.fg));
        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Beer item) {
        items.add(item);
        notifyItemInserted(items.size()-1);
    }

    class BeerViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;
        TextView styleTextView;
        TextView abvTextView;
        TextView ogTextView;
        TextView fgTextView;

        Beer item;

        BeerViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.ivBeerIcon);
            nameTextView = itemView.findViewById(R.id.tvBeerName);
            styleTextView = itemView.findViewById(R.id.tvBeerStyle);
            abvTextView = itemView.findViewById(R.id.tvABV);
            ogTextView = itemView.findViewById(R.id.tvOG);
            fgTextView = itemView.findViewById(R.id.tvFG);
        }


    }
}