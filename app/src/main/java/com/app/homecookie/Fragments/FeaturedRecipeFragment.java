package com.app.homecookie.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.homecookie.R;
import com.google.gson.JsonObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeaturedRecipeFragment extends Fragment {

    private View view;

    public static FeaturedRecipeFragment newInstance(Bundle bundle) {
        Bundle args = new Bundle();
        FeaturedRecipeFragment fragment = new FeaturedRecipeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_featured_recipe, container, false);
        return view;
    }
}

