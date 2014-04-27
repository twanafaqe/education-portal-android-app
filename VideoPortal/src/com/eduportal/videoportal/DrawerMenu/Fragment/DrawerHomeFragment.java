package com.eduportal.videoportal.DrawerMenu.Fragment;


import com.eduportal.videoportal.R;
import android.app.Fragment;
import android.content.Intent;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class DrawerHomeFragment extends Fragment {
	
	
	public DrawerHomeFragment(){}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
     View rootView = inflater.inflate(R.layout.drawer_home_fragment, container, false);

   
       
        return rootView;
    }

	
}
