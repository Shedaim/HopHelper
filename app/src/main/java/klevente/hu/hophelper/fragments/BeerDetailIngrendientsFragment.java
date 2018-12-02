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

import klevente.hu.hophelper.R;
import klevente.hu.hophelper.adapters.IngredientsAdapter;
import klevente.hu.hophelper.constants.Unit;
import klevente.hu.hophelper.data.Beer;
import klevente.hu.hophelper.data.BeerList;

public class BeerDetailIngrendientsFragment extends Fragment {

    private static final String TAG = "BeerDetailIngredients";
    private static final String BEER_IDX = "beer";

    private Beer beer;


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
        tvBeerABV.setText(view.getContext().getString(R.string.abv, beer.abv));

        TextView tvBeerOG = view.findViewById(R.id.tvBeerDetailOG);
        tvBeerOG.setText(view.getContext().getString(R.string.og, beer.og));

        TextView tvBeerFG = view.findViewById(R.id.tvBeerDetailFG);
        tvBeerFG.setText(view.getContext().getString(R.string.fg, beer.fg));

        TextView tvBeerDescription = view.findViewById(R.id.tvBeerDetailDescription);
        tvBeerDescription.setText(beer.description);

        TextView tvBeerYeast = view.findViewById(R.id.tvBeerDetailYeast);
        tvBeerYeast.setText(view.getContext().getString(R.string.yeast, beer.yeast));

        RecyclerView maltsRecyclerView = view.findViewById(R.id.rvMalts);
        IngredientsAdapter maltsAdapter = new IngredientsAdapter(beer.malts, Unit.KG);
        maltsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        maltsRecyclerView.setAdapter(maltsAdapter);

        RecyclerView hopsRecyclerView = view.findViewById(R.id.rvHops);
        IngredientsAdapter hopsAdapter = new IngredientsAdapter(beer.hops, Unit.G);
        hopsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hopsRecyclerView.setAdapter(hopsAdapter);

        RecyclerView extrasRecyclerView = view.findViewById(R.id.rvExtras);
        IngredientsAdapter extrasAdapter = new IngredientsAdapter(beer.extras, Unit.G);
        extrasRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        extrasRecyclerView.setAdapter(extrasAdapter);

        return view;
    }
}
