package com.example.hitthetimetoandrod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

public class GameActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private GameFragment gameFragment;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        fragmentManager = getSupportFragmentManager();

        gameFragment = new GameFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, gameFragment).commitAllowingStateLoss();
    }
}