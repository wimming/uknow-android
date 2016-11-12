package com.xuewen.xuewen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.xuewen.bean.Question;
import com.xuewen.utility.GlobalUtil;

import java.util.List;

/**
 * Created by ym on 16-10-22.
 */

public class QuestionListAdapter extends BaseAdapter {

    private List<Question> list;
    private Context context;

    public QuestionListAdapter(List<Question> list, Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.question_list_view_item, null);
            viewHolder = new ViewHolder();

            viewHolder.que_description = (TextView)view.findViewById(R.id.que_description);
            viewHolder.ans_description = (TextView)view.findViewById(R.id.ans_description);
            viewHolder.ans_headimgurl = (ImageView) view.findViewById(R.id.ans_headimgurl);
            viewHolder.listen = (ImageButton) view.findViewById(R.id.listen);
            viewHolder.review = (TextView)view.findViewById(R.id.review);

            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.que_description.setText(list.get(position).que_description);
        viewHolder.ans_description.setText(list.get(position).ans_description);
        ImageLoader.getInstance().displayImage(list.get(position).ans_headimgurl, viewHolder.ans_headimgurl, GlobalUtil.getInstance().circleBitmapOptions);
//        viewHolder.listen
        viewHolder.review.setText(list.get(position).heard+"人听过，"+list.get(position).liked+"人觉得好");

        return view;
    }

    private class ViewHolder {
        public TextView que_description;
        public TextView ans_description;
        public ImageView ans_headimgurl;
        public ImageButton listen;
        public TextView review;
    }
}
