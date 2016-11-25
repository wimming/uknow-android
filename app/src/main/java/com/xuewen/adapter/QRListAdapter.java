package com.xuewen.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.Question;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.xuewen.QuestionListAdapter;
import com.xuewen.xuewen.R;

import java.util.List;

/**
 * Created by ym on 16-11-22.
 */

public class QRListAdapter extends BaseAdapter {

    private List<QRBean> list;
    private Context context;

    public QRListAdapter(List<QRBean> list, Context context) {
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
        QRListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.qr_list_item, null);
            viewHolder = new QRListAdapter.ViewHolder();

            viewHolder.description = (TextView)view.findViewById(R.id.description);
            viewHolder.answerer_description = (TextView)view.findViewById(R.id.answerer_description);
            viewHolder.answerer_avatarUrl = (ImageView) view.findViewById(R.id.answerer_avatarUrl);
            viewHolder.listen = (Button) view.findViewById(R.id.listen);
            viewHolder.review = (TextView)view.findViewById(R.id.review);

            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (QRListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.description.setText(list.get(position).description);
        viewHolder.answerer_description.setText(list.get(position).answerer_description);
        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar, viewHolder.answerer_avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);

//        viewHolder.listen
        viewHolder.review.setText(list.get(position).listeningNum+"人听过，"+list.get(position).praiseNum+"人觉得好");

        return view;
    }

    private class ViewHolder {
        public TextView description;
        public TextView answerer_description;
        public ImageView answerer_avatarUrl;
        public Button listen;
        public TextView review;
    }
}
