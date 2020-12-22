
package com.minwooseonno.hitthetimetoandrod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private double userRecord;

    private List<FirebasePost> arrayList;
    private Boolean flag_dataChange = false;


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

                userRecord = Double.parseDouble(dataSnapshot.child(getIntent().getStringExtra("idToken")).child("bestRecord").getValue().toString());

                Collections.sort(arrayList, new Comparator<FirebasePost>() {

                    Collator collator = Collator.getInstance();

                    @Override
                    public int compare(FirebasePost fp1, FirebasePost fp2) {
                        if (fp1.getRecord() < fp2.getRecord()) {
                            return -1;
                        } else if (fp1.getRecord() > fp2.getRecord()) {
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
                Bundle bundle = new Bundle();
                bundle.putDouble("userRecord", userRecord);

                fragment.setArguments(bundle);
            }
            fragmentTransaction.add(R.id.frameLayouts, fragment, tag);
        } else {
            if(flag_dataChange && id == R.id.navigation_rank){

                fragmentTransaction.remove(fragment);
                fragment = new RankFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("arrayList", (ArrayList<? extends Parcelable>) arrayList);

                fragment.setArguments(bundle);
                flag_dataChange = false;
            }else{
                fragmentTransaction.show(fragment);
            }

        }

        fragmentTransaction.setPrimaryNavigationFragment(fragment);
        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNow();
    }

    // 마지막으로 뒤로 가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    // 첫 번째 뒤로 가기 버튼을 누를 때 표시
    private Toast toast;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // 기존 뒤로 가기 버튼의 기능을 막기 위해 주석 처리 또는 삭제

        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지났으면 Toast 출력
        // 2500 milliseconds = 2.5 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간에 2.5초를 더해 현재 시간과 비교 후
        // 마지막으로 뒤로 가기 버튼을 눌렀던 시간이 2.5초가 지나지 않았으면 종료
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
            toast = Toast.makeText(this,"이용해 주셔서 감사합니다.",Toast.LENGTH_LONG);
            toast.show();
            /*
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
             */
        }
    }
}
