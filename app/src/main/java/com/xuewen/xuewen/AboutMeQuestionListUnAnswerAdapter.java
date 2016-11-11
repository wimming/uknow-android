package com.xuewen.xuewen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuewen.bean.Question;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */
public class AboutMeQuestionListUnAnswerAdapter extends BaseAdapter{
    private List<Question> list;
    private Context context;

    public AboutMeQuestionListUnAnswerAdapter(List<Question> list, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.aboutme_answer_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.aboutMe_unanswerItem_iv_avator = (ImageView) view.findViewById(R.id.aboutMe_unanswerItem_iv_avator);
            viewHolder.aboutMe_unanswerItem_tv_ask = (TextView)view.findViewById(R.id.aboutMe_unanswerItem_tv_ask);
            viewHolder.aboutMe_unanswerItem_tv_title = (TextView) view.findViewById(R.id.aboutMe_unanswerItem_tv_title);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.aboutMe_unanswerItem_tv_title.setText(list.get(position).que_description);
        viewHolder.aboutMe_unanswerItem_tv_ask.setText(list.get(position).ans_description);
        return view;
    }

    private class ViewHolder {
        public ImageView aboutMe_unanswerItem_iv_avator;
        public TextView aboutMe_unanswerItem_tv_ask;
        public TextView aboutMe_unanswerItem_tv_title;
    }
}
