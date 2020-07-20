package com.devlomi.fireapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.devlomi.fireapp.activities.main.calls.CallsFragment;
import com.devlomi.fireapp.activities.main.chats.FragmentChats;
import com.devlomi.fireapp.activities.main.status.StatusFragment;
import com.devlomi.fireapp.fragments.ClassifiedThreadsFragment;
import com.devlomi.fireapp.model.realms.User;
;import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    List<Fragment> fragmentList = new ArrayList<>();

    private int count = 4;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Override
    public Fragment getItem(int position) {



        switch (position){
            case 0:
                return new FragmentChats();
            case 1:
                return new StatusFragment();
            case 3:
                return new CallsFragment();
            case 2:
                return new ClassifiedThreadsFragment();
            default:
                throw new IllegalStateException("Not implemented Fragment exception");
        }

    }

    @Override
    public int getCount() {
        return count;

    }



}
