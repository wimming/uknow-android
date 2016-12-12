package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-11-21.
 */

@DatabaseTable(tableName = "tb_QR")
public class QRBean {
    public QRBean() {}
//    @DatabaseField(generatedId = true)
//    private int id;

    @DatabaseField(columnName = "id")
    public int id;

    @DatabaseField(columnName = "description")
    public String description;

    @DatabaseField(columnName = "answerer_id")
    public int answerer_id;

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

    @DatabaseField(columnName = "audioSeconds")
    public int audioSeconds;

}
