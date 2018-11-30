package klevente.hu.hophelper.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.BoilingAdapter;
import klevente.hu.hophelper.data.Beer;

public class BeerDetailsBoilingFragment extends Fragment {

    private Beer beer;

    private RecyclerView boilingRecyclerView;
    private BoilingAdapter boilingAdapter;

    public BeerDetailsBoilingFragment() {}

    public static BeerDetailsBoilingFragment newInstance(Beer beer) {
        BeerDetailsBoilingFragment fragment = new BeerDetailsBoilingFragment();
        fragment.beer = beer;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_details_boiling, container, false);

        boilingRecyclerView = view.findViewById(R.id.rvBoiling);
        boilingAdapter = new BoilingAdapter(beer);
        boilingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        boilingRecyclerView.setAdapter(boilingAdapter);


        return view;
    }

}
