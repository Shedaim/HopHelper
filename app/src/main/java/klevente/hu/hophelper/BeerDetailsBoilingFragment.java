package klevente.hu.hophelper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import klevente.hu.hophelper.data.Beer;

public class BeerDetailsBoilingFragment extends Fragment {

    private Beer beer;

    public BeerDetailsBoilingFragment() {}

    public static BeerDetailsBoilingFragment newInstance(Beer beer) {
        BeerDetailsBoilingFragment fragment = new BeerDetailsBoilingFragment();
        fragment.beer = beer;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_details_boiling, container, false);
    }

}
