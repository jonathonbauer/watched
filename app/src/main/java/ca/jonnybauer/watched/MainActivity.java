package ca.jonnybauer.watched;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import ca.jonnybauer.watched.Pages.SearchPage;
import ca.jonnybauer.watched.Pages.UpcomingPage;
import ca.jonnybauer.watched.Pages.WatchListPage;

public class MainActivity extends AppCompatActivity implements
        SearchPage.OnFragmentInteractionListener,
        UpcomingPage.OnFragmentInteractionListener,
        WatchListPage.OnFragmentInteractionListener {



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            // Fragment Objects
            Fragment selectedFragment;
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
                case R.id.navigation_search:
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
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
