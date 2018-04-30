package com.example.juanca.controlgaraje;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView txt;
    ImageButton ibtn1, ibtn2, ibtn3;
    AtomicBoolean status1, status2, status3;
    final int OPEN_GAR=0x00, CLOSE_GAR=0x01, OPEN_ENT=0x02,
            CLOSE_ENT=0x03, OPEN_BULB=0x04, CLOSE_BULB=0x05;
    //Socket s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt=(TextView)findViewById(R.id.textView3);
        ibtn1=(ImageButton)findViewById(R.id.imageButton2);
        ibtn2=(ImageButton)findViewById(R.id.imageButton7);
        ibtn3=(ImageButton)findViewById(R.id.imageButton);

        ibtn1.setOnClickListener(this);
        ibtn2.setOnClickListener(this);
        ibtn3.setOnClickListener(this);
        status1=new AtomicBoolean(true);
        status2=new AtomicBoolean(true);
        status3=new AtomicBoolean(true);
    }

    public void showText(String text) {
        Toast toast = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup)toast.getView();
        TextView messageTextView = (TextView)group.getChildAt(0);
        messageTextView.setTextSize(25);
        toast.show();
    }

    @Override
    public void onClick(View v) {
        Log.d("STATE", "BUTTON PRESSED!!");
        // default method for handling onClick Events..
        switch (v.getId()) {
            case R.id.imageButton2:
                if (status1.get()) {
                    ibtn1.setImageResource(R.drawable.close_garage);
                    status1.set(false);
                    showText("Abriendo entrada.");
                } else {
                    ibtn1.setImageResource(R.drawable.open_garage);
                    status1.set(true);
                    showText("Cerrando entrada.");
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket s = new Socket("juancalc.ddns.net", 8888);
                            //Socket s = new Socket("192.168.1.8", 8888);
                            DataOutputStream output = new DataOutputStream(s.getOutputStream());
                            if (!status1.get()) { output.write(OPEN_GAR); } else { output.write(CLOSE_GAR); }
                            s.close();
                        } catch (Exception ex) { txt.setText(ex.toString()); }
                    }
                }).start();
                break;
            case R.id.imageButton7:
                Log.d("STATE", "BUTTON 3 is Being executed!!");
                if (status2.get()) {
                    ibtn2.setImageResource(R.drawable.close_entrada);
                    status2.set(false);
                    showText("Abriendo entrada");
                } else {
                    ibtn2.setImageResource(R.drawable.open_entrada);
                    status2.set(true);
                    showText("Cerrando entrada");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket s = new Socket("juancalc.ddns.net", 8888);
                            //Socket s = new Socket("192.168.1.8", 8888);
                            DataOutputStream output = new DataOutputStream(s.getOutputStream());
                            if (!status2.get()) { output.write(OPEN_ENT); } else { output.write(CLOSE_ENT); }
                            s.close();
                        } catch (Exception ex) { txt.setText(ex.toString()); }
                    }
                }).start();
                break;
            case R.id.imageButton:
                txt.setText("Actuador accionado.");
                if (status3.get()) {
                    ibtn3.setImageResource(R.drawable.close_bulb);
                    status3.set(false);
                } else {
                    ibtn3.setImageResource(R.drawable.open_bulb);
                    status3.set(true);
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Socket s = new Socket("juancalc.ddns.net", 8888);
                            //Socket s = new Socket("192.168.1.8", 8888);
                            DataOutputStream output = new DataOutputStream(s.getOutputStream());
                            if (!status3.get()) { output.write(OPEN_BULB); } else { output.write(CLOSE_BULB); }
                            s.close();
                        } catch (Exception ex) {
                            txt.setText(ex.toString());
                        }
                    }
                }).start();
                break;
            default:
                break;

        }
    }
}
