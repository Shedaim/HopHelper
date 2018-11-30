package klevente.hu.hophelper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class MashingAdapter extends RecyclerView.Adapter<MashingAdapter.MashViewHolder> {


    @NonNull
    @Override
    public MashViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MashViewHolder mashViewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MashViewHolder extends RecyclerView.ViewHolder {

        public MashViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
