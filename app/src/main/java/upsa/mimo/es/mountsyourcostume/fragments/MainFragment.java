package upsa.mimo.es.mountsyourcostume.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.helpers.Pages;

/**
 * Created by JoseFelix on 22/06/2016.
 */
public class MainFragment extends Fragment {

    private static final String TAG = "MAIN_FRAGMENT";

    public MainFragment(){

    }

    public static MainFragment newInstance(){
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_main_content, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(view!=null){
            loadUI();
        }
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void loadUI() {

        TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        ViewPager pager = (ViewPager) getActivity().findViewById(R.id.view_pager);
        pager.setAdapter(new PagerAdapter(getActivity().getSupportFragmentManager()));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.setupWithViewPager(pager);
    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return FavouriteCostume.newInstance();
                case 1:
                    return SearchCostume.newInstance();
                default:
                    return null;
            }

            //return ContentFragment.newInstance(getDataType(position));
        }

        @Override public int getCount() {
            return Pages.values().length;
        }

        @Override public CharSequence getPageTitle(int position) {
            return Pages.values()[position].toString();
        }

    }

}
