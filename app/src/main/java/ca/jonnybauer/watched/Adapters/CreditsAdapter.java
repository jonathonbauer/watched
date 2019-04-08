package ca.jonnybauer.watched.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import ca.jonnybauer.watched.Models.Credit;
import ca.jonnybauer.watched.Pages.CreditFragment;
import ca.jonnybauer.watched.R;

public class CreditsAdapter extends FragmentPagerAdapter {
    private ArrayList<Credit> credits;


    // Constructor
    public CreditsAdapter(FragmentManager fm, ArrayList<Credit> credits) {
        super(fm);
        this.credits = credits;
    }

    @Override
    public Fragment getItem(int i) {
        return CreditFragment.newInstance(credits.get(i).getTitle(), credits.get(i).getDesc(), credits.get(i).getLink());
    }

    @Override
    public int getCount() {
        return credits.size();
    }



}
