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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_1, container, false);
        final ListView usersListView = (ListView) rootView.findViewById(R.id.usersListView);

        List<UserMe> list = new ArrayList<>();
        UserMe userMe;
        for (int i = 0; i < 10; i++) {
            userMe = new UserMe();
            userMe.username = "张三";
            userMe.school = "中山大学软学院学生";
            userMe.description = "爱好产品，曾在腾讯实习，略懂开发";
            if (i < 5) {
                userMe.followed = 1;   //假数据
            } else {
                userMe.followed = 0;
            }
            list.add(userMe);
        }

        UserListAdapter userListAdapter = new UserListAdapter(list, getActivity());
        usersListView.setAdapter(userListAdapter);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AskActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
