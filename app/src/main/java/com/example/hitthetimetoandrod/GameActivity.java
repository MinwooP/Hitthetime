
package com.example.hitthetimetoandrod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GameActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNV;

    private FragmentManager fragmentManager;
    private GameFragment gameFragment;
    private FragmentTransaction transaction;

    private FirebaseDatabase database;
    private DatabaseReference databaseRef;
    private DataSnapshot arrayData;

    private List<FirebasePost> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        arrayList = new ArrayList<FirebasePost>();


        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference("/users/");
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue().toString();
                    double bestRecord = Double.parseDouble(snapshot.child("bestRecord").getValue().toString());
                    arrayList.add(new FirebasePost(name, bestRecord));
                    Log.d("LoginActivity", "Single ValueEventListener : " + snapshot.getValue()); //{bestRecord=64, name=박민우}'
                }

                 Collections.sort(arrayList, new Comparator<FirebasePost>() {

                    Collator collator = Collator.getInstance();

                    @Override
                    public int compare(FirebasePost fp1, FirebasePost fp2) {
                        if (fp1.getRecord() < fp2.getRecord()){
                            return -1;
                        } else if (fp1.getRecord() > fp2.getRecord()){
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("LoginActivity", "Single ValueEventListener Error");
            }
        });

        mBottomNV = findViewById(R.id.nav_view);
        // BottomNavigationHelper.disableShiftMode(mBottomNV);
        mBottomNV.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() { //NavigationItemSelected
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                BottomNavigate(menuItem.getItemId());
                return true;
            }
        });
        mBottomNV.setSelectedItemId(R.id.navigation_game);

    }


    private void BottomNavigate(int id) {  //BottomNavigation 페이지 변경
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            if (id == R.id.navigation_game) {
                fragment = new GameFragment();

            } else if (id == R.id.navigation_rank) {
                fragment = new RankFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("arrayList", (ArrayList<? extends Parcelable>) arrayList);

                fragment.setArguments(bundle);
            } else {
                fragment = new UserFragment();
            }
            fragmentTransaction.add(R.id.frameLayouts, fragment, tag);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();

    }
}
