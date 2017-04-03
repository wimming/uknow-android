package com.xuewen.xuewen;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.xuewen.adapter.QuestionsListAdapter;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.UUidIBean;
import com.xuewen.networkservice.ApiService;
import com.xuewen.networkservice.ApiServiceRequestResultHandler;
import com.xuewen.networkservice.QResult;
import com.xuewen.networkservice.UUidFResult;
import com.xuewen.networkservice.UUidIResult;
import com.xuewen.utility.CurrentUser;
import com.xuewen.utility.GlobalUtil;
import com.xuewen.utility.MyTextWatcher;
import com.xuewen.utility.ToastMsg;
import com.xuewen.utility.Validate;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AskActivity extends AppCompatActivity {

    private int id;

    @BindView(R.id.editText)
    EditText editText;
    @BindView((R.id.textView))
    TextView textView;
    @BindView(R.id.sendAskedRequest)
    Button sendAskedRequest;

    @BindView(R.id.avatarUrl)
    ImageView avatarUrl;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.followed)
    TextView followed;
    @BindView(R.id.followedAndAnswerSituation)
    TextView followedAndAnswerSituation;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.question_list_view)
    ListView questionListView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    @BindView(R.id.main_content)
    CoordinatorLayout main_content;



    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        //注册
        ButterKnife.bind(this);
        apiService = ApiService.retrofit.create(ApiService.class);

        ImageLoader.getInstance().displayImage("drawable://" +  R.drawable.avatar,avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            questionListView.setNestedScrollingEnabled(true);
        }

        //两个滚动 有冲突 不做刷新
//        refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light);
//        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //Toast.makeText(getActivity(), "刷新一下", Toast.LENGTH_SHORT).show();
//                refresh.setRefreshing(false);
//                requestData(id);
//
//            }
//        });


        refresh.setEnabled(false); //不做刷新
        refresh.setRefreshing(true);
        main_content.setVisibility(View.GONE);



        editText.addTextChangedListener(new MyTextWatcher(this, 60, textView));

        sendAskedRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendSendAskedRequestService(CurrentUser.userId, editText.getText().toString(), id);
                Toast.makeText(AskActivity.this, "提问成功", Toast.LENGTH_SHORT).show();
            }
        });

        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        id = getIntent().getIntExtra("id", -1);
        if (id == -1) {
            Toast.makeText(AskActivity.this, ToastMsg.APPLICATION_ERROR, Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        requestData(id);

        followed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //简单判断当前是否为关注
                if (followed.getText().equals("+关注")) {
                    renderFollowedButton();
                    sendFollowsService(CurrentUser.userId, id, new ApiServiceRequestResultHandler() {
                        @Override
                        public void onSuccess(Object dataBean) {
                            renderFollowedButton();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            renderUnfollowedButton();
                        }
                    });
                } else {
                    renderUnfollowedButton();
                    sendUnFollowsService(CurrentUser.userId, id, new ApiServiceRequestResultHandler() {
                        @Override
                        public void onSuccess(Object dataBean) {
                            renderUnfollowedButton();
                        }

                        @Override
                        public void onError(String errorMessage) {
                           renderFollowedButton();
                        }
                    });
                }
            }
        });

    }


    private void sendSendAskedRequestService(int asker_id, String  description, int answerer_id) {

        if (Validate.isExistEmpty(editText)) {
            ToastMsg.showTips(AskActivity.this, ToastMsg.ARG_INVALID_EMPTY);
            return;
        }

        Call<QResult> call = apiService.requestQ(asker_id, description, answerer_id); //7
        call.enqueue(new Callback<QResult>() {
            @Override
            public void onResponse(Call<QResult> call, Response<QResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AskActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(AskActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    return;
                }
                //Toast.makeText(AskActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AskActivity.this, AskedSuccessActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onFailure(Call<QResult> call, Throwable t) {
                Toast.makeText(AskActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void requestData(int uid) {
        Call<UUidIResult> call = apiService.requestUUidI(uid, CurrentUser.userId);
        call.enqueue(new Callback<UUidIResult>() {
            @Override
            public void onResponse(Call<UUidIResult> call, Response<UUidIResult> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(AskActivity.this, ToastMsg.SERVER_ERROR, Toast.LENGTH_LONG).show();
                    refresh.setRefreshing(false);
                    main_content.setVisibility(View.VISIBLE);
                    return;
                }
                if (response.body().status != 200) {
                    Toast.makeText(AskActivity.this, response.body().errmsg, Toast.LENGTH_LONG).show();
                    refresh.setRefreshing(false);
                    main_content.setVisibility(View.VISIBLE);
                    return;
                }

                renderView(response.body().data);
                refresh.setRefreshing(false);
                main_content.setVisibility(View.VISIBLE);

            }
            @Override
            public void onFailure(Call<UUidIResult> call, Throwable t) {
                Toast.makeText(AskActivity.this, ToastMsg.NETWORK_ERROR+" : "+t.getMessage(), Toast.LENGTH_LONG).show();
                refresh.setRefreshing(false);
                main_content.setVisibility(View.VISIBLE);
            }
        });
    }

    private void renderView(UUidIBean data) {
        title.setText("向" + data.username + "提问");
        username.setText(data.username);
        status.setText(data.status);
        description.setText(data.description);
        if (data.followed == 0) {
            followed.setText("+关注");
            followed.setBackgroundResource(R.drawable.follow_button);
            followed.setTextColor(Color.GRAY);
            //viewHolder.followed.setTextColor(context.getResources().getColor(R.color.main_color));
        }

        ImageLoader.getInstance().displayImage(GlobalUtil.getInstance().baseAvatarUrl+ data.avatarUrl, avatarUrl, GlobalUtil.getInstance().circleBitmapOptions);

        followedAndAnswerSituation.setText(data.followedNum + "人关注, 回答了" + data.ansNum + "个问题");

        final QuestionsListAdapter adapter = new QuestionsListAdapter(data.answers, AskActivity.this);
        questionListView.setAdapter(adapter);
        questionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(AskActivity.this, QuestionDetailActivity.class);
                intent.putExtra("id", ((QRBean) parent.getAdapter().getItem(position)).id);
                startActivity(intent);
            }
        });
    }


    private void sendFollowsService(int uid, int followed_uid, final ApiServiceRequestResultHandler apiServiceRequestResultHandler) {

        Call<UUidFResult> call = apiService.requestUUidF(uid, followed_uid);
        call.enqueue(new Callback<UUidFResult>() {
            @Override
            public void onResponse(Call<UUidFResult> call, Response<UUidFResult> response) {
                if (!response.isSuccessful()) {
                    apiServiceRequestResultHandler.onError(ToastMsg.SERVER_ERROR);
                    return;
                }
                if (response.body().status != 200) {
                    apiServiceRequestResultHandler.onSuccess(response.body().data);
                    return;
                }
                apiServiceRequestResultHandler.onSuccess(response.body().data);
            }
            @Override
            public void onFailure(Call<UUidFResult> call, Throwable t) {
                apiServiceRequestResultHandler.onError(ToastMsg.NETWORK_ERROR+" : "+t.getMessage());
            }
        });
    }

    private void sendUnFollowsService(int uid, int followed_uid, final ApiServiceRequestResultHandler apiServiceRequestResultHandler) {

        Call<UUidFResult> call = apiService.deleteUUidF(uid, followed_uid);
        call.enqueue(new Callback<UUidFResult>() {
            @Override
            public void onResponse(Call<UUidFResult> call, Response<UUidFResult> response) {
                if (!response.isSuccessful()) {
                    apiServiceRequestResultHandler.onError(ToastMsg.SERVER_ERROR);
                    return;
                }
                if (response.body().status != 200) {
                    apiServiceRequestResultHandler.onError(response.body().errmsg);
                    return;
                }
                apiServiceRequestResultHandler.onSuccess(response.body());
            }
            @Override
            public void onFailure(Call<UUidFResult> call, Throwable t) {
                apiServiceRequestResultHandler.onError(ToastMsg.NETWORK_ERROR+" : "+t.getMessage());
            }
        });
    }






    private void renderFollowedButton() {
        followed.setText("已关注");
        followed.setBackgroundResource(R.drawable.radius_button_shape);
        followed.setTextColor(Color.WHITE);
    }

    private void renderUnfollowedButton() {
        followed.setText("+关注");
        followed.setBackgroundResource(R.drawable.follow_button);
        followed.setTextColor(Color.GRAY);
    }

}
