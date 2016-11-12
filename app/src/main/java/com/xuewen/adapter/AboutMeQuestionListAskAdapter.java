package com.xuewen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuewen.bean.Question;
import com.xuewen.xuewen.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/12.
 */
public class AboutMeQuestionListAskAdapter extends BaseAdapter{

    private List<Question> list;
    private Context context;

    public AboutMeQuestionListAskAdapter(List<Question> list, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.aboutme_ask_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.que_headimgurl = (ImageView) view.findViewById(R.id.que_headimgurl);
            viewHolder.que_username = (TextView)view.findViewById(R.id.que_username);
            viewHolder.que_description = (TextView) view.findViewById(R.id.que_description);
            viewHolder.ans_status = (TextView) view.findViewById(R.id.ans_status);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.que_username.setText(list.get(position).que_username);
        viewHolder.que_description.setText(list.get(position).que_description);

        return view;
    }

    private class ViewHolder {
        public ImageView que_headimgurl;
        public TextView que_username;
        public TextView que_description;
        public TextView ans_status;
    }
}