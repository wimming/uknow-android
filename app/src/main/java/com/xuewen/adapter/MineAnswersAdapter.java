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
 * Created by Administrator on 2016/11/11.
 */
public class MineAnswersAdapter extends BaseAdapter{

    private List<UUidBean.Answer> list;
    private Context context;

    public MineAnswersAdapter(List<UUidBean.Answer> list, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.mine_answers_item, null);
            viewHolder = new ViewHolder();
            viewHolder.asker_avatarUrl = (ImageView) view.findViewById(R.id.asker_avatarUrl);
            viewHolder.asker_username = (TextView)view.findViewById(R.id.asker_username);
            viewHolder.description = (TextView) view.findViewById(R.id.description);
            viewHolder.finishedText = (TextView)view.findViewById(R.id.finishedText);
            viewHolder.answerButton = (ImageView) view.findViewById(R.id.answerButton);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.asker_username.setText(list.get(position).asker_username+"的提问：");
        viewHolder.description.setText(list.get(position).description);

        ImageLoader.getInstance().displayImage(Global.getInstance().baseAvatarUrl+list.get(position).asker_avatarUrl, viewHolder.asker_avatarUrl, Global.getInstance().circleBitmapOptions);

        if (list.get(position).finished) {
            viewHolder.answerButton.setVisibility(View.GONE);
            viewHolder.finishedText.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.finishedText.setVisibility(View.GONE);
            viewHolder.answerButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private class ViewHolder {
        public ImageView asker_avatarUrl;
        public TextView asker_username;
        public TextView description;

        public TextView finishedText;
        public ImageView answerButton;
    }
}
