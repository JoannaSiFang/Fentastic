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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robinhood.spark.SparkAdapter;
import com.robinhood.spark.SparkView;

import java.util.*;


public class ResultFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private final String[] HEALTH_INFO = {"SDNN", "RMSSD","SDSD","pNN50","pNN20","IQRNN","HTI","SKEW","KURT","AHRR","WS"};
    private SharedViewModel mSharedViewModel;
    private double[] historicData;
    private Spinner mySpinner;
    private SparkView sparkView;
    private MySparkAdapter sparkAdapter;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Map<String, TreeMap<String,Double>> healthInfoHistoryMap = new HashMap<>();

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
//        float[] dummydata = new float[20];
//        Random rd = new Random();
//        for (int i = 0; i < 20; i++){
//            dummydata[i] = 0;
//        }
        List<Double> dummydata = getHealthData(1);
        sparkAdapter = new MySparkAdapter();
        sparkAdapter.updateAllData(dummydata);
        sparkView.setAdapter(sparkAdapter);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(root.getContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        //set onchange function to database
        String userId = mAuth.getCurrentUser().getUid();
        final DatabaseReference currentUserHealthInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("healthInfo");
        for (int i = 0; i <= 10; i++) {
            healthInfoHistoryMap.put(HEALTH_INFO[i], new TreeMap());
            final DatabaseReference info = currentUserHealthInfo.child(HEALTH_INFO[i]);
            info.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String healthInfoName = info.getKey();
                    if (healthInfoName == null) {
                        return;
                    }
                    HashMap<String, Double> newMap = (HashMap) dataSnapshot.getValue();
                    TreeMap<String, Double> changedMap = new TreeMap();
                    changedMap.putAll(newMap);
                    healthInfoHistoryMap.put(healthInfoName, changedMap);
                    //System.out.println("dataSnapshot:" + newMap);
                    //System.out.println("healthInfoHistoryMap:" + healthInfoHistoryMap);
                    //System.out.println(healthInfoName+"Map:" + changedMap);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return root;
    }
    public void getHistoryData(String  key){

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String keyword = adapterView.getItemAtPosition(i).toString();
        //get data
        List<Double> dummydata = getHealthData(i);
//        float[] dummydata = new float[0];
//        Float[] dummy = (Float[]) vals.toArray();
//        System.out.println("dummy: "+ dummy.toString());

        sparkAdapter.updateAllData(dummydata);
        sparkView.setAdapter(sparkAdapter);


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private List<Double> getHealthData(int i) {
        TreeMap<String, Double> curInfo = healthInfoHistoryMap.get(HEALTH_INFO[i]);
        if (curInfo == null) {
            return new ArrayList<>();
        }
        Set<String> keys = curInfo.keySet();
        Iterator<String> iter = keys.iterator();

        List<Double> valueSortedByDate =  new ArrayList<>();

        while (iter.hasNext() ) {
            String key = iter.next();
            valueSortedByDate.add(curInfo.get(key));
        }
        System.out.println("the "+ HEALTH_INFO[i]+ " data is :" +valueSortedByDate.toString());
        return valueSortedByDate;
    }
}

class MySparkAdapter extends SparkAdapter {
    private List<Double> yData = new ArrayList<>();

    public MySparkAdapter(List<Double> yData) {
        this.yData = yData;
    }
    public MySparkAdapter(){

    }
    @Override
    public int getCount() {
        return yData.size();
    }

    @Override
    public Object getItem(int index) {
        return yData.get(index);
    }

    @Override
    public float getY(int index) {
        double d = yData.get(index);
        float f = (float) d;
        return f;
    }

    public void updateAllData(List<Double> data){
        yData = data;
    }
}