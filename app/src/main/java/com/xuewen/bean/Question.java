package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-10-22.
 */

@DatabaseTable(tableName = "tb_question")
public class Question {
    public Question() {}
    public Question(String que_description) {
        this.que_description = que_description;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "que__id")
    public String que__id;

    @DatabaseField(columnName = "que_description")
    public String que_description;

    @DatabaseField(columnName = "que_username")
    public String que_username;

    @DatabaseField(columnName = "que_headimgurl")
    public String que_headimgurl;

    @DatabaseField(columnName = "ans_status")
    public boolean ans_status;

    @DatabaseField(columnName = "ans_username")
    public String ans_username;

//    文档写错了？
//    @DatabaseField(columnName = "ans_status")
//    public String ans_status;

    @DatabaseField(columnName = "ans_description")
    public String ans_description;

    @DatabaseField(columnName = "ans_headimgurl")
    public String ans_headimgurl;

    @DatabaseField(columnName = "ans_url")
    public String ans_url;

//    @DatabaseField(columnName = "askDate")
//    public String askDate;

    @DatabaseField(columnName = "heard")
    public int heard;

    @DatabaseField(columnName = "liked")
    public int liked;

}
