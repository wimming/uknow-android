package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xuewen.adapter.QRListAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.Question;
import com.xuewen.databaseservice.DatabaseHelper;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QQidResult;
import com.xuewen.networkservice.QRResult;
import com.xuewen.utility.ToastMsg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-23.
 */

public class RecommendationFragment extends Fragment {

    private List<QRBean> list = new ArrayList<>();
    private DatabaseHelper databaseHelper;
    private boolean networkLock = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_0, container, false);

        final  SwipeRefreshLayout refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Toast.makeText(getActivity(), "刷新一下", Toast.LENGTH_SHORT).show();
                refresh.setRefreshing(false);
                //handler -> update message
            }
        });

        final ListView listView = (ListView) rootView.findViewById(R.id.listView);
        final QRListAdapter adapter = new QRListAdapter(list, getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                intent.putExtra("id", ((QRBean) parent.getAdapter().getItem(position)).id);
                startActivity(intent);

            }
        });

        if (!networkLock) {

            // database service
            databaseHelper = DatabaseHelper.getHelper(getActivity());
            try {
                List<QRBean> records = databaseHelper.getQRBeanDao().queryForAll();
                list.clear();
                list.addAll(records);
                adapter.notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // network service
            ApiService apiService = ApiService.retrofit.create(ApiService.class);
            Call<QRResult> call = apiService.requestQR();

            call.enqueue(new Callback<QRResult>() {
                @Override
                public void onResponse(Call<QRResult> call, Response<QRResult> response) {
                    if (!response.isSuccessful()) {
                        Toast.makeText(getActivity(), ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (response.body().status != 200) {
                        Toast.makeText(getActivity(), response.body().errmsg, Toast.LENGTH_LONG).show();
                        return;
                    }
                    list.clear();
                    list.addAll(response.body().data);
                    adapter.notifyDataSetChanged();

                    // write back to database
                    try {
                        databaseHelper.getQRBeanDao().executeRaw("delete from tb_QR");
                        databaseHelper.getQRBeanDao().create(response.body().data);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    networkLock = true;
                }

                @Override
                public void onFailure(Call<QRResult> call, Throwable t) {
                    Toast.makeText(getActivity(), ToastMsg.NETWORK_ERROR + " : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }

        return rootView;
    }
}
