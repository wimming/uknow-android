package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by ym on 16-11-26.
 */

@DatabaseTable(tableName = "tb_UUidI")
public class UUidIBean {
    public UUidIBean() {}
//    @DatabaseField(generatedId = true)
//    private int id;

    @DatabaseField(columnName = "avatarUrl")
    public String avatarUrl;

    @DatabaseField(columnName = "username")
    public String username;

    @DatabaseField(columnName = "status")
    public String status;

    @DatabaseField(columnName = "description")
    public String description;

    @DatabaseField(columnName = "followed")
    public int followed;

    @DatabaseField(columnName = "followedNum")
    public int followedNum;

    @DatabaseField(columnName = "ansNum")
    public int ansNum;

    public List<QRBean> answers;
}
