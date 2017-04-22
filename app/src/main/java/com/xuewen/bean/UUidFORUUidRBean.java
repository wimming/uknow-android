package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by ym on 16-11-26.
 */

@DatabaseTable(tableName = "tb_UUidFANDUUidR")
public class UUidFORUUidRBean {
    public UUidFORUUidRBean() {}
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

}
