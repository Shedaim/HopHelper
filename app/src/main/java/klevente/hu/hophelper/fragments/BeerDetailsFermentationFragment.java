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
import klevente.hu.hophelper.adapters.FermentationAdapter;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;

public class BeerDetailsFermentationFragment extends Fragment {

    private Beer beer;
    private static final String BEER_IDX = "beer";

    public BeerDetailsFermentationFragment() {}

    public static BeerDetailsFermentationFragment newInstance(int index) {
        BeerDetailsFermentationFragment fragment = new BeerDetailsFermentationFragment();
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
        View view = inflater.inflate(R.layout.fragment_beer_details_fermentation, container, false);

        RecyclerView fermentationRecyclerView = view.findViewById(R.id.rvFermentation);
        FermentationAdapter fermentationAdapter = new FermentationAdapter(beer);
        fermentationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fermentationRecyclerView.setAdapter(fermentationAdapter);

        return view;
    }
}
