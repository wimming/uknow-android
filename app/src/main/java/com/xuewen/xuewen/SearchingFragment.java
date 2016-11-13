package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.xuewen.adapter.UserListAdapter;
import com.xuewen.bean.Question;
import com.xuewen.bean.UserMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ym on 16-10-23.
 */

public class SearchingFragment extends Fragment {

    private TextView concernPersonTitle;
    private TextView recommendedPersonTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        final ListView concernPerson = (ListView) rootView.findViewById(R.id.concernPerson);
        final ListView recommendedPerson = (ListView) rootView.findViewById(R.id.recommendedPerson);
        concernPersonTitle = (TextView) rootView.findViewById(R.id.concernPersonTitle);
        recommendedPersonTitle =  (TextView) rootView.findViewById(R.id.recommendedPersonTitle);

        List<UserMe> list = new ArrayList<>();
        UserMe userMe;
        for (int i = 0; i < 10; i++) {
            userMe = new UserMe();
            userMe.username = "张三";
            userMe.school = "中山大学软学院学生";
            userMe.description = "爱好产品，曾在腾讯实习，略懂开发";
            list.add(userMe);
        }

        List<UserMe> list2 = new ArrayList<>();
        UserMe userMe2;
        for (int i = 0; i < 10; i++) {
            userMe2 = new UserMe();
            userMe2.username = "张三";
            userMe2.school = "中山大学软学院学生";
            userMe2.description = "爱好产品，曾在腾讯实习，略懂开发";
            list2.add(userMe2);
        }

        UserListAdapter userListAdapter = new UserListAdapter(list, getActivity());
        UserListAdapter userListAdapter2 = new UserListAdapter(list2, getActivity());
        concernPerson.setAdapter(userListAdapter);
        recommendedPerson.setAdapter(userListAdapter2);

        concernPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //fragment下面用 getActivity获取它所在的activity
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
            }
        });

        //交互思路，把两个Listview的高度都设置为 自动填充剩余空间 weight = 1
        // 这样的话 一个不见 另外一个占据屏幕  两个都在平分

        //2种状态 （2开） （1开1关） * 2
        concernPersonTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (concernPerson.getVisibility() == View.VISIBLE && recommendedPerson.getVisibility() == View.VISIBLE) {
                    recommendedPerson.setVisibility(View.GONE);
                } else if (concernPerson.getVisibility() == View.VISIBLE && recommendedPerson.getVisibility() == View.GONE) {
                    recommendedPerson.setVisibility(View.VISIBLE);
                } else {
                    concernPerson.setVisibility(View.VISIBLE);
                    recommendedPerson.setVisibility(View.GONE);
                }
            }
        });

        recommendedPersonTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (concernPerson.getVisibility() == View.VISIBLE && recommendedPerson.getVisibility() == View.VISIBLE) {
                    concernPerson.setVisibility(View.GONE);
                } else if (recommendedPerson.getVisibility() == View.VISIBLE && concernPerson.getVisibility() == View.GONE) {
                    concernPerson.setVisibility(View.VISIBLE);
                } else {
                    recommendedPerson.setVisibility(View.VISIBLE);
                    concernPerson.setVisibility(View.GONE);
                }
            }
        });


        return rootView;
    }
}
