package com.app.homecookie.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.homecookie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllRecipesFragment extends Fragment {


    public AllRecipesFragment() {
        // Required empty public constructor
    }
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_recipes, container, false);

        activity = getActivity();
        return view;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {

    }




}
