package com.xuewen.xuewen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.xuewen.adapter.UserListAdapter;
import com.xuewen.bean.Question;
import com.xuewen.bean.UserMe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ym on 16-10-23.
 */

public class SearchingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        ListView concernPerson = (ListView) rootView.findViewById(R.id.concernPerson);
        ListView recommendedPerson = (ListView) rootView.findViewById(R.id.recommendedPerson);

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

        return rootView;
    }
}
