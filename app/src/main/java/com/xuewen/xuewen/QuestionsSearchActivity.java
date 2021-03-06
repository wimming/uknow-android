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

import com.xuewen.adapter.QuestionsListAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.QFResult;
import com.xuewen.utility.ToastMsg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ym on 16-10-28.
 */

public class QuestionsSearchActivity extends AppCompatActivity {

    private List<QRBean> resultList = new ArrayList<>();
    private QuestionsListAdapter resultAdapter;

    @BindView(R.id.searchView)
    SearchView searchView;
//    @BindView(R.id.gridView)
//    GridView gridView;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.resultListView)
    ListView resultListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_questions_search);

        ButterKnife.bind(this);

        //搜索框的字体颜色 大小的改变 ????
//        SearchView searchView = (SearchView) findViewById(R.id.search_view_title);
//        SearchView.SearchAutoComplete textView = ( SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);

//        EditText textView = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
//        final int editViewId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
//        SearchView.SearchAutoComplete textView = (SearchView.SearchAutoComplete) searchView.findViewById(editViewId);

//        textView.setTextColor(Color.RED);
//        textView.setHintTextColor(Color.RED);

        // 暂时不用标签
//        //多行多列网格 九宫格布局 gridview
//        String[] buttonsText = {"软件工程", "中山大学", "互联网", "安卓开发", "腾讯", "奖学金" };
//        //simpleAdapter数据源
//        List<Map<String, Object>> datalist = new ArrayList<>();
//        for (String aButtonsText : buttonsText) {
//            Map<String, Object> tempMap = new HashMap<>();
//            tempMap.put("button", aButtonsText);
//            datalist.add(tempMap);
//        }
//        String[] from = {"button"};
//        int[] to = {R.id.button};
//        SimpleAdapter adapter = new SimpleAdapter(this, datalist, R.layout.activity_questions_search_item, from, to);
//        gridView.setAdapter(adapter);

        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light);
        // 下拉刷新
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!searchView.getQuery().toString().isEmpty()) {
                    retrieveQuestionsAndRender(searchView.getQuery().toString());
                }
                else {
                    refresh.setRefreshing(false);
                }
            }
        });

        // 结果列表
        resultAdapter = new QuestionsListAdapter(resultList, QuestionsSearchActivity.this);
        resultListView.setAdapter(resultAdapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuestionsSearchActivity.this, QuestionDetailActivity.class);
                intent.putExtra("id", ((QRBean) parent.getAdapter().getItem(position)).id);
                startActivity(intent);
            }
        });

        // 搜索框
//        searchView.onActionViewExpanded();
//        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.isEmpty()) {
                    retrieveQuestionsAndRender(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    retrieveQuestionsAndRender(newText);
                }
                return false;
            }
        });

    }

    private void retrieveQuestionsAndRender(String query) {
        refresh.setRefreshing(true);
        ApiService apiService = ApiService.retrofit.create(ApiService.class);
        Call<QFResult> call = apiService.requestQF(query);
        call.enqueue(new Callback<QFResult>() {
            @Override
            public void onResponse(Call<QFResult> call, Response<QFResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(QuestionsSearchActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(QuestionsSearchActivity.this, response.body().errmsg, Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    return;
                }
                resultList.clear();
                resultList.addAll(response.body().data);
                resultAdapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
                if (response.body().data.size() == 0) {
                    Toast.makeText(QuestionsSearchActivity.this, ToastMsg.EMPTY_RESULT, Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onFailure(Call<QFResult> call, Throwable t) {
                Toast.makeText(QuestionsSearchActivity.this, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                refresh.setRefreshing(false);
            }
        });
    }

}
