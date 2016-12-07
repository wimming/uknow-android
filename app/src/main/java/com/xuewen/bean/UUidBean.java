package com.xuewen.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by ym on 16-11-30.
 */


@DatabaseTable(tableName = "tb_UUid")
public class UUidBean {

    public UUidBean() {}
//    @DatabaseField(generatedId = true)
//    private int id;

    @DatabaseField(id = true)
    public int id;

    @DatabaseField(columnName = "username")
    public String username;

    @DatabaseField(columnName = "avatarUrl")
    public String avatarUrl;

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

    @DatabaseField(columnName = "followedNum")
    public int followedNum;

    @DatabaseField(columnName = "askNum")
    public int askNum;

    @DatabaseField(columnName = "ansNum")
    public int ansNum;

    public List<Answer> answer;
    public List<Asked> asked;

    public class Answer {
        public int id;
        public String description;
        public String asker_username;
        public String asker_avatarUrl;
        public boolean finished;
    }

    public class Asked {
        public int id;
        public String description;
        public int answerer_id;
        public String answerer_username;
        public String answerer_status;
        public String answerer_description;
        public String answerer_avatarUrl;
        public boolean finished;
    }

}
