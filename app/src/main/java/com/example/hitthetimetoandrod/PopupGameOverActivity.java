package com.example.hitthetimetoandrod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PopupGameOverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_game_over);
        TextView text_Result = (TextView)findViewById(R.id.text_end_result);
        Button btn_home = (Button) findViewById(R.id.image_home);
        Button btn_restart = (Button)findViewById(R.id.image_restart);
        Intent intent = getIntent();
        Double result = intent.getDoubleExtra("Result", 0);

        text_Result.setText(result.toString());
        text_Result.bringToFront();
        btn_home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        btn_restart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PopupGameOverActivity.this, TimeAttackActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }return true;
    }
    @Override
    public void onBackPressed(){

    }


}