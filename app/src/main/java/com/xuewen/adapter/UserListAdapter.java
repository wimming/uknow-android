package com.xuewen.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.UUidFANDUUidRBean;
import com.xuewen.bean.UserMe;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.xuewen.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */
public class UserListAdapter extends BaseAdapter{

    private List<UUidFANDUUidRBean> list;
    private Context context;

    public UserListAdapter(List<UUidFANDUUidRBean> list, Context context) {
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
            viewHolder.username = (TextView) view.findViewById(R.id.username);
            viewHolder.avatarUrl = (ImageView) view.findViewById(R.id.avatarUrl);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            viewHolder.description= (TextView) view.findViewById(R.id.description);
            viewHolder.followed = (TextView) view.findViewById(R.id.followed);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(list.get(position).username);
        viewHolder.status.setText(list.get(position).status);
        viewHolder.description.setText(list.get(position).description);
        ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().avatarUrl+list.get(position).avatarUrl, viewHolder.avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);

//        // 渲染层已经做了渲染判断 点击只需更改数据即可 无需再次渲染
//        if (list.get(position).followed == 0) {
//            //viewHolder.followed.setVisibility(View.INVISIBLE);
//            viewHolder.followed.setBackgroundResource(R.drawable.unfollow_button);
//            viewHolder.followed.setText("+关注");
//
//            viewHolder.followed.setTextColor(context.getResources().getColor(R.color.main_color));
//
//
//        } else {
////            viewHolder.followed.setVisibility(View.VISIBLE);
//            viewHolder.followed.setBackgroundResource(R.drawable.follow_button);
//            viewHolder.followed.setText("已关注");
//            viewHolder.followed.setTextColor(Color.GRAY);
//        }

        return view;
    }

    private class ViewHolder {
        public TextView username;
        public ImageView avatarUrl;
        public TextView status;
        public TextView description;
        public TextView followed;
    }
}
