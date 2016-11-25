package com.xuewen.xuewen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.Question;
import com.xuewen.utility.GlobalUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */
public class AboutMeQuestionListAnswerAdapter extends BaseAdapter{

    private List<Question> list;
    private Context context;

    public AboutMeQuestionListAnswerAdapter(List<Question> list, Context context) {
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
            viewHolder.que_headimgurl = (ImageView) view.findViewById(R.id.que_headimgurl);
            viewHolder.que_username = (TextView)view.findViewById(R.id.que_username);
            viewHolder.que_description = (TextView) view.findViewById(R.id.que_description);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.que_username.setText(list.get(position).que_description);
        viewHolder.que_description.setText(list.get(position).ans_description);

        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, viewHolder.que_headimgurl, GlobalUtil.getInstance().circleBitmapOptions);
        return view;
    }

    private class ViewHolder {
        public ImageView que_headimgurl;
        public TextView que_username;
        public TextView que_description;
    }
}
