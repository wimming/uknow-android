package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huangyuming on 16-12-7.
 */

@DatabaseTable(tableName = "tb_UUidFAR")
public class UUidFARBean {
    public UUidFARBean() {}
//    @DatabaseField(generatedId = true)
//    private int id;

    @DatabaseField(columnName = "id")
    public int id;

    @DatabaseField(columnName = "username")
    public String username;

    @DatabaseField(columnName = "avatarUrl")
    public String avatarUrl;

    @DatabaseField(columnName = "status")
    public String status;

    @DatabaseField(columnName = "description")
    public String description;

    @DatabaseField(columnName = "followed")
    public int followed;

}
