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
public class AddIngredientsFragment extends Fragment {


    public AddIngredientsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_ingredients, container, false);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

}
