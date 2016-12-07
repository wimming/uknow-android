package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xuewen.adapter.QRListAdapter;
import com.xuewen.adapter.UserListAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.UUidFANDUUidRBean;
import com.xuewen.bean.UUidFARBean;
import com.xuewen.databaseservice.DatabaseHelper;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QRResult;
import com.xuewen.networkservice.UUidFARResult;
import com.xuewen.networkservice.UUidRResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.ToastMsg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-23.
 */

public class SearchingFragment extends Fragment {

    private List<UUidFARBean> dataList = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private boolean networkLock = false;
    private UserListAdapter adapter;

    @BindView(R.id.refresh) SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        ButterKnife.bind(this, rootView);

        databaseHelper = DatabaseHelper.getHelper(getActivity());
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestDataAndUpdateView();
                //refresh.setRefreshing(false);
                //handler -> update message
            }
        });

        final ListView listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new UserListAdapter(dataList, getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AskActivity.class);
                intent.putExtra("id", ((UUidFARBean) parent.getAdapter().getItem(position)).id);
                startActivity(intent);
            }
        });

        requestDataAndUpdateView();

        // database service -> network service(开始刷新 -> 加载成功 -> 结束刷新) -> write back to database
//        if (!networkLock) {
//
//            // database service
//            databaseHelper = DatabaseHelper.getHelper(getActivity());
//            try {
//                List<UUidFARBean> records = databaseHelper.getUUidFARBeanDao().queryForAll();
//                dataList.clear();
//                dataList.addAll(records);
//                adapter.notifyDataSetChanged();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//
//            // network service
//            // 开始刷新 -> 加载成功 -> 结束刷新
//            //refresh.setRefreshing(true);
//
//            ApiService apiService = ApiService.retrofit.create(ApiService.class);
//            Call<UUidFARResult> call = apiService.requestUUidFAR(CurrentUser.userId);
//
//            call.enqueue(new Callback<UUidFARResult>() {
//                @Override
//                public void onResponse(Call<UUidFARResult> call, Response<UUidFARResult> response) {
//                    if (!response.isSuccessful()) {
//                        Toast.makeText(getActivity(), ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    if (response.body().status != 200) {
//                        Toast.makeText(getActivity(), response.body().errmsg, Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    dataList.clear();
//                    dataList.addAll(response.body().data);
//                    adapter.notifyDataSetChanged();
//
//                    refresh.setRefreshing(false);
//
//                    networkLock = true;
//
//                    // write back to database
//                    try {
//                        databaseHelper.getUUidFARBeanDao().executeRaw("delete from tb_UUidFAR");
//                        databaseHelper.getUUidFARBeanDao().create(response.body().data);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<UUidFARResult> call, Throwable t) {
//                    Toast.makeText(getActivity(), ToastMsg.NETWORK_ERROR + " : " + t.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            });
//
//        }

        return rootView;
    }


    private void requestDataAndUpdateView() {

        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidFARResult> call = apiService.requestUUidFAR(CurrentUser.userId);

        call.enqueue(new Callback<UUidFARResult>() {
            @Override
            public void onResponse(Call<UUidFARResult> call, Response<UUidFARResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    updateFromDB();
                    refresh.setRefreshing(false);
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(getActivity(), response.body().errmsg, Toast.LENGTH_LONG).show();
                    updateFromDB();
                    refresh.setRefreshing(false);
                    return;
                }
                dataList.clear();
                dataList.addAll(response.body().data);
                adapter.notifyDataSetChanged();

                refresh.setRefreshing(false);

                networkLock = true;

                // write back to database
                try {
                    databaseHelper.getUUidFARBeanDao().executeRaw("delete from tb_UUidFAR");
                    databaseHelper.getUUidFARBeanDao().create(response.body().data);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<UUidFARResult> call, Throwable t) {
                Toast.makeText(getActivity(), ToastMsg.NETWORK_ERROR + " : " + t.getMessage(), Toast.LENGTH_LONG).show();
                updateFromDB();
                refresh.setRefreshing(false);
            }
        });
    }

    private void updateFromDB() {
        try {
            List<UUidFARBean> records = databaseHelper.getUUidFARBeanDao().queryForAll();
            dataList.clear();
            dataList.addAll(records);
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
