package com.xuewen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.UUidBean;
import com.xuewen.utility.Global;
import com.xuewen.xuewen.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */
public class MineAsksAdapter extends BaseAdapter{

    private List<UUidBean.Asked> list;
    private Context context;

    public MineAsksAdapter(List<UUidBean.Asked> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.mine_asks_item, null);
            viewHolder = new ViewHolder();
            viewHolder.answerer_avatarUrl = (ImageView) view.findViewById(R.id.answerer_avatarUrl);
            viewHolder.answerer_status = (TextView)view.findViewById(R.id.answerer_status);
            viewHolder.description = (TextView) view.findViewById(R.id.description);
            viewHolder.isAnswered = (TextView) view.findViewById(R.id.isAnswered);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.answerer_status.setText(list.get(position).answerer_username+" | "+list.get(position).answerer_status);
        viewHolder.description.setText(list.get(position).description);

        viewHolder.isAnswered.setText(list.get(position).finished ? "已回答" : "未回答");

        ImageLoader.getInstance().displayImage(Global.getInstance().baseAvatarUrl+list.get(position).answerer_avatarUrl, viewHolder.answerer_avatarUrl, Global.getInstance().circleBitmapOptions);

        return view;
    }

    private class ViewHolder {
        public ImageView answerer_avatarUrl;
        public TextView answerer_status;
        public TextView description;
        public TextView isAnswered;
    }
}
