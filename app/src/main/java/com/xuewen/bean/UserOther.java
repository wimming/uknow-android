package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-10-28.
 */

@DatabaseTable(tableName = "tb_user_other")
public class UserOther {
    public UserOther() {}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "userid")
    public int userid;

    @DatabaseField(columnName = "headimgurl")
    public String headimgurl;

    @DatabaseField(columnName = "follow_num")
    public int follow_num;

    @DatabaseField(columnName = "ans_num")
    public int ans_num;

    @DatabaseField(columnName = "user_status")
    public boolean user_status;

    @DatabaseField(columnName = "user_description")
    public String user_description;


}
