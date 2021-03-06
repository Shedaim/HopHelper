package klevente.hu.hophelper.adapters;

import android.arch.persistence.room.Index;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;

public class MainBeerAdapter extends RecyclerView.Adapter<MainBeerAdapter.BeerViewHolder> {

    private RecyclerView recyclerView;
    private Context context;

    public interface BeerAdapterListener {
        void onItemClick(int index);
    }

    private BeerAdapterListener listener;

    public MainBeerAdapter(RecyclerView recyclerView, @NonNull BeerAdapterListener listener) {
        this.listener = listener;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_beer, parent, false);
        itemView.setOnClickListener((view) -> {
            int itemPosition = recyclerView.getChildLayoutPosition(itemView);
            listener.onItemClick(itemPosition);

        });
        return new BeerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BeerViewHolder holder, int position) {
        Beer item = BeerList.get(position);
        holder.nameTextView.setText(item.name);
        holder.styleTextView.setText(item.style);
        holder.abvTextView.setText(context.getString(R.string.abv, item.abv));
        holder.ogTextView.setText(context.getString(R.string.og, item.og));
        holder.fgTextView.setText(context.getString(R.string.fg, item.fg));
        holder.item = item;
    }

    @Override
    public int getItemCount() {
        return BeerList.size();
    }

    public void updateAll(List<Beer> beers) {
        BeerList.setItems(beers);
        notifyDataSetChanged();
    }

    public void addItem(Beer item) {
        BeerList.add(item);
        notifyItemInserted(BeerList.size() - 1);
    }

    public void removeItem(Beer item) {
        int item_index = BeerList.get_index(item);
        if (item_index != -1){
            BeerList.remove(item);
            notifyItemRemoved(item_index);
        }
    }

    public void updateItem(Beer _new, Beer old) {
        removeItem(old);
        addItem(_new);
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
