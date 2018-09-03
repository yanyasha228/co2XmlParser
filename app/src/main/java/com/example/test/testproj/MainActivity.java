package com.example.test.testproj;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.test.testproj.helpers.ConnectivityHelper;

/**
 * Main appActivity
 *
 * @author Ruslan Zosimov
 * @version 1.0
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final static String TOOLBAR_TITLES = "TOOLBAR_TITLES";
    private final static String VISIBLE = "VISIBLE";

    private String titels;
    Toolbar toolbar;
    private MainFragment mainFragment;
    private FavoritesFragment favoritesFragment;
    private PriceChangingFragment priceChangingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (savedInstanceState != null) titels = savedInstanceState.getString(TOOLBAR_TITLES);
        else {
            replaceFragment(mainFragment = new MainFragment(), true);
            titels = "Главная";
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        //Fragments backStackChangedListener
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fragmentManager = getSupportFragmentManager();

                Fragment fragment = fragmentManager.findFragmentByTag(VISIBLE);

                if (fragment instanceof MainFragment) {
                    titels = "Главная";
                    navigationView.setCheckedItem(R.id.nav_main);
                } else if(fragment instanceof FavoritesFragment){
                    titels = "Выбранные";
                    navigationView.setCheckedItem(R.id.nav_favorites);
                }else if (fragment instanceof PriceChangingFragment){
                    titels = "Изменение цены";
                    navigationView.setCheckedItem(R.id.nav_changing_offers_price);
                }
                toolbar.setTitle(titels);

            }

        });
        toolbar.setTitle(titels);

        if (!new ConnectivityHelper(this).isConnected())
            Toast.makeText(this, "Ожидание соединения......", Toast.LENGTH_SHORT).show();

    }

    //Replacing fragment func
    private void replaceFragment(Fragment fragment, boolean addToStack) {
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContentContainer, fragment, VISIBLE);
        if (addToStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    // Clearing fragments "backStack"
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 1) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(VISIBLE);
        switch (id) {
            case R.id.nav_main:
                if (fragment instanceof MainFragment) drawer.closeDrawer(GravityCompat.START);
                else {
                    if (mainFragment == null)
                        replaceFragment(mainFragment = new MainFragment(), false);
                    else replaceFragment(mainFragment, false);
                    titels = "Главная";
                    toolbar.setTitle(R.string.nav_mains);
                    drawer.closeDrawer(GravityCompat.START);
                    mainFragment.getOfferListWithFavoritesValidation();
                    //If drawerLayout -> click "Main" -> in fragments backStack remains only MainFragment
                    //This helps to avoid an infinite set of fragments

                }
                break;

            case R.id.nav_favorites:
                if (fragment instanceof FavoritesFragment) drawer.closeDrawer(GravityCompat.START);

                else {
                    if (favoritesFragment == null)
                        replaceFragment(favoritesFragment = new FavoritesFragment(), false);
                    else replaceFragment(favoritesFragment, false);
                    titels = "Выбранные";
                    toolbar.setTitle(R.string.nav_favoritess);
                    drawer.closeDrawer(GravityCompat.START);

                }
                break;

            case R.id.nav_changing_offers_price:
                if (fragment instanceof PriceChangingFragment)
                    drawer.closeDrawer(GravityCompat.START);
                else {
                    if (priceChangingFragment == null)
                        replaceFragment(priceChangingFragment = new PriceChangingFragment(), false);
                    else replaceFragment(priceChangingFragment, false);
                    titels = "Изменение цены";
                    toolbar.setTitle(R.string.nav_changing_offers_price);
                    drawer.closeDrawer(GravityCompat.START);

                }
                break;

            case R.id.nav_createXml:

                Intent intent = new Intent(this, CreateOffersXmlActivity.class);
                drawer.closeDrawer(GravityCompat.START);

                startActivity(intent);
                break;
        }


        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TOOLBAR_TITLES, titels);
    }


}
