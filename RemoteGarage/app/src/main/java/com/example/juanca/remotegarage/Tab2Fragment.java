package com.example.juanca.remotegarage;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

public class Tab2Fragment extends Fragment {
    private static final String TAG = "Tab2Fragment";

    private Switch simpleSwitch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.tab2_fragment,container,false);
        simpleSwitch = (Switch) view.findViewById(R.id.switch1);
        return view;
    }

    public boolean switchChecked(){
        return simpleSwitch.isChecked();
    }

}
