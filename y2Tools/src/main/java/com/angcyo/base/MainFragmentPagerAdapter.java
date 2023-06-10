package com.angcyo.base;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;


public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	private ArrayList<Fragment> list;

	public MainFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	public MainFragmentPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragmentList) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.list = fragmentList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
