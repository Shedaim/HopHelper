package klevente.hu.hophelper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class NewMashingAdapter extends RecyclerView.Adapter<NewMashingAdapter.NewMashingViewHolder> {


    @NonNull
    @Override
    public NewMashingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewMashingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NewMashingViewHolder extends RecyclerView.ViewHolder {

        NewMashingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
