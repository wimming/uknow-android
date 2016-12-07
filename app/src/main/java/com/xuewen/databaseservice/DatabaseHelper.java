package com.xuewen.databaseservice;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.xuewen.bean.QRBean;
import com.xuewen.bean.UUidBean;
import com.xuewen.bean.UUidFARBean;

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
            TableUtils.createTable(connectionSource, QRBean.class);
            TableUtils.createTable(connectionSource, UUidFARBean.class);
            TableUtils.createTable(connectionSource, UUidBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource, QRBean.class, true);
            TableUtils.dropTable(connectionSource, UUidFARBean.class, true);
            TableUtils.dropTable(connectionSource, UUidBean.class, true);
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

    private Dao<QRBean, Integer> QRBeanDao;
    private Dao<UUidFARBean, Integer> UUidFARBeanDao;
    private Dao<UUidBean, Integer> UUidBeanDao;
    /**
     * 获得Dao
     *
     * @return
     * @throws SQLException
     */
    public Dao<QRBean, Integer> getQRBeanDao() throws SQLException
    {
        if (QRBeanDao == null)
        {
            QRBeanDao = getDao(QRBean.class);
        }
        return QRBeanDao;
    }
    public Dao<UUidFARBean, Integer> getUUidFARBeanDao() throws SQLException
    {
        if (UUidFARBeanDao == null)
        {
            UUidFARBeanDao = getDao(UUidFARBean.class);
        }
        return UUidFARBeanDao;
    }
    public Dao<UUidBean, Integer> getUUidBeanDao() throws SQLException
    {
        if (UUidBeanDao == null)
        {
            UUidBeanDao = getDao(UUidBean.class);
        }
        return UUidBeanDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close()
    {
        super.close();
        QRBeanDao = null;
        UUidFARBeanDao = null;
        UUidBeanDao = null;
    }

}
