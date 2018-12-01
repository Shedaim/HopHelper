package klevente.hu.hophelper.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.IngredientsAdapter;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;

public class BeerDetailIngrendientsFragment extends Fragment {

    private static final String TAG = "BeerDetailIngredients";
    private static final String BEER_IDX = "beer";

    private Beer beer;

    private RecyclerView maltsRecyclerView;
    private RecyclerView hopsRecyclerView;
    private RecyclerView extrasRecyclerView;
    private IngredientsAdapter maltsAdapter;
    private IngredientsAdapter hopsAdapter;
    private IngredientsAdapter extrasAdapter;


    public BeerDetailIngrendientsFragment() {}

    public static BeerDetailIngrendientsFragment newInstance(int index) {
        BeerDetailIngrendientsFragment fragment = new BeerDetailIngrendientsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_detail_ingredients, container, false);

        // TODO beer icon color
        // TODO beer bitterness meter

        TextView tvBeerStyle = view.findViewById(R.id.tvBeerDetailStyle);
        tvBeerStyle.setText(beer.style);

        TextView tvBeerABV = view.findViewById(R.id.tvBeerDetailABV);
        tvBeerABV.setText(String.format(Locale.getDefault(), "%s: %.1f%s", view.getContext().getString(R.string.abv), beer.abv, "%"));

        TextView tvBeerOG = view.findViewById(R.id.tvBeerDetailOG);
        tvBeerOG.setText(String.format(Locale.getDefault(), "%s: %d", view.getContext().getString(R.string.og), beer.og));

        TextView tvBeerFG = view.findViewById(R.id.tvBeerDetailFG);
        tvBeerFG.setText(String.format(Locale.getDefault(), "%s: %d", view.getContext().getString(R.string.fg), beer.fg));

        TextView tvBeerDescription = view.findViewById(R.id.tvBeerDetailDescription);
        tvBeerDescription.setText(beer.description);

        TextView tvBeerYeast = view.findViewById(R.id.tvBeerDetailYeast);
        tvBeerYeast.setText(beer.yeast);

        maltsRecyclerView = view.findViewById(R.id.rvMalts);
        maltsAdapter = new IngredientsAdapter(beer.malts);
        maltsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        maltsRecyclerView.setAdapter(maltsAdapter);

        hopsRecyclerView = view.findViewById(R.id.rvHops);
        hopsAdapter = new IngredientsAdapter(beer.hops);
        hopsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hopsRecyclerView.setAdapter(hopsAdapter);

        extrasRecyclerView = view.findViewById(R.id.rvExtras);
        extrasAdapter = new IngredientsAdapter(beer.extras);
        extrasRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        extrasRecyclerView.setAdapter(extrasAdapter);

        return view;
    }
}
