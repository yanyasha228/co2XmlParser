package com.example.test.testproj;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.test.testproj.Utils.SearchUtils;
import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.adapters.ShowsListAdapter;
import com.example.test.testproj.adapters.ShowsListPriceChangingAdapter;
import com.example.test.testproj.models.Offer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class PriceChangingFragment extends Fragment implements ShowsListAdapter.OfferClickListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText search;
    private static List<Offer> showFavoritesList;
    private ShowsListPriceChangingAdapter showsListPriceChangingAdapter;
    private DBAdapter dbAdapter;
    private TextView noDataResults;
    private ImageButton upPriceButton;
    private ImageButton downPriceButton;
    private EditText percentEditText;
    private ImageButton selectAllButton;
    private ImageButton saveChangesButton;
    private boolean allOffersSelected;
    private SearchUtils<Offer> offerSearchUtils = new SearchUtils<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        allOffersSelected = false;
        getAllFavorites();
        showsListPriceChangingAdapter.setFilter(showFavoritesList);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.changing_price_fragment, container, false);
        search = (EditText) layout.findViewById(R.id.searchFavorites);
        percentEditText = (EditText) layout.findViewById(R.id.percentEdit);
        upPriceButton = (ImageButton) layout.findViewById(R.id.upPriceButton);
        downPriceButton = (ImageButton) layout.findViewById(R.id.downPriceButton);
        selectAllButton = (ImageButton) layout.findViewById(R.id.selectAllButton);
        noDataResults = (TextView) layout.findViewById(R.id.noResultsFavorites);
        saveChangesButton = (ImageButton) layout.findViewById(R.id.saveChangesButton);

        upPriceButton.setOnClickListener(this);
        selectAllButton.setOnClickListener(this);
        downPriceButton.setOnClickListener(this);
        saveChangesButton.setOnClickListener(this);

        dbAdapter = new DBAdapter(getActivity());
        getAllFavorites();
        recyclerView = layout.findViewById(R.id.showListFavorites);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        showsListPriceChangingAdapter = new ShowsListPriceChangingAdapter(getActivity(), showFavoritesList);
        showsListPriceChangingAdapter.setOfferClickListener(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(showsListPriceChangingAdapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Checking internet connection
                //if (!connectivityHelper.isConnected())
                // Toast.makeText(getActivity(), "Waiting for internet connection...", Toast.LENGTH_SHORT).show();
                //Searching in favorites show
                String searchText = (s.toString()).toLowerCase();
                List<Offer> searchShowList = offerSearchUtils.findSearchingItemByNonFullName(showFavoritesList, searchText);

//                for (Offer searchOffers : showFavoritesList) {
//                    String name = searchOffers.getName().toLowerCase();
//                    if (name.contains(searchText)) {
//                        searchShowList.add(searchOffers);
//                    }
//                }
                if (searchShowList.size() == 0 && s.length() != 0) {
                    recyclerView.setVisibility(View.GONE);
                    noDataResults.setVisibility(View.VISIBLE);
                } else {
                    noDataResults.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                showFavoritesList = searchShowList;
                if (s.length() == 0) getAllFavorites();
                showsListPriceChangingAdapter.setFilter(showFavoritesList);
            }
        });
        //If press ENTER -> hide keyboard
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            KeyboardFavorites.hide(search);
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        return layout;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectAllButton:
                if (!allOffersSelected) {
                    for (Offer offer : showFavoritesList) {
                        offer.setSelectedForChangingPrice(true);
                    }
                    showsListPriceChangingAdapter.setFilter(showFavoritesList);
                    ((ImageButton) view).setImageResource(R.drawable.heart_like);
                    allOffersSelected = true;
                } else {
                    for (Offer offer : showFavoritesList) {
                        offer.setSelectedForChangingPrice(false);
                    }
                    showsListPriceChangingAdapter.setFilter(showFavoritesList);
                    ((ImageButton) view).setImageResource(R.drawable.heart_unlike);
                    allOffersSelected = false;
                }
                break;

            case R.id.upPriceButton:
                pricePercentageIncrease(showFavoritesList, Double.valueOf(percentEditText.getText().toString()));
                showsListPriceChangingAdapter.setFilter(showFavoritesList);
                break;

            case R.id.downPriceButton:
                pricePercentageDecrease(showFavoritesList, Double.valueOf(percentEditText.getText().toString()));
                showsListPriceChangingAdapter.setFilter(showFavoritesList);
                break;

            case R.id.saveChangesButton:
                AlertDialog.Builder altDialog = new AlertDialog.Builder(getActivity());
                altDialog.setMessage("Сохранить изменение?").setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                saveOffersChanges(showFavoritesList);
                            }
                        })
                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = altDialog.create();
                alertDialog.setTitle("Сохранение изменений");
                alertDialog.show();

        }
    }

    //Class with hideKeyboard func
    public static class KeyboardFavorites {
        public static void hide(View view) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Get all favorites show from database
    private void getAllFavorites() {
        dbAdapter.open();
        showFavoritesList = dbAdapter.getOffers();
        dbAdapter.close();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*
     *Implementing interface which is in the custom RecyclerView.Adapter   {@link ShowsListAdapter}
     */
    @Override
    public void showClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.offerFavorite:
                if (!showFavoritesList.get(position).isSelectedForChangingPrice()) {
                    ((ImageButton) view).setImageResource(R.drawable.heart_like);
                    showFavoritesList.get(position).setSelectedForChangingPrice(true);
                } else {
                    ((ImageButton) view).setImageResource(R.drawable.heart_unlike);
                    showFavoritesList.get(position).setSelectedForChangingPrice(false);
                }
                break;

            case R.id.offerImage:
                Intent imageIntent = new Intent(getActivity(), ImageActivity.class);
                imageIntent.putExtra("url", showFavoritesList.get(position).getImage());
                getActivity().startActivity(imageIntent);
                break;
        }

    }

    private void pricePercentageDecrease(List<Offer> offersForPriceDecreasing, Double percent) {
        for (Offer offer : offersForPriceDecreasing) {
            if (offer.isSelectedForChangingPrice())
                offer.setPrice(round(((offer.getPrice() * (100 - percent)) / 100), 2));
        }
    }

    private void pricePercentageIncrease(List<Offer> offersForPriceDecreasing, Double percent) {
        for (Offer offer : offersForPriceDecreasing) {
            if (offer.isSelectedForChangingPrice())
                offer.setPrice(round(((offer.getPrice() * (100 + percent)) / 100), 2));
        }
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void saveOffersChanges(List<Offer> offersForSaving) {
        dbAdapter.open();
        for (Offer offer : offersForSaving) {
            dbAdapter.update(offer);
        }
        dbAdapter.close();
    }
}
