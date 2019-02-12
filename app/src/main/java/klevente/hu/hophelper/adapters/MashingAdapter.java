package klevente.hu.hophelper.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.constants.MinSecondDateFormat;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.Ingredient;

public class MashingAdapter extends RecyclerView.Adapter<MashingAdapter.MashViewHolder> {

    private List<Ingredient> mashingTimes;

    private Context context;

    public MashingAdapter(Beer beer) {
        mashingTimes = beer.mashingTimes;
    }

    @NonNull
    @Override
    public MashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_mash, parent, false);
        return new MashViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MashViewHolder holder, int position) {
        Ingredient time = mashingTimes.get(position);
        holder.timeTextView.setText(context.getString(R.string.hourmin, MinSecondDateFormat.format(time.time)));
        holder.tempTextView.setText(context.getString(R.string.celsius, time.temp));
    }

    @Override
    public int getItemCount() { return mashingTimes.size(); }

    class MashViewHolder extends RecyclerView.ViewHolder {

        TextView timeTextView;
        TextView tempTextView;

        MashViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.tvMashTime);
            tempTextView = itemView.findViewById(R.id.tvMashTemp);
        }
    }
}
