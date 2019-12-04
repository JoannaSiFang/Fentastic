package com.android.ethetiqs.fentastic.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.ethetiqs.fentastic.R;
import com.android.ethetiqs.fentastic.ui.SharedViewModel;
import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResultFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private SharedViewModel mSharedViewModel;
    private double[] historicData;
    private Spinner mySpinner;
    private SparkView sparkView;
    private MySparkAdapter sparkAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mSharedViewModel =
                ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_result, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        mSharedViewModel.getInputDataId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        //implement spinner function
        mySpinner = (Spinner) root.findViewById(R.id.spinner1);
        mySpinner.setOnItemSelectedListener(this);
        sparkView = (SparkView) root.findViewById(R.id.sparkview);
        float[] dummydata = new float[20];
        Random rd = new Random();
        for (int i = 0; i < 20; i++){
            dummydata[i] = 0;
        }
        sparkAdapter = new MySparkAdapter();
        //sparkView.setAdapter(sparkAdapter);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(root.getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        return root;
    }
    public void getHistoryData(String  key){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String keyword = adapterView.getItemAtPosition(i).toString();
        float[] dummydata = new float[20];
        Random rd = new Random();
        for (int j = 0; j < 20; j++){
            dummydata[j] = rd.nextInt(10000)/100;
        }
        sparkAdapter.updateAllData(dummydata);
        sparkView.setAdapter(sparkAdapter);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

class MySparkAdapter extends SparkAdapter {
    private float[] yData;

    public MySparkAdapter(float[] yData) {
        this.yData = yData;
    }
    public MySparkAdapter(){

    }
    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }

    public void updateAllData(float[] data){
        yData = data;
    }
}