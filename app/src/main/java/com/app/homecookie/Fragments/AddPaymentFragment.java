package com.app.homecookie.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.homecookie.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPaymentFragment extends Fragment {

    View view;

    public AddPaymentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_payment, container, false);

        return view;
    }

}
