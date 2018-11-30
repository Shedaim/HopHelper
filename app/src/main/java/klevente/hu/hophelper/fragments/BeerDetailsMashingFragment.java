package klevente.hu.hophelper.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.data.Beer;

public class BeerDetailsMashingFragment extends Fragment {

    private Beer beer;

    public BeerDetailsMashingFragment() {}

    public static BeerDetailsMashingFragment newInstance(Beer beer) {
        BeerDetailsMashingFragment fragment = new BeerDetailsMashingFragment();
        fragment.beer = beer;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_details_mashing, container, false);
    }
}
