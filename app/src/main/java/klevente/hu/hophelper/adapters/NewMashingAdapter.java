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

public class NewMashingAdapter extends RecyclerView.Adapter<NewMashingAdapter.NewMashingViewHolder> {

    private List<Ingredient> mashTimes = new ArrayList<>();

    private Context context;

    public NewMashingAdapter() {}

    public List<Ingredient> getMashTimeList() {
        return mashTimes;
    }

    public void addItem(int temp, long minutes) {
        mashTimes.add(new Ingredient("", 0, TimeUnit.MINUTES.toMillis(minutes), temp));
        notifyDataSetChanged();
    }

    private void removeItem(Ingredient time) {
        mashTimes.remove(time);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewMashingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_mash, parent, false);
        return new NewMashingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewMashingViewHolder holder, int position) {
        Ingredient time = mashTimes.get(position);
        holder.tempTextView.setText(context.getString(R.string.celsius, time.temp));
        holder.timeTextView.setText(MinSecondDateFormat.format(time.time));
        holder.time = time;
    }

    @Override
    public int getItemCount() { return mashTimes.size(); }

    class NewMashingViewHolder extends RecyclerView.ViewHolder {

        TextView tempTextView;
        TextView timeTextView;
        ImageButton deleteButton;

        Ingredient time;

        NewMashingViewHolder(@NonNull View itemView) {
            super(itemView);
            tempTextView = itemView.findViewById(R.id.tvNewMashTemp);
            timeTextView = itemView.findViewById(R.id.tvNewMashTime);
            deleteButton = itemView.findViewById(R.id.btnNewMashDelete);

            deleteButton.setOnClickListener(v -> removeItem(time));

        }
    }
}
