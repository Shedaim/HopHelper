package klevente.hu.hophelper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.MashingAdapter;
import klevente.hu.hophelper.data.Beer;

public class BeerDetailsMashingFragment extends Fragment {

    private Beer beer;

    private RecyclerView mashingRecyclerView;
    private MashingAdapter mashingAdapter;

    public BeerDetailsMashingFragment() {}

    public static BeerDetailsMashingFragment newInstance(Beer beer) {
        BeerDetailsMashingFragment fragment = new BeerDetailsMashingFragment();
        fragment.beer = beer;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_details_mashing, container, false);

        mashingRecyclerView = view.findViewById(R.id.rvMashing);
        mashingAdapter = new MashingAdapter(beer);
        mashingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mashingRecyclerView.setAdapter(mashingAdapter);

        return view;
    }
}
