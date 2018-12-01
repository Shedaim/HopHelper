package klevente.hu.hophelper.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class NewBoilingAdapter extends RecyclerView.Adapter<NewBoilingAdapter.NewBoilingViewHolder> {


    @NonNull
    @Override
    public NewBoilingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewBoilingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NewBoilingViewHolder extends RecyclerView.ViewHolder {

        public NewBoilingViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
