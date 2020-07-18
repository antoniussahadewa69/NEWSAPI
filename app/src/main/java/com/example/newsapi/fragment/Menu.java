package com.example.newsapi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.newsapi.R;
import com.example.newsapi.adapter.MenuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * Created by antoniuskrisnasahadewa on 17/06/2020.
 */

public class Menu extends Fragment {
    Context mContext;
    Realm mRealm;
    private List<String> menu;
    String[] eArray;
    @BindView(R.id.recyclerViewMenu)
    RecyclerView rvMenu;
    private MenuAdapter menuAdapter;

    public Menu() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mContext = getContext();
        eArray = getActivity().getResources().getStringArray(R.array.Category);
        initializeData();
        return view;
    }

    private void setRecyclerView(){
        menuAdapter = new MenuAdapter(mContext, menu);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvMenu.setLayoutManager(layoutManager);
        rvMenu.setItemAnimator(new DefaultItemAnimator());
        rvMenu.setAdapter(menuAdapter);
    }

    public void initializeData() {
        menu=new ArrayList<String>();
        for (int i = 0; i<eArray.length ;i++){
            menu.add(eArray[i]);
        }
        setRecyclerView();
    }
}
