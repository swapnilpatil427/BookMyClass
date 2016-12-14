package com.example.swapn.bookmyclass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.swapn.bookmyclass.adapters.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.swapn.bookmyclass.fragments.test1Fragment;
import com.example.swapn.bookmyclass.fragments.test2Fragment;

import java.util.List;
import java.util.Vector;

public class TestActivity extends AppCompatActivity {
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initialisePaging();
    }

    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(Fragment.instantiate(this,test1Fragment.class.getName()));
        fragments.add(Fragment.instantiate(this,test2Fragment.class.getName()));

        mPagerAdapter = new PagerAdapter(this.getSupportFragmentManager(), fragments);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(mPagerAdapter);
    }





}
