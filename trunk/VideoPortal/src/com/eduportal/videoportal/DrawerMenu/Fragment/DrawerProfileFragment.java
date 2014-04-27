package com.eduportal.videoportal.DrawerMenu.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eduportal.videoportal.R;

public class DrawerProfileFragment extends Fragment {
	
	public DrawerProfileFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.drawer_profile_fragment, container, false);
         
        return rootView;
    }
}
