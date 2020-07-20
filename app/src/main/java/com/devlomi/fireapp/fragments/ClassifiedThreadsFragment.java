/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package com.devlomi.fireapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.devlomi.fireapp.R;
import com.devlomi.fireapp.adapters.ClassifiedAdapter;
import com.devlomi.fireapp.model.ClassifiedModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by itzik on 6/17/2014.
 */
public class ClassifiedThreadsFragment extends BaseFragment {



    protected boolean inflateMenuItems = true;
    private LinearLayout llEmptyView;


    private List<ClassifiedModel> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private ClassifiedAdapter mAdapter;

    public static ClassifiedThreadsFragment newInstance() {
        ClassifiedThreadsFragment f = new ClassifiedThreadsFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    public Fragment ClassifiedThreadsFragment() {
        ClassifiedThreadsFragment f = new ClassifiedThreadsFragment();
        Bundle b = new Bundle();
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(inflateMenuItems);

      /*  NM.events().sourceOnMain()
                .filter(NetworkEvent.filterPrivateThreadsUpdated())
                .subscribe(networkEvent -> {
                    *//*if(tabIsVisible) {*//*
                    reloadData();
                    *//* }*//*
                });

        NM.events().sourceOnMain()
                .filter(NetworkEvent.filterType(EventType.TypingStateChanged))
                .subscribe(networkEvent -> {
                    *//*if(tabIsVisible) {*//*
                    adapter.setTyping(networkEvent.thread, networkEvent.text);
                    adapter.notifyDataSetChanged();
                    *//*}*//*
                });*/

    }





    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!inflateMenuItems)
            return;

        super.onCreateOptionsMenu(menu, inflater);
        MenuItem item = menu.add(Menu.NONE, R.id.action_chat_sdk_add, 10, getActivity().getString(R.string.add_conversation));
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setIcon(R.drawable.ic_plus);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

      View  mainView = inflater.inflate(R.layout.activity_classified_fragement, container, false);

       // initViews();

        //reloadData();


        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_view);
        mAdapter = new ClassifiedAdapter(list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareClassifiedModelData();


        return mainView;
    }
    private void prepareClassifiedModelData () {
        ClassifiedModel ClassifiedModel = new ClassifiedModel("Jobs", "Action & Adventure", "800");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Taxi", "Animation, Kids & Family", "015");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Electronics", "Action", "2115");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Mobiles", "Animation", "90");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Computers", "Science Fiction & Fantasy", "12");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Cars", "Action", "100");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Bikes", "Animation", "245");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Furniture", "Science Fiction", "123");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Property", "Animation", "324");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Matarimoni", "Action & Adventure", "234");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Fast Food", "Science Fiction", "345");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Sports", "Animation", "3453");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Hand Craft", "Science Fiction", "343");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Ayurveda", "Action & Adventure", "654");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("books", "Action & Adventure", "900");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Services", "Science Fiction & Fantasy", "20");
        list.add(ClassifiedModel);

        ClassifiedModel = new ClassifiedModel("Toys", "Science Fiction & Fantasy", "120");
        list.add(ClassifiedModel);
        mAdapter.notifyDataSetChanged();
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item){

        if (!inflateMenuItems)
            return super.onOptionsItemSelected(item);

        *//* Cant use switch in the library*//*
        int id = item.getItemId();

        if (id == R.id.action_chat_sdk_add) {
            InterfaceManager.shared().a.startSelectContactsActivity(getContext());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @Override
    public void onResume() {
        super.onResume();
       // adapter.notifyDataSetChanged();
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean showAds() {
        return false;
    }

    /*public void setTabVisibility (boolean isVisible) {
        super.setTabVisibility(isVisible);
        reloadData();
    }*/

}
