package com.example.hitthetimetoandrod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn_Start, btn_Stop, btn_Reset;
    private TextView text_Goal, text_Now;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    private Thread timeThread = null;
    private Boolean isRunning = true;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameFragment newInstance(String param1, String param2) {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



        handler = new Handler();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        btn_Start = (Button) v.findViewById(R.id.btn_start);
        btn_Stop = (Button) v.findViewById(R.id.btn_stop);
        btn_Reset = (Button) v.findViewById(R.id.btn_reset);
        text_Goal = (TextView) v.findViewById(R.id.text_targetTime);
        text_Now = (TextView) v.findViewById(R.id.text_nowTime);
        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);
                btn_Reset.setEnabled(false);
                btn_Start.setEnabled(false);
            }
        });

        btn_Reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                MillisecondTime = 0L;
                StartTime = 0L;
                TimeBuff = 0L;
                UpdateTime = 0L;
                Seconds = 0;
                Minutes = 0;
                MilliSeconds = 0;

                text_Now.setText("00:00:00");
                btn_Start.setEnabled(true);
            }
        });
        btn_Stop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                handler.removeCallbacks(runnable);
                btn_Reset.setEnabled(true);
            }
        });
        return v;
    }

    public Runnable runnable = new Runnable(){
        public void run(){
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds%60;
            MilliSeconds = (int) (UpdateTime % 1000);
            text_Now.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));
            handler.postDelayed(this, 0);
        }
    };
}