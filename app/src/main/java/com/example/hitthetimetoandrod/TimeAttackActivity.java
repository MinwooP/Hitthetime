package com.example.hitthetimetoandrod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.time.Duration;
import java.util.Random;
import java.util.StringTokenizer;

//진짜 게임을 하는 부분
public class TimeAttackActivity extends AppCompatActivity {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Button btn_Start, btn_Stop, btn_Reset;
    private LottieAnimationView animationView;
    private TextView text_Goal, text_Now, text_Score1, text_Score2, text_Score3, text_Score4, text_Score5;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    Handler handler;
    int Seconds, Minutes, MilliSeconds;
    private Thread timeThread = null;
    private Boolean isRunning = true;

    Random random = new Random();
    int max_num_value_sec = 3;
    int min_num_value_sec = 1;
    int[] arr_random_sec = new int[5];
    int[] arr_random_milisec = new int[5];
    int COUNT_GAME = 0;
    String goalTime = "";
    double RESULT = 0;

    boolean flag_start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_attack);
        handler = new Handler();
        btn_Start = (Button) findViewById(R.id.btn_start);
        btn_Stop = (Button) findViewById(R.id.btn_stop);

        text_Now = (TextView) findViewById(R.id.text_nowTime);
        text_Goal = (TextView) findViewById(R.id.text_goalTime);

        text_Score1 = (TextView) findViewById(R.id.score1);
        text_Score2 = (TextView) findViewById(R.id.score2);
        text_Score3 = (TextView) findViewById(R.id.score3);
        text_Score4 = (TextView) findViewById(R.id.score4);
        text_Score5 = (TextView) findViewById(R.id.score5);


        COUNT_GAME = 0;

        for (int i = 0; i < 5; i++) {
            arr_random_sec[i] = random.nextInt(max_num_value_sec - min_num_value_sec + 1) + min_num_value_sec;
            arr_random_milisec[i] = random.nextInt(100);
        }
        goalTime = arr_random_sec[COUNT_GAME] + ":" + arr_random_milisec[COUNT_GAME] + "";

        animationView = (LottieAnimationView) findViewById(R.id.animation_view);

        btn_Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTime = SystemClock.uptimeMillis();
                handler.postDelayed(runnable, 0);

                btn_Start.setEnabled(false);
                animationView.setAnimation("clock1.json");
                animationView.playAnimation();
                animationView.setSpeed(0.64f);
                animationView.setRepeatCount(1);

                text_Goal.setText(goalTime);
                flag_start = true;
            }
        });


        btn_Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag_start) {
                    handler.removeCallbacks(runnable); // 핸들러 지운다.
                    String temp = (String) text_Now.getText();
                    String[] tokens = temp.split(":");
                    int score = Math.abs((arr_random_sec[COUNT_GAME] - Integer.parseInt(tokens[0]))) * 100 + Math.abs((arr_random_milisec[COUNT_GAME] - Integer.parseInt(tokens[1])));
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.INVISIBLE);
                    if (score > 100) { // 목표 시간보다 +1초 초과, -1초 미만 차이날 경우
                        animationView.setAnimation("6952-fail.json");
                        animationView.setVisibility(View.VISIBLE);
                        animationView.playAnimation();
                    } else {
                        animationView.setAnimation("6951-success.json");
                        animationView.setVisibility(View.VISIBLE);
                        animationView.playAnimation();
                    }
                    RESULT += score;


                    COUNT_GAME += 1;
                    if (COUNT_GAME < 5) {
                        goalTime = arr_random_sec[COUNT_GAME] + ":" + arr_random_milisec[COUNT_GAME] + "";
                        text_Goal.setText(goalTime);
                    }

                    if (COUNT_GAME != 6) {//COUNT GAME이 5가 아닐때, 게임이 진행중일 때
                        if (COUNT_GAME == 1) {
                            Log.e("COUNT_GAME STATE", "COUNT GAME 1" + score);
                            text_Score1.setText(score + "");
                        } else if (COUNT_GAME == 2) {
                            Log.e("COUNT_GAME STATE", "COUNT GAME 2" + score);
                            text_Score2.setText(score + "");
                        } else if (COUNT_GAME == 3) {
                            Log.e("COUNT_GAME STATE", "COUNT GAME 3" + score);
                            text_Score3.setText(score + "");
                        } else if (COUNT_GAME == 4) {
                            Log.e("COUNT_GAME STATE", "COUNT GAME 4" + score);
                            text_Score4.setText(score + "");
                        } else if (COUNT_GAME == 5) {
                            Log.e("COUNT_GAME STATE", "COUNT GAME 5" + score);
                            text_Score5.setText(score + "");
                            handler.removeCallbacks(runnable);
                            String idToken = getIntent().getStringExtra("idToken");
                            Intent intent = new Intent(TimeAttackActivity.this, PopupGameOverActivity.class);
                            intent.putExtra("Result", RESULT);
                            intent.putExtra("idToken", idToken);
                            finish();
                            startActivity(intent);
                        }
                    }
                    Handler timer = new Handler();
                    timer.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            StartTime = SystemClock.uptimeMillis();
                            MillisecondTime = 0;
                            UpdateTime = 0;
                            handler.postDelayed(runnable, 0);
                        }
                    }, 500);

                }
            }

        });
    }

    public Runnable runnable = new Runnable() {
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Seconds = Seconds % 60;
            if (Seconds > 9) {
                btn_Stop.post(new Runnable() {
                    @Override
                    public void run() {
                        btn_Stop.performClick();
                    }
                });

            }
            MilliSeconds = (int) (UpdateTime % 100);
            text_Now.setText(
                    String.format("%01d", Seconds) + ":"
                            + String.format("%02d", MilliSeconds));
            handler.postDelayed(this, 0);

        }
    };

}