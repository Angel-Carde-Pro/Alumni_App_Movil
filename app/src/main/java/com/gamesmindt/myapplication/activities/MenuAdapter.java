package com.gamesmindt.myapplication.activities;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import nl.psdcompany.duonavigationdrawer.views.DuoOptionView;

class MenuAdapter extends BaseAdapter {
    private ArrayList<DuoOptionView> mOptionViews = new ArrayList<>();
    private ArrayList<String> mOptions = new ArrayList<>();

    public long getItemId(int i) {
        return (long) i;
    }

    MenuAdapter(ArrayList<String> arrayList) {
        this.mOptions = arrayList;
    }

    public int getCount() {
        return this.mOptions.size();
    }

    public Object getItem(int i) {
        return this.mOptions.get(i);
    }

    public void setViewSelected(int i, boolean z) {
        for (int i2 = 0; i2 < this.mOptionViews.size(); i2++) {
            if (i2 == i) {
                this.mOptionViews.get(i2).setSelected(z);
            } else {
                this.mOptionViews.get(i2).setSelected(!z);
            }
        }
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        DuoOptionView duoOptionView;
        String str = this.mOptions.get(i);
        if (view == null) {
            duoOptionView = new DuoOptionView(viewGroup.getContext());
        } else {
            duoOptionView = (DuoOptionView) view;
        }
        duoOptionView.bind(str, (Drawable) null, (Drawable) null);
        this.mOptionViews.add(duoOptionView);
        return duoOptionView;
    }
}
