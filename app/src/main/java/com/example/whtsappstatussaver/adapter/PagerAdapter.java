package com.example.whtsappstatussaver.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.whtsappstatussaver.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PagerAdapter extends FragmentPagerAdapter {
    private final ArrayList<String> stringArrayList = new ArrayList<>();
    private final ArrayList<Fragment> fragmentarraylist = new ArrayList<>();
    private Map<Integer, String> mStringFragmentTags;
    private FragmentManager mFragmentManager;
    private Activity WaActivity;

    public PagerAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        mFragmentManager = fm;
        mStringFragmentTags = new HashMap<>();
        this.WaActivity = activity;
    }


    @Override
    public Fragment getItem(int position) {
        return fragmentarraylist.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return stringArrayList.get(position);
    }


    @Override
    public int getCount() {
        return stringArrayList.size();
    }

    public void addTabs(String text, Fragment fragment) {
        stringArrayList.add(text);
        fragmentarraylist.add(fragment);
    }
    
    @Override
    public Object instantiateItem( ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            // record the fragment tag here.
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mStringFragmentTags.put(position, tag);
        }
        return obj;
    }
    @Override
    public int getItemPosition( Object object) {
        return POSITION_NONE;
    }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(WaActivity).inflate(
                R.layout.whatsapp_category_items, null);
        TextView tv = (TextView) tab.findViewById(R.id.tvThemeName);

        tv.setText(stringArrayList.get(position));
        return tab;
    }
}