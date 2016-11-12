package com.xuewen.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuewen.bean.UserMe;
import com.xuewen.xuewen.R;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */
public class UserListAdapter extends BaseAdapter{

    private List<UserMe> list;
    private Context context;

    public UserListAdapter(List<UserMe> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Object getItem(int position) {
        if (list == null) return null;
        return list.get(position);
    }

    @Override
    public int getCount() {
        if (list == null) return 0;
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.user_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.headimgurl = (ImageView) view.findViewById(R.id.headimgurl);
            viewHolder.username = (TextView) view.findViewById(R.id.username);
            viewHolder.description= (TextView) view.findViewById(R.id.description);
            viewHolder.school = (TextView) view.findViewById(R.id.school);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(list.get(position).username);
        viewHolder.description.setText(list.get(position).description);
        viewHolder.school.setText(list.get(position).school);

        return view;
    }

    private class ViewHolder {
        public ImageView headimgurl;
        public TextView username;
        public TextView description;
        public TextView school;
    }
}
