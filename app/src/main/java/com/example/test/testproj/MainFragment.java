package com.example.test.testproj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.test.testproj.adapters.DBAdapter;
import com.example.test.testproj.adapters.ShowsListAdapter;
import com.example.test.testproj.helpers.ConnectivityHelper;
import com.example.test.testproj.helpers.ShowBuilder;
import com.example.test.testproj.models.Offer;
import com.example.test.testproj.models.OfferServerList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fragment that uses the REST API of www.tvmaze.com to searching data
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */

public class MainFragment extends Fragment implements ShowsListAdapter.OfferClickListener {
    public static String offersServerList;
    private RecyclerView recyclerView;
    private EditText search;
    private DBAdapter dbAdapter;
    private ShowsListAdapter showListAdapter;
    private static List<Offer> offersMainList;
    private static List<Offer> offersFavoriteList;
    private static List<Offer> offersSearchList;
    private ConnectivityHelper connectivityHelper;
    private TextView noDataResults;
    private OfferServerList offerServerList;
    private String stringOfMain;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_main, container, false);

        search = (EditText) layout.findViewById(R.id.searchMain);

        noDataResults = (TextView) layout.findViewById(R.id.noResultsMain);

        dbAdapter = new DBAdapter(getActivity());

        connectivityHelper = new ConnectivityHelper(getActivity());
        offerServerList = OfferServerList.getInstance();
        offersMainList = offerServerList.getOfferServerMainList();
        getAllFavorites();
        offersSearchList = offersFavoriteList;
        stringOfMain = offerServerList.getStringOffersXmlMain();

        recyclerView = layout.findViewById(R.id.showListMain);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        showListAdapter = new ShowsListAdapter(getActivity(), offersSearchList);

        showListAdapter.setOfferClickListener(this);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(showListAdapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //If checking connection is successful - > lets search
                //Searching offers
                String searchText = (s.toString()).toLowerCase();
                List<Offer> searchOfferList = new ArrayList<Offer>();
                for (Offer searchOffers : offersMainList) {
                    String name = searchOffers.getName().toLowerCase();
                    if (name.contains(searchText)) {
                        searchOfferList.add(searchOffers);
                    }
                }
                if (searchOfferList.size() == 0 && s.length() != 0) {
                    recyclerView.setVisibility(View.GONE);
                    noDataResults.setVisibility(View.VISIBLE);
                } else {
                    noDataResults.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }

                offersSearchList = searchOfferList;
                if (s.length() == 0) {
                    getAllFavorites();
                    offersSearchList = offersFavoriteList;
                }
                getOfferSearchListWithFavoritesValidation();
                showListAdapter.setFilter(offersSearchList);

            }
        });
//If press ENTER -> hide keyboard
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            KeyboardMain.hide(search);
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

    //Class with hideKeyboard func
    public static class KeyboardMain {
        public static void hide(View view) {
            Context context = view.getContext();
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Loading data from server Async
    /*
    private void getShowServerData(final String searchStr) {
        AsyncTask<String, Void, Void> searchTask = new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://api.tvmaze.com/search/shows?q=" + searchStr).build();
                try {
                    Response response = client.newCall(request).execute();
                    showListSearch = new ShowBuilder(response.body().string()).getShowListWithFavoritesValidation(showMainList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if (showListSearch.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noDataResults.setVisibility(View.VISIBLE);
                } else {
                    noDataResults.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                showListAdapter.setFilter(showListSearch);
            }
        };
        searchTask.execute(searchStr);
    }
    */
//If Show doesn't exist put Show into database
    private void addFavoriteShow(int position) {
        dbAdapter.open();
        Offer addToFavoritesOffer = offersSearchList.get(position);
        if (!dbAdapter.offerAlreadyExists(addToFavoritesOffer)) {
            addToFavoritesOffer.setFav(1);
            dbAdapter.insert(addToFavoritesOffer);
            addToFavoritesOffer.setId(dbAdapter.getOfferByName(addToFavoritesOffer.getName()).getId());
        }
        dbAdapter.close();
        getAllFavorites();

    }

    //Deleting Show
    private void deleteFavoriteShow(Offer offer) {
        dbAdapter.open();
        dbAdapter.delete(offer.getId());
        dbAdapter.close();
        getAllFavorites();

    }
    public void getOfferListWithFavoritesValidation() {
        getAllFavorites();
        for (Offer favOffer : offersFavoriteList) {
            for (Offer nOffer : offersMainList) {
                if (nOffer.getName().equals(favOffer.getName()) && nOffer.getUrl().equals(favOffer.getUrl())) {
                    nOffer.setFav(1);
                    nOffer.setId(favOffer.getId());
                    nOffer.setStock_quantity(favOffer.getStock_quantity());
                    nOffer.setPrice(favOffer.getPrice());
                    nOffer.setVendor(favOffer.getVendor());
                } else {
                    nOffer.setFav(0);
                    nOffer.setId(0);

                }
            }
            for (Offer nsOffer : offersSearchList) {
                if (nsOffer.getName().equals(favOffer.getName()) && nsOffer.getUrl().equals(favOffer.getUrl())) {
                    nsOffer.setFav(1);
                    nsOffer.setId(favOffer.getId());
                    nsOffer.setStock_quantity(favOffer.getStock_quantity());
                    nsOffer.setPrice(favOffer.getPrice());
                    nsOffer.setVendor(favOffer.getVendor());
                } else {
                    nsOffer.setFav(0);
                    nsOffer.setId(0);
                }

            }

        }
    }
    public void getOfferSearchListWithFavoritesValidation() {
        getAllFavorites();
        for (Offer favOffer : offersFavoriteList)
            for (Offer nOffer : offersSearchList) {
                if (nOffer.getName().equals(favOffer.getName()) && nOffer.getUrl().equals(favOffer.getUrl())) {
                    nOffer.setFav(1);
                    nOffer.setId(favOffer.getId());
                    nOffer.setStock_quantity(favOffer.getStock_quantity());
                    nOffer.setPrice(favOffer.getPrice());
                    nOffer.setVendor(favOffer.getVendor());
                }

            }

    }
    /*
*Implementing interface which is in the custom RecyclerView.Adapter   {@link ShowsListAdapter}
*/
    @Override
    public void showClicked(View view, int position) {
        switch (view.getId()) {
            case R.id.showItemLayout:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse((offersSearchList.get(position)).getUrl()));
                getActivity().startActivity(intent);
                break;
            case R.id.offerFavorite:
                if (offersSearchList.get(position).getFav() == 0) {
                    addFavoriteShow(position);
                    ((ImageButton) view).setImageResource(R.drawable.heart_like);
                    offersSearchList.get(position).setFav(1);
                } else {
                    deleteFavoriteShow(offersSearchList.get(position));
                    ((ImageButton) view).setImageResource(R.drawable.heart_unlike);
                    offersSearchList.get(position).setFav(0);
                }
                break;
            case R.id.offerImage:
                Intent imageIntent = new Intent(getActivity(), ImageActivity.class);
                imageIntent.putExtra("url", offersSearchList.get(position).getImage());
                getActivity().startActivity(imageIntent);
                break;
        }
    }


    //Get favorites list
    public void getAllFavorites() {
        dbAdapter.open();
        offersFavoriteList = dbAdapter.getOffers();
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

}
