package com.xuewen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.UUidBean;
import com.xuewen.databaseservice.DatabaseHelper;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UUidResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.Global;
import com.xuewen.utility.ToastMsg;
import com.xuewen.xuewen.MainActivity;
import com.xuewen.xuewen.ProfileActivity;
import com.xuewen.xuewen.R;
import com.xuewen.xuewen.SettingActivity;

import java.sql.SQLException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-23.
 */

public class MineFragment extends Fragment {

    private UUidBean data;
    private DatabaseHelper databaseHelper;

    @BindView(R.id.avatarUrl) ImageView avatarUrl;
    @BindView(R.id.followedNum) TextView followedNum;
    @BindView(R.id.username) TextView username;
    @BindView(R.id.status) TextView status;
    @BindView(R.id.description) TextView description;
    @BindView(R.id.aboutme_iv_edit) ImageView aboutme_iv_edit;
    @BindView(R.id.aboutme_iv_setting) ImageView aboutme_iv_setting;

    @BindView(R.id.appbar) AppBarLayout appbar;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, rootView);

        tabLayout = (TabLayout) rootView.findViewById(R.id.aboutme_tbl);
        viewPager = (ViewPager) rootView.findViewById(R.id.aboutme_pager);

        // 两个tablayout嵌套的话，子的必须使用getChildPragmentManager
        // 不然和父的 PragmentManager冲突
        viewPager.setAdapter(new AskAndAnswerAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        aboutme_iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        aboutme_iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // database service -> network service(不可见、开始刷新 -> 加载成功 -> 可见、结束刷新) -> write back to database
        // 内存中无数据，请求数据并缓存
        if (!MainActivity.getDataKeeper().mineCached) {

            // database service
            databaseHelper = DatabaseHelper.getHelper(getActivity());
            try {
                List<UUidBean> records = databaseHelper.getUUidBeanDao().queryForEq("id", CurrentUser.userId);
                if (records.size() > 1) {
                    Toast.makeText(getActivity(), ToastMsg.APPLICATION_ERROR, Toast.LENGTH_SHORT).show();
                }
                if (records.size() != 0) {
                    UUidBean record = records.get(0);
                    renderView(record);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            // retrieve data
            // 不可见 -> 加载成功 -> 可见
            appbar.setVisibility(View.INVISIBLE);
            requestAndRender();

        }
        // 内存中有数据，直接用
        else {

            data = MainActivity.getDataKeeper().mine;
            renderView(data);

        }

    }

    private void requestAndRender() {
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidResult> call = apiService.requestUUid(CurrentUser.userId);

        call.enqueue(new Callback<UUidResult>() {
            @Override
            public void onResponse(Call<UUidResult> call, Response<UUidResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(getActivity(), response.body().errmsg, Toast.LENGTH_SHORT).show();
                    return;
                }

                renderView(response.body().data);

                appbar.setVisibility(View.VISIBLE);

                data = response.body().data;

                // 缓存至内存
                MainActivity.getDataKeeper().mine = response.body().data;
                MainActivity.getDataKeeper().mineCached = true;

                // write back to database
                try {
                    databaseHelper.getUUidBeanDao().createOrUpdate(response.body().data);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UUidResult> call, Throwable t) {
                Toast.makeText(getActivity(), ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderView(UUidBean data) {
        ImageLoader.getInstance().displayImage(Global.getInstance().baseUrl+"static/avatar/"+data.avatarUrl, avatarUrl, Global.getInstance().circleBitmapOptions);
        username.setText(data.username);
        description.setText(data.description);
        status.setText(data.status);
        followedNum.setText(data.followedNum+"人关注");

        BaseAdapter answerAdapter = ((MineSubFragmentOne)((AskAndAnswerAdapter)viewPager.getAdapter()).aboutMeFragmentOne).dataListAdapter;
        BaseAdapter askAdapter = ((MineSubFragmentTwo)((AskAndAnswerAdapter)viewPager.getAdapter()).aboutMeFragmentTwo).dataListAdapter;
        List<UUidBean.Answer> answerList = ((MineSubFragmentOne)((AskAndAnswerAdapter)viewPager.getAdapter()).aboutMeFragmentOne).dataList;
        List<UUidBean.Asked> askedList = ((MineSubFragmentTwo)((AskAndAnswerAdapter)viewPager.getAdapter()).aboutMeFragmentTwo).dataList;

        if (data.answer != null && data.asked != null) {
            answerList.clear();
            answerList.addAll(data.answer);
            askedList.clear();
            askedList.addAll(data.asked);

            if (askAdapter != null && answerAdapter != null) {
                askAdapter.notifyDataSetChanged();
                answerAdapter.notifyDataSetChanged();
            }

            tabLayout.getTabAt(0).setText("我答 "+data.answer.size());
            tabLayout.getTabAt(1).setText("我问 "+data.asked.size());
        }

    }

    class AskAndAnswerAdapter extends FragmentPagerAdapter {

        public Fragment aboutMeFragmentTwo;
        public Fragment aboutMeFragmentOne;

        public AskAndAnswerAdapter(FragmentManager fm) {
            super(fm);
            aboutMeFragmentOne = new MineSubFragmentOne();
            aboutMeFragmentTwo = new MineSubFragmentTwo();
        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                if (aboutMeFragmentOne == null) {
                    aboutMeFragmentOne = new MineSubFragmentOne();
                }
                return aboutMeFragmentOne;
            }
            else if (position == 1) {
                if (aboutMeFragmentTwo == null) {
                    aboutMeFragmentTwo = new MineSubFragmentTwo();
                }
                return aboutMeFragmentTwo;
            }
            else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "我答";
            } else if (position == 1) {
                return "我问";
            } else {
                return null;
            }
        }
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