package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-10-22.
 */

@DatabaseTable(tableName = "tb_question")
public class Question {
    public Question() {}
    public Question(String content) {
        this.content = content;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(columnName = "content")
    public String content;
}
