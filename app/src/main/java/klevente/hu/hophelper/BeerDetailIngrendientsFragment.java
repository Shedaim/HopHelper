package klevente.hu.hophelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

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

        return view;
    }
}
