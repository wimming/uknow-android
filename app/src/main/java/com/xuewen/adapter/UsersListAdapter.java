package com.xuewen.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.xuewen.bean.UUidFARBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.UUidFResult;
import com.xuewen.utility.BitmapDecoder;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.ToastMsg;
import com.xuewen.xuewen.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2016/11/12.
 */
public class UsersListAdapter extends BaseAdapter{

    private List<UUidFARBean> list;
    private Context context;

    private Bitmap defaultAvatar;

    public UsersListAdapter(List<UUidFARBean> list, Context context) {
        this.list = list;
        this.context = context;

        defaultAvatar = BitmapDecoder.decodeSampledBitmapFromResource(context.getResources(), R.drawable.avatar, 100, 100);
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
            view = LayoutInflater.from(context).inflate(R.layout.users_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.username = (TextView) view.findViewById(R.id.username);
            viewHolder.avatarUrl = (ImageView) view.findViewById(R.id.avatarUrl);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            viewHolder.description= (TextView) view.findViewById(R.id.description);
            viewHolder.followed = (TextView) view.findViewById(R.id.followed);

            viewHolder.followed.setOnClickListener(onFollowedClickListener);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.username.setText(list.get(position).username);
        viewHolder.status.setText(list.get(position).status);
        viewHolder.description.setText(list.get(position).description);

        if (viewHolder.avatarUrl.getTag() == null
                ||
                !viewHolder.avatarUrl.getTag().equals(GlobalUtil.getInstance().baseAvatarUrl+list.get(position).avatarUrl)) {

            viewHolder.avatarUrl.setImageBitmap(defaultAvatar);
            ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl + list.get(position).avatarUrl,
                    viewHolder.avatarUrl,
                    GlobalUtil.getInstance().circleBitmapOptions,
                    new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            Log.e("UIL", "onLoadingStarted");
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            Log.e("UIL", "onLoadingFailed");
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            Log.e("UIL", "onLoadingComplete");
                            view.setTag(imageUri);
                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            Log.e("UIL", "onLoadingCancelled");
                        }
                    });

        }


        // 渲染层已经做了渲染判断 点击只需更改数据即可 无需再次渲染
        if (list.get(position).followed == 0) {
            //viewHolder.followed.setVisibility(View.INVISIBLE);
            viewHolder.followed.setBackgroundResource(R.drawable.unfollow_button);
            viewHolder.followed.setText("+关注");
            viewHolder.followed.setTextColor(context.getResources().getColor(R.color.main_color));


        } else {
//            viewHolder.followed.setVisibility(View.VISIBLE);
            viewHolder.followed.setBackgroundResource(R.drawable.follow_button_gray);
            viewHolder.followed.setText("已关注");
            viewHolder.followed.setTextColor(Color.GRAY);
        }

        viewHolder.followed.setTag(position);

        return view;
    }

    private class ViewHolder {
        public TextView username;
        public ImageView avatarUrl;
        public TextView status;
        public TextView description;
        public TextView followed;
    }

    private View.OnClickListener onFollowedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            final int position = (int)view.getTag();
            if (list.get(position).followed == 0) {

                ApiService apiService = ApiService.retrofit.create(ApiService.class);
                Call<UUidFResult> call = apiService.requestUUidF(CurrentUser.userId, list.get(position).id);
                call.enqueue(new Callback<UUidFResult>() {
                    @Override
                    public void onResponse(Call<UUidFResult> call, Response<UUidFResult> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(context, ToastMsg.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (response.body().status != 200) {
                            Toast.makeText(context, response.body().errmsg, Toast.LENGTH_SHORT).show();
                            return;
                        }

                        list.get(position).followed = 1;
                        TextView textView = (TextView)view;
                        textView.setBackgroundResource(R.drawable.follow_button_gray);
                        textView.setText("已关注");
                        textView.setTextColor(Color.GRAY);
                    }
                    @Override
                    public void onFailure(Call<UUidFResult> call, Throwable t) {
                        Toast.makeText(context, ToastMsg.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
    };
}
