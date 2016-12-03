package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-11-21.
 */

@DatabaseTable(tableName = "tb_QQid")
public class QQidBean {
    public QQidBean() {}
//    @DatabaseField(generatedId = true)
//    private int id;

    @DatabaseField(columnName = "id")
    public int id;

    @DatabaseField(columnName = "description")
    public String description;

    @DatabaseField(columnName = "askDate")
    public String askDate;

    @DatabaseField(columnName = "audioUrl")
    public String audioUrl;

    @DatabaseField(columnName = "asker_username")
    public String asker_username;

    @DatabaseField(columnName = "asker_avatarUrl")
    public String asker_avatarUrl;

    @DatabaseField(columnName = "answerer_username")
    public String answerer_username;

    @DatabaseField(columnName = "answerer_status")
    public String answerer_status;

    @DatabaseField(columnName = "answerer_description")
    public String answerer_description;

    @DatabaseField(columnName = "answerer_avatarUrl")
    public String answerer_avatarUrl;

    @DatabaseField(columnName = "listeningNum")
    public int listeningNum;

    @DatabaseField(columnName = "praiseNum")
    public int praiseNum;

    @DatabaseField(columnName = "commented")
    public int commented;

    @DatabaseField(columnName = "liked")
    public int liked;
}
