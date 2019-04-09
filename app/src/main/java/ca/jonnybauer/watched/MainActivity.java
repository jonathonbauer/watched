package ca.jonnybauer.watched;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ca.jonnybauer.watched.Pages.CreditFragment;
import ca.jonnybauer.watched.Pages.CreditsPage;
import ca.jonnybauer.watched.Pages.SearchPage;
import ca.jonnybauer.watched.Pages.TheatresPage;
import ca.jonnybauer.watched.Pages.UpcomingPage;
import ca.jonnybauer.watched.Pages.WatchListPage;
import ca.jonnybauer.watched.Pages.MoviePopUp;

public class MainActivity extends AppCompatActivity implements
        SearchPage.OnFragmentInteractionListener,
        UpcomingPage.OnFragmentInteractionListener,
        WatchListPage.OnFragmentInteractionListener,
        MoviePopUp.OnFragmentInteractionListener,
        TheatresPage.OnFragmentInteractionListener,
        CreditsPage.OnFragmentInteractionListener,
        CreditFragment.OnFragmentInteractionListener {

    Fragment selectedFragment;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.menu_search:
                // Fragment Objects
                Fragment selectedFragment;
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.setCustomAnimations(R.anim.move_in, R.anim.move_out, R.anim.move_back_in, R.anim.move_back_out);

                selectedFragment = fm.findFragmentByTag("Search");
                if(selectedFragment == null) {
                    transaction.replace(R.id.main_content, new SearchPage(), "Search");

                } else if(!selectedFragment.isVisible()) {
                    transaction.replace(R.id.main_content, selectedFragment);
                }

                setTitle(getString(R.string.search_page_title));
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            case R.id.menu_credits:
                // Fragment Objects
                Fragment selectedFragment2;
                FragmentManager fm2 = getSupportFragmentManager();
                FragmentTransaction transaction2 = fm2.beginTransaction();
                transaction2.setCustomAnimations(R.anim.move_in, R.anim.move_out, R.anim.move_back_in, R.anim.move_back_out);

                selectedFragment2 = fm2.findFragmentByTag("Credits");
                if(selectedFragment2 == null) {
                    transaction2.replace(R.id.main_content, new CreditsPage(), "Credits");

                } else if(!selectedFragment2.isVisible()) {
                    transaction2.replace(R.id.main_content, selectedFragment2);
                }

//                setTitle(getString(R.string.credit_title));
                setTitle(getString(R.string.credit_title));
                transaction2.addToBackStack(null);
                transaction2.commit();
                return true;

            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, SettingsActivity.GeneralPreferenceFragment.class.getName());
                intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true);
                startActivity(intent);

                default:
                    return super.onOptionsItemSelected(item);
        }


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Fragment Objects

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_upcoming:
                    selectedFragment = fm.findFragmentByTag("Upcoming");
                    if(selectedFragment == null) {
                        transaction.replace(R.id.main_content, new UpcomingPage(), "Upcoming");

                    } else if(!selectedFragment.isVisible()) {
                        transaction.replace(R.id.main_content, selectedFragment);
                    }
                    setTitle(getString(R.string.upcoming_page_title));
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;

                case R.id.navigation_watch_list:
                    selectedFragment = fm.findFragmentByTag("Watch List");
                    if(selectedFragment == null) {
                        transaction.replace(R.id.main_content, new WatchListPage(), "Watch List");

                    } else if(!selectedFragment.isVisible()) {
                        transaction.replace(R.id.main_content, selectedFragment);
                    }
                    setTitle(getString(R.string.watch_list_page_title));
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;

                case R.id.navigation_theatres:
                    selectedFragment = fm.findFragmentByTag("Theatres");
                    if(selectedFragment == null) {
                        transaction.replace(R.id.main_content, new TheatresPage(), "Theatres");

                    } else if(!selectedFragment.isVisible()) {
                        transaction.replace(R.id.main_content, selectedFragment);
                    }
                    setTitle(getString(R.string.theatres_page_title));
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;

            }

            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        selectedFragment = fm.findFragmentByTag("Watch List");
        if(selectedFragment == null) {
            transaction.replace(R.id.main_content, new WatchListPage(), "Watch List");

        } else if(!selectedFragment.isVisible()) {
            transaction.replace(R.id.main_content, selectedFragment);
        }
        setTitle(getString(R.string.watch_list_page_title));

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_watch_list);

        transaction.addToBackStack(null);
        transaction.commit();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Change the actionbar font

        // Fragment Objects
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        selectedFragment = fm.findFragmentByTag("Watch List");
        if(selectedFragment == null) {
            transaction.replace(R.id.main_content, new WatchListPage(), "Watch List");

        } else if(!selectedFragment.isVisible()) {
            transaction.replace(R.id.main_content, selectedFragment);
        }
        setTitle(getString(R.string.watch_list_page_title));
        transaction.addToBackStack(null);
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_watch_list);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
