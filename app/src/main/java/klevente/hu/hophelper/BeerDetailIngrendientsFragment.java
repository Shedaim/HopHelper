package klevente.hu.hophelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import klevente.hu.hophelper.data.Beer;

public class BeerDetailIngrendientsFragment extends Fragment {

    private static final String TAG = "BeerDetailIngredientsFragment";

    private Beer beer;

    public BeerDetailIngrendientsFragment() {}

    public static BeerDetailIngrendientsFragment newInstance(Beer beer) {
        BeerDetailIngrendientsFragment fragment = new BeerDetailIngrendientsFragment();
        fragment.beer = beer;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_detail_ingrendients, container, false);
    }
}
