package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rd.PageIndicatorView;

import java.util.ArrayList;

import ca.jonnybauer.watched.Adapters.CreditsAdapter;
import ca.jonnybauer.watched.Models.Credit;
import ca.jonnybauer.watched.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreditsPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CreditsPage extends Fragment {

    private OnFragmentInteractionListener mListener;

    public CreditsPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credits_page, container, false);

        // Build the credits arraylist
        ArrayList<Credit> credits = new ArrayList<>();

        credits.add(new Credit(getString(R.string.api_title),getString(R.string.api_desc),getString(R.string.api_link)));
        credits.add(new Credit(getString(R.string.logo_title),getString(R.string.logo_desc),getString(R.string.logo_link)));
        credits.add(new Credit(getString(R.string.volley_title),getString(R.string.volley_desc),getString(R.string.volley_link)));
        credits.add(new Credit(getString(R.string.picasso_title),getString(R.string.picasso_desc),getString(R.string.picasso_link)));
        credits.add(new Credit(getString(R.string.gson_title),getString(R.string.gson_desc),getString(R.string.gson_link)));
        credits.add(new Credit(getString(R.string.rating_bar_title),getString(R.string.rating_bar_desc),getString(R.string.rating_bar_link)));


        // Get the viewPager from the layout
        ViewPager viewPager = view.findViewById(R.id.creditsVP);


        // Create the CreditsAdapter
        CreditsAdapter adapter = new CreditsAdapter(getChildFragmentManager(), credits);
        viewPager.setAdapter(adapter);

        // Get the pageviewIndicator
        PageIndicatorView pageIndicatorView = view.findViewById(R.id.credits_indicator);
        pageIndicatorView.setViewPager(viewPager);




        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public class CreditAnimation implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) {
                view.setAlpha(0f);
            } else if (position <= 0) {
                view.setAlpha(1 - position);
            } else if (position <= 1) {
                view.setAlpha(1 - position);
                view.setTranslationX(pageWidth * -position);
                float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);
            } else {
                view.setAlpha(0f);
            }
        }
    }

}
