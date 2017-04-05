package com.xuewen.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xuewen.bean.QRBean;
import com.xuewen.utility.BitmapDecoder;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.xuewen.QuestionAskActivity;
import com.xuewen.xuewen.QuestionDetailActivity;
import com.xuewen.xuewen.R;

import java.util.List;

/**
 * Created by ym on 16-11-22.
 */

public class QuestionsListAdapter extends BaseAdapter {

    private List<QRBean> list;
    private Context context;

    private Bitmap defaultAvatar;

    LruCache<String, Bitmap> mCaches;

    public QuestionsListAdapter(List<QRBean> list, Context context) {
        this.list = list;
        this.context = context;

        defaultAvatar = BitmapDecoder.decodeSampledBitmapFromResource(context.getResources(), R.drawable.avatar, 100, 100);

        //下面是建立缓存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();  //运行时最大内存
        int cacheSize = maxMemory/4;  //缓存的大小
        mCaches = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    //将bitmap添加到缓存
    public void addBitmapToCache(String url, Bitmap bitmap){
        if (getBitmapFormCache(url) == null){
            mCaches.put(url, bitmap);
        }
    }
    //从缓存中获取数据
    public Bitmap getBitmapFormCache(String url){
        return mCaches.get(url);
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
        QuestionsListAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.questions_list_item, null);
            viewHolder = new QuestionsListAdapter.ViewHolder();

            viewHolder.description = (TextView)view.findViewById(R.id.description);
            viewHolder.answerer_description = (TextView)view.findViewById(R.id.answerer_description);
            viewHolder.answerer_avatarUrl = (ImageView) view.findViewById(R.id.answerer_avatarUrl);
            viewHolder.listen = (Button) view.findViewById(R.id.listen);
            viewHolder.review = (TextView)view.findViewById(R.id.review);
            viewHolder.avatarClicker = (View) view.findViewById(R.id.avatarClicker);

            viewHolder.listen.setOnClickListener(onListenClickListener);
            viewHolder.avatarClicker.setOnClickListener(onAnswererAvatarUrlClickListener);

            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (QuestionsListAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.description.setText(list.get(position).description);
        viewHolder.answerer_description.setText(list.get(position).answerer_username+" | "+list.get(position).answerer_status+"。"+list.get(position).answerer_description);
        viewHolder.listen.setText(list.get(position).audioSeconds+"''");
        viewHolder.review.setText(list.get(position).listeningNum+"人听过，"+list.get(position).praiseNum+"人觉得好");

        viewHolder.listen.setTag(position);
        viewHolder.avatarClicker.setTag(position);

        if (viewHolder.answerer_avatarUrl.getTag() == null
                ||
                !viewHolder.answerer_avatarUrl.getTag().equals(GlobalUtil.getInstance().baseAvatarUrl+list.get(position).answerer_avatarUrl)) {

            viewHolder.answerer_avatarUrl.setImageBitmap(defaultAvatar);
            ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl + list.get(position).answerer_avatarUrl,
                    viewHolder.answerer_avatarUrl,
                    GlobalUtil.getInstance().circleBitmapOptions,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            view.setTag(imageUri);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });

        }

        return view;
    }

    private class ViewHolder {
        public TextView description;
        public TextView answerer_description;
        public ImageView answerer_avatarUrl;
        public Button listen;
        public TextView review;
        public View avatarClicker;
    }

    private View.OnClickListener onListenClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, QuestionDetailActivity.class);
            intent.putExtra("id", ((QRBean)getItem((int)view.getTag())).id);
            context.startActivity(intent);
        }
    };
    private View.OnClickListener onAnswererAvatarUrlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, QuestionAskActivity.class);
            Toast.makeText(context, ((QRBean)getItem((int)view.getTag())).answerer_id+"", Toast.LENGTH_SHORT).show();
            intent.putExtra("id", ((QRBean)getItem((int)view.getTag())).answerer_id);
            context.startActivity(intent);
        }
    };
}
