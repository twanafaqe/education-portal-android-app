package com.eduportal.videoportal;

import java.util.ArrayList;

import com.eduportal.videoportal.DrawerMenu.NavDrawerItem;
import com.eduportal.videoportal.DrawerMenu.NavDrawerListAdapter;
import com.eduportal.videoportal.DrawerMenu.Fragment.DrawerHomeFragment;
import com.eduportal.videoportal.DrawerMenu.Fragment.DrawerMyPacketFragment;
import com.eduportal.videoportal.DrawerMenu.Fragment.DrawerProfileFragment;
import com.eduportal.videoportal.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	/* nav drawer ba�l��� */
	private CharSequence mDrawerTitle;

	/* Uygulaman�n ba�l���n� ge�ici olarak kullanmak i�in de�er tan�mland�. */
	private CharSequence mTitle;

	/* menu itemleri */
	private String[] navMenuTitles;
	
	/*
	 * TypedArray farkl� tipte nesneleri ayn� dizi i�inde tutmak i�in
	 * kullan�l�r. yani dizi elemanlar� homojen olmak zorunda de�ildir.
	 */
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		/*
		 * menu itemlerinin ve iconlar�n kaynaklar�n� ald�k.ObtainTypeArray ile
		 * resimlerin dizisini ald�k.
		 */
		navMenuTitles = new String[]{"Anasayfa","Paketlerim","T�m Paketler","Arama","Profil","��k��"};

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// DrawerHomeFragment
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0]));
		// DrawerLectureFragment
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], true,"12"));
		// C
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], true,"66"));
//		// D
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3]));
//		// E
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4]));
//		// F
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5]));
//		

		/* B�t�n iconlar� bir arrayde toplam�� olduk recycle() ile */
		//navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		/* GEREKLI ADAPTER SET EDILDI*/
		adapter = new NavDrawerListAdapter(getApplicationContext(),navDrawerItems);
		mDrawerList.setAdapter(adapter);

		/*
		 * ActionBar �zerindeki buton hem anasayfa butonu,hemde a��l�p kapanan
		 * yani toggle �eklinde �al��an buton haline geldi.
		 */
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));  

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.drawer_open, // nav drawer a��l�nca
				R.string.app_name // nav drawer kapan�nca
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);/* onPrepareOptionsMenu() yani standart menu �a�r�ld���nda g�sterilecek olan ba�l�k */ 
				invalidateOptionsMenu();/* invalidateOptionsMenu() �zerinde oynanm�� recycle menu tipini g�sterir */
				
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle("Menu");
				invalidateOptionsMenu();
			}
		};
		
		/* GEREKLI ADAPTER SET EDILDI*/
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			/* PROGRAM HER ACILDIGINDA EN USTTEKI MENU SECILI GELIR */
			displayView(0);
		}
	}

	
	/*
	 *	Menuye t�klandi�i zaman listener durumlar�n� yazal�m 
	 */
	private class SlideMenuClickListener implements	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			/* MENU ACILDIGINDA HANGISI SECILIRSE ONUN SAYFASI G�Z�K�R */
			displayView(position);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// menu toggle �eklinde kullan�l�rken menu getirilmesi
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// click i�lemi oldu�u zaman gelecek menu i�eri�i
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {			
		/* Menu a��ld���nda settings menusu a��lmas�n� engelledik */
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}


	/*
	 *	 Se�ilen menudeki itemin sayfas�n�n getirilme i�lemleri
	 *	(i�erik fragmenti)
	 */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new DrawerHomeFragment();
			break;
		case 1:
			fragment = new DrawerMyPacketFragment();
			break;
		case 2:
			fragment = new DrawerProfileFragment();
			break;
//		case 3:
//			fragment = new DFragment();
//			break;
//		case 4:
//			fragment = new EFragment();
//			break;
//		case 5:
//			fragment = new FFragment();
//			break;

		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();


			/* Se�ilen itemi ba�l���n� de�i�tir ve menuyu kapat  */
			
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 *  ActionBarDrawerToggle kullanilirken, de�i�imi anlamak i�in
	 *  kullan�lmas� gereken metodlar >> onPostCreate() ve  onConfigurationChanged()
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		/* onRestoreInstanceState durumu : Daha �nce kullan�p kaydedilen activiti onStart() durumunda son durumun devam edilmesini sa�lar */
		/* Burada onRestoreInstanceState durumu yakalan�r.Bu nedenle activty syncState() ile son durum senkronize edilir.  */
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		/* toggle de�i�ikliklerini yakalad�k */
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}