package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-10-26.
 */

@DatabaseTable(tableName = "tb_user_me")
public class UserMe {
    public UserMe() {}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "username")
    public String username;

    @DatabaseField(columnName = "headimgurl")
    public String headimgurl;

    @DatabaseField(columnName = "status")
    public boolean status;

    @DatabaseField(columnName = "description")
    public String description;

    @DatabaseField(columnName = "school")
    public String school;

    @DatabaseField(columnName = "major")
    public String major;

    @DatabaseField(columnName = "grade")
    public String grade;

    @DatabaseField(columnName = "followed")
    public int followed;

    @DatabaseField(columnName = "que_num")
    public int que_num;

    @DatabaseField(columnName = "ans_num")
    public int ans_num;

}
