package com.example.juanca.remotegarage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tab1Fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Tab1Fragment";

    Tab2Fragment tab2;
    ImageButton ibtn1, ibtn2, ibtn3;
    AtomicBoolean status1, status2, status3;
    final int OPEN_GAR=0x00, CLOSE_GAR=0x01, OPEN_ENT=0x02,
            CLOSE_ENT=0x03, OPEN_BULB=0x04, CLOSE_BULB=0x05;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);

        ibtn1=(ImageButton)view.findViewById(R.id.imageButton);
        ibtn2=(ImageButton)view.findViewById(R.id.imageButton2);
        ibtn3=(ImageButton)view.findViewById(R.id.imageButton3);


        ibtn1.setOnClickListener(this);
        ibtn2.setOnClickListener(this);
        ibtn3.setOnClickListener(this);
        status1=new AtomicBoolean(true);
        status2=new AtomicBoolean(true);
        status3=new AtomicBoolean(true);

        return view;
    }

    public void setInfoTab(Tab2Fragment tab2) {
        this.tab2 = tab2;
    }

    public void showText(String text) {
        Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup)toast.getView();
        TextView messageTextView = (TextView)group.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        AtomicBoolean status    =   new AtomicBoolean();
        int command             =   10;
        String host             =   "juancalc.ddns.net";

        switch (v.getId()) {
            case R.id.imageButton2:
                if (status2.get()) {
                    status2.set(false);
                    command=OPEN_GAR;
                    ibtn2.setImageResource(R.drawable.close_garage);
                    showText("Abriendo entrada.");
                } else {
                    status2.set(true);
                    command=CLOSE_GAR;
                    ibtn2.setImageResource(R.drawable.open_garage);
                    showText("Cerrando entrada.");
                }
                break;
            case R.id.imageButton3:
                if (status3.get()) {
                    status3.set(false);
                    command=OPEN_ENT;
                    ibtn3.setImageResource(R.drawable.close_entrada);
                    showText("Abriendo entrada");
                } else {
                    status3.set(true);
                    command=CLOSE_ENT;
                    ibtn3.setImageResource(R.drawable.open_entrada);
                    showText("Cerrando entrada");
                }
                break;
            case R.id.imageButton:
                if (status1.get()) {
                    status1.set(false);
                    command=OPEN_BULB;
                    ibtn1.setImageResource(R.drawable.close_bulb);
                    showText("Luz encendida.");
                } else {
                    status1.set(true);
                    command=CLOSE_BULB;
                    ibtn1.setImageResource(R.drawable.open_bulb);
                    showText("Luz apagada.");
                }
                break;
            default:
                break;
        }

        if (tab2.switchChecked()) { host = "192.168.1.8"; }
        new Thread(new MyRunnable(host, command, status)).start();
    }


    public class MyRunnable implements Runnable {
        final String KEY = "dGz3WWDriyUJbiK9vUNZuIbHZ0pt9S07";
        final int port;
        String host;
        int command;
        AtomicBoolean status;
        public MyRunnable(String host, int command, AtomicBoolean status) {
            this.port = 8888;
            this.host = host;
            this.command = command;
            this.status = status;
        }

        public void run() {
            try {
                Log.d("STATE", "ATTEMPTING TO CONNECT");
                Socket s = new Socket(host, port);
                DataOutputStream output = new DataOutputStream(s.getOutputStream());
                byte[] send = (KEY+"#"+String.valueOf(command)).getBytes();
                output.write(send);
                s.close();
            } catch (Exception ex) {
                Log.d("STATE", "ERROR CONNECTING?");
                Log.d("STATE", ex.toString());
            }
        }
    }
}
