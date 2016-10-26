package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-10-26.
 */

@DatabaseTable(tableName = "tb_user")
public class User {
    public User() {}

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "username")
    public String username;

    @DatabaseField(columnName = "password")
    public String password;

    @DatabaseField(columnName = "headimgurl")
    public String headimgurl;

    @DatabaseField(columnName = "status")
    public String status;

    @DatabaseField(columnName = "description")
    public String description;

    @DatabaseField(columnName = "school")
    public String school;

    @DatabaseField(columnName = "major")
    public String major;

    @DatabaseField(columnName = "grade")
    public String grade;

    @DatabaseField(columnName = "openid")
    public String openid;

}
