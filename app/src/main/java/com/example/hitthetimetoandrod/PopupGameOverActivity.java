package com.example.hitthetimetoandrod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PopupGameOverActivity extends Activity {

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup_game_over);
        TextView text_Result = (TextView)findViewById(R.id.text_end_result);
        ImageButton btn_home = (ImageButton) findViewById(R.id.image_home);
        ImageButton btn_restart = (ImageButton) findViewById(R.id.image_restart);
        Intent intent = getIntent();
        String idToken = intent.getStringExtra("idToken");
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
                intent.putExtra("idToken", idToken);
                startActivity(intent);
                finish();
            }
        });

        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("/users/" + idToken);


        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object b = dataSnapshot.child("bestRecord").getValue();
                double bestRecord = Double.parseDouble(b.toString());
                if(bestRecord > result){
                    databaseRef.child("bestRecord").setValue(result);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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