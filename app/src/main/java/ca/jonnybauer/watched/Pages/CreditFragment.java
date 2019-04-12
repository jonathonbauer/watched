package ca.jonnybauer.watched.Pages;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ca.jonnybauer.watched.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreditFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESC = "desc";
    private static final String ARG_LINK = "link";

    // TODO: Rename and change types of parameters
    private String mTitle;
    private String mDesc;
    private String mLink;

    private OnFragmentInteractionListener mListener;

    public CreditFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CreditFragment newInstance(String title, String desc, String link) {
        CreditFragment fragment = new CreditFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESC, desc);
        args.putString(ARG_LINK, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(ARG_TITLE);
            mDesc = getArguments().getString(ARG_DESC);
            mLink = getArguments().getString(ARG_LINK);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_credit, container, false);

        TextView title = view.findViewById(R.id.credit_title);
        TextView desc = view.findViewById(R.id.credit_desc);
        Button link = view.findViewById(R.id.credit_link);

        title.setText(mTitle);
        desc.setText(mDesc);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri website = Uri.parse(mLink);
                System.out.println(website);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(website);
                if(intent.resolveActivity(getContext().getPackageManager()) != null) {
                    getContext().startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
                }
            }
        });





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
}
