package com.xuewen.xuewen;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xuewen.bean.QRBean;
import com.xuewen.bean.UUidBean;
import com.xuewen.bean.UUidFARBean;
import com.xuewen.fragment.MineFragment;
import com.xuewen.fragment.QuestionsRcmdFragment;
import com.xuewen.fragment.UsersRelatedFragment;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UpdateResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;

//    @BindView(R.id.searchView) SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setCustomView(getLayoutInflater().inflate(R.layout.activity_main_tab1, null));
        tabLayout.getTabAt(1).setCustomView(getLayoutInflater().inflate(R.layout.activity_main_tab2, null));
        tabLayout.getTabAt(2).setCustomView(getLayoutInflater().inflate(R.layout.activity_main_tab3, null));

//        Toast.makeText(MainActivity.this, ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getLargeMemoryClass()+"", Toast.LENGTH_SHORT).show();
//        Toast.makeText(MainActivity.this, ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getMemoryClass()+"", Toast.LENGTH_SHORT).show();

//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                startActivity(new Intent(MainActivity.this, QuestionsSearchActivity.class));
//                startActivity(new Intent(MainActivity.this, APITestActivity.class));
//            }
//        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
//                        .setAction("Action", null).show();
//            }
//        });

        retrieveUpdate();

    }

    private void retrieveUpdate() {

        String versionName = "";
        try {
            PackageManager pm = MainActivity.this.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(MainActivity.this.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UpdateResult> call = apiService.getUpdate(versionName);

        call.enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                if (response.body().status != 200) {
                    return;
                }

                if (response.body().data.need == 1) {
                    showUpdateDialog(response.body().data.newestVersion);
                }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {

            }
        });

    }

    private void showUpdateDialog(String newestVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setTitle("发现新版本："+newestVersion)
                .setMessage("是否立即下载？\n也可以前往 设置->版本更新 处更新")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse("http://uknow.online/uKnow.apk");
                        intent.setData(content_url);
                        startActivity(intent);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.options_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//
//        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);
//
//        SearchView searchView = null;
//        if (searchItem != null) {
//            searchView = (SearchView) searchItem.getActionView();
//        }
//        if (searchView != null) {
//            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
//        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment recommendationFragment;
        private Fragment searchingFragment;
        private Fragment profileFragment;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                if (recommendationFragment == null) {
                    recommendationFragment = new QuestionsRcmdFragment();
                }
                return recommendationFragment;
            }
            else if (position == 1) {
                if (searchingFragment == null) {
                    searchingFragment = new UsersRelatedFragment();
                }
                return searchingFragment;
            }
            else if (position == 2) {
                if (profileFragment == null) {
                    profileFragment = new MineFragment();
                }
                return profileFragment;
            }
            else {
                return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "推荐";
//                case 1:
//                    return "找人";
//                case 2:
//                    return "我的";
//            }
            return null;
        }
    }

    private static DataKeeper dataKeeper = new DataKeeper();
    public static DataKeeper getDataKeeper() {
        return dataKeeper;
    }
    public static class DataKeeper {
        public List<QRBean> questionsList = new ArrayList<>();
        public List<UUidFARBean> usersList = new ArrayList<>();
        public UUidBean mine;
        public boolean questionsCached = false;
        public boolean usersCached = false;
        public boolean mineCached = false;
    }

}
