package com.example.whtsappstatussaver.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.whtsappstatussaver.R;
import com.example.whtsappstatussaver.model.ModelHelp;

import java.util.ArrayList;


public class AdapterHelper extends Adapter<AdapterHelper.MyViewHolder> {
    private ArrayList<ModelHelp> modelHelpArrayList;

    class MyViewHolder extends ViewHolder {

        MyViewHolder(View view) {
            super(view);
        }
    }

    public AdapterHelper(ArrayList<ModelHelp> arrayList) {
        modelHelpArrayList = arrayList;
        Log.e("WhtsappHelp","init : "+ modelHelpArrayList.size());
    }

    @Override
    public int getItemCount() {
        Log.e("WhtsappHelp","Size : "+ modelHelpArrayList.size());
        return modelHelpArrayList.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Log.e("WhtsappHelp",""+i);
        ModelHelp helpModel = modelHelpArrayList.get(i);
  }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dialog_permission, viewGroup, false));
    }
}
