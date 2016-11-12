package com.xuewen.databaseservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xuewen.bean.Question;
import com.xuewen.bean.UserMe;
import com.xuewen.bean.UserOther;

import java.sql.SQLException;

/**
 * Created by ym on 16-10-22.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "database1.db";
    private static final int VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Question.class);
            TableUtils.createTable(connectionSource, UserMe.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, Question.class, true);
            TableUtils.dropTable(connectionSource, UserMe.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;
    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context)
    {
        if (instance == null)
        {
            synchronized (DatabaseHelper.class)
            {
                if (instance == null)

                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    private Dao<Question, Integer> questionDao;
    private Dao<UserMe, Integer> userMeDao;
    private Dao<UserOther, Integer> userOtherDao;
    /**
     * 获得Dao
     *
     * @return
     * @throws SQLException
     */
    public Dao<Question, Integer> getQuestionDao() throws SQLException
    {
        if (questionDao == null)
        {
            questionDao = getDao(Question.class);
        }
        return questionDao;
    }
    public Dao<UserMe, Integer> getUserMeDao() throws SQLException
    {
        if (userMeDao == null)
        {
            userMeDao = getDao(UserMe.class);
        }
        return userMeDao;
    }
    public Dao<UserOther, Integer> getUserOtherDao() throws SQLException
    {
        if (userOtherDao == null)
        {
            userOtherDao = getDao(UserOther.class);
        }
        return userOtherDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        questionDao = null;
    }

}
