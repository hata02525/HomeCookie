package com.app.homecookie.Fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.homecookie.Activity.MainActivity;
import com.app.homecookie.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TermsFragment extends Fragment implements View.OnClickListener {

    View view;
    ImageView iv_back;
    Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_terms, container, false);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        activity = getActivity();
        return view;
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ((MainActivity) activity).replaceLoginFragment();
                break;
        }
    }
}
