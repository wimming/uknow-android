package com.xuewen.xuewen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.UUidBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.networkservice.UUidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.ToastMsg;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-23.
 */

public class ProfileFragment extends Fragment {

    private ImageView aboutme_iv_setting;

    @BindView(R.id.avatarUrl) ImageView avatarUrl;
    @BindView(R.id.followedNum) TextView followedNum;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.status) TextView status;
    @BindView(R.id.description) TextView description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);

        ButterKnife.bind(this, rootView);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.aboutme_tbl);
        aboutme_iv_setting = (ImageView) rootView.findViewById(R.id.aboutme_iv_setting);
//        avatar = (ImageView) rootView.findViewById(R.id.avatar);

//        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, avatar, GlobalUtil.getInstance().circleBitmapOptions);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.aboutme_pager);

        // 两个tablayout嵌套的话，子的必须使用getChildPragmentManager
        // 不然和父的 PragmentManager冲突
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
                   return "我问 20";
               } else if (position == 0){
                   return "我答 20";
               } else {
                   return null;
               }
            }
        });
        tabLayout.setupWithViewPager(viewPager);

        aboutme_iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyInfoActivity.class);
                startActivity(intent);
            }
        });

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidResult> call = apiService.requestUUid(CurrentUser.userId);

        call.enqueue(new Callback<UUidResult>() {
            @Override
            public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {
                if (response.body().status != 200) {
                    Toast.makeText(getActivity(), response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }
                renderView(response.body().data);
            }

            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(getActivity(), ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

    private void renderView(UUidBean data) {

        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);
        username.setText(data.username);
        description.setText(data.description);
        status.setText(data.status);
        followedNum.setText(data.followedNum+"");

    }
}


//class NoScrollViewPager extends ViewPager {
//
//    public NoScrollViewPager(Context context) {
//        super(context);
//    }
//
//    public NoScrollViewPager(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return false;
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }
//
//}