package com.xuewen.xuewen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;

/**
 * Created by ym on 16-10-23.
 */

public class ProfileFragment extends Fragment {

    private ImageView aboutme_iv_setting;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.aboutme_tbl);

        NoScrollViewPager viewPager = (NoScrollViewPager) rootView.findViewById(R.id.aboutme_pager);

        // 两个tablayout嵌套的话，子的必须使用getChildPragmentManager
        //不然和父的 PragmentManager冲突
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {

                if (position == 1) {
                    return new AboutMeFragmentTwo();
                }
                return new AboutMeFragmentOne();
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
               if (position == 1) {
                   return "我提问";
               } else if (position == 0){
                   return "我回答";
               } else {
                   return null;
               }
            }
        });
        tabLayout.setupWithViewPager(viewPager);


        aboutme_iv_setting = (ImageView) rootView.findViewById(R.id.aboutme_iv_setting);
        aboutme_iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyInfoActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}


class NoScrollViewPager extends ViewPager{


    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }
}