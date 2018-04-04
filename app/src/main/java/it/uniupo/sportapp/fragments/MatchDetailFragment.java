package it.uniupo.sportapp.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.uniupo.sportapp.MainActivity;
import it.uniupo.sportapp.R;
import it.uniupo.sportapp.Singleton;

/**
 * create an instance of this fragment.
 */
public class MatchDetailFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "match";
    private static final String ARG_PARAM2 = "season";
    private static final String ARG_PARAM3 = "key";
    private String matchIndex, seasonIndex, pickers;
    private String roomKey;


    public MatchDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchIndex = getArguments().getString(ARG_PARAM1);
            seasonIndex = getArguments().getString(ARG_PARAM2);
            roomKey = getArguments().getString(ARG_PARAM3);
            pickers = getArguments().getString("pickers");
            Log.d("mDet", seasonIndex );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_match_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewPager viewPager = view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.match_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        Log.d("MDF", matchIndex);
        Bundle b = new Bundle();
        b.putString("pickers", pickers);
        b.putString("match", matchIndex);
        b.putString("season", seasonIndex);
        b.putString("room", roomKey);
        MatchInfoTabFragment matchInfoTabFragment = new MatchInfoTabFragment();
        matchInfoTabFragment.setArguments(b);
        MatchChatTabFragment matchChatTabFragment = new MatchChatTabFragment();
        matchChatTabFragment.setArguments(b);
        adapter.addFragment(matchInfoTabFragment, "Details");
        adapter.addFragment(matchChatTabFragment, "Chat");
        viewPager.setAdapter(adapter);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_profile:
                ((MainActivity)getActivity()).addFragment(new ProfileFragment());
                return true;
            case R.id.action_delete_match:
                SeasonDetailFragment sFragment = new SeasonDetailFragment();
                Bundle args = new Bundle();
                args.putString("key", roomKey);
                args.putString("season", seasonIndex);
                sFragment.setArguments(args);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("rooms").child(Singleton.getCurrentRoom().getRoomKey()).child("existingSeasons").child(seasonIndex).child("seasonMatches").child(matchIndex).removeValue();
                ((MainActivity)getActivity()).addFragment(sFragment);
            default: return super.onOptionsItemSelected(item);
        }
    }
}
