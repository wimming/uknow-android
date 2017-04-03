package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.xuewen.adapter.UsersListAdapter;
import com.xuewen.bean.UUidFARBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UUidFARResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.ToastMsg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by huangyuming on 17-3-28.
 */

public class UsersSearchActivity extends AppCompatActivity {

    private List<UUidFARBean> resultList = new ArrayList<>();
    private UsersListAdapter resultAdapter;

    @BindView(R.id.searchView)
    SearchView searchView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.resultListView)
    ListView resultListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_search_for_user);

        ButterKnife.bind(this);

        // 下拉刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveUsersAndRender("");
            }
        });

        // 结果列表
        resultAdapter = new UsersListAdapter(resultList, UsersSearchActivity.this);
        resultListView.setAdapter(resultAdapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(UsersSearchActivity.this, QuestionAskActivity.class);
                intent.putExtra("id", ((UUidFARBean) parent.getAdapter().getItem(position)).id);
                startActivity(intent);

            }
        });

        // 搜索框
//        searchView.onActionViewExpanded();
//        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(UsersSearchActivity.this, query, Toast.LENGTH_SHORT).show();
                retrieveUsersAndRender(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(UsersSearchActivity.this, newText, Toast.LENGTH_SHORT).show();
                retrieveUsersAndRender(newText);
                return false;
            }
        });

    }

    private void retrieveUsersAndRender(String query) {
        refresh.setRefreshing(true);
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<UUidFARResult> call = apiService.requestUUidFAR(CurrentUser.userId);
        call.enqueue(new Callback<UUidFARResult>() {
            @Override
            public void onResponse(Call<UUidFARResult> call, Response<UUidFARResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(UsersSearchActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    refresh.setRefreshing(false);
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(UsersSearchActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    refresh.setRefreshing(false);
                    return;
                }
                resultList.clear();
                resultList.addAll(response.body().data);
                resultAdapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<UUidFARResult> call, Throwable t) {
                Toast.makeText(UsersSearchActivity.this, ToastMsg.NETWORK_ERROR + " : " + t.getMessage(), Toast.LENGTH_LONG).show();
                refresh.setRefreshing(false);
            }
        });
    }
}
