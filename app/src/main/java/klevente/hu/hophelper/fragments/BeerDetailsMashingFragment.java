package klevente.hu.hophelper.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.IngredientAdapter;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;

public class BeerDetailsMashingFragment extends Fragment {

    private Beer beer;
    private static final String BEER_IDX = "beer";

    public BeerDetailsMashingFragment() {}

    public static BeerDetailsMashingFragment newInstance(int index) {
        BeerDetailsMashingFragment fragment = new BeerDetailsMashingFragment();
        Bundle bundle = new Bundle(1);
        bundle.putInt(BEER_IDX, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beer = BeerList.get(getArguments().getInt(BEER_IDX));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_details_mashing, container, false);

        RecyclerView mashingRecyclerView = view.findViewById(R.id.rvMashing);
        IngredientAdapter mashingAdapter = new IngredientAdapter("mash", beer);
        mashingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mashingRecyclerView.setAdapter(mashingAdapter);

        return view;
    }
}
