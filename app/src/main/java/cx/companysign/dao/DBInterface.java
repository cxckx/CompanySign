package cx.companysign.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.List;

import cx.companysign.CompanySignApp;
import cx.companysign.bean.UserEntity;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 * <p/>
 * 有两个静态标识可开启QueryBuilder的SQL和参数的日志输出：
 * QueryBuilder.LOG_SQL = true;
 * QueryBuilder.LOG_VALUES = true;
 */
public class DBInterface {
    private static DBInterface dbInterface = null;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context = null;
    //private int  loginUserId =0;
    private String loginUserId = null;

    public static DBInterface instance() {
        if (dbInterface == null) {
            synchronized (DBInterface.class) {
                if (dbInterface == null) {
                    dbInterface = new DBInterface();
                }
            }
        }
        return dbInterface;
    }

    private DBInterface() {
         initDbHelp(CompanySignApp.applicationContext, "company.db");
    }

    /**
     * 上下文环境的更新
     * 1. 环境变量的clear
     * check
     */
    public void close() {
        if (openHelper != null){
            openHelper.close();
            openHelper = null;
            context = null;
        }
    }

    public void initDbHelp(Context ctx, String dbName) {
        if (ctx == null || TextUtils.isEmpty(dbName)) {
            throw new RuntimeException("#DBInterface# init DB exception!");
        }
        close();
        // 临时处理，为了解决离线登陆db实例初始化的过程
        context = ctx;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, dbName, null);
        this.openHelper = helper;
    }

    /**
     * Query for readable DB
     */
    public DaoSession openReadableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /**
     * Query for writable DB
     */
    public DaoSession openWritableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private void isInitOk() {
        if (openHelper == null) {
            //logger.e("DBInterface#isInit not success or start,cause by openHelper is null");
            // 抛出异常 todo
            throw new RuntimeException("DBInterface#isInit not success or start,cause by openHelper is null");
        }
    }



    public List<UserEntity> loadAllUser(){
        UserEntityDao customerDAO = openReadableDb().getUserEntityDao();
        List<UserEntity> customers = customerDAO.loadAll();
        return customers;
    }

    public List<UserEntity> loadUserbyPartCompany(String partCompany){
        UserEntityDao customerDAO = openReadableDb().getUserEntityDao();
        List<UserEntity> customers = customerDAO.queryBuilder().where(UserEntityDao.Properties.UserPartCompany.eq(partCompany)).list();
        return customers;
    }
    public List<UserEntity> loadUserbyBranch(String branch){
        UserEntityDao customerDAO = openReadableDb().getUserEntityDao();
        List<UserEntity> customers = customerDAO.queryBuilder().where(UserEntityDao.Properties.UserBranch.eq(branch)).list();
        return customers;
    }

    public UserEntity getUser(String phone){
        UserEntityDao customerDAO = openReadableDb().getUserEntityDao();
        UserEntity user = customerDAO.queryBuilder().where(UserEntityDao.Properties.UserPhone.eq(phone)).unique();
        return user;
    }



    public void insertOrUpdateUser(List<UserEntity> entities){
        UserEntityDao dao=openWritableDb().getUserEntityDao();
        dao.insertOrReplaceInTx(entities);
    }

    public int getUserCount(){
        return openReadableDb().getUserEntityDao().loadAll().size();
    }
    public void clear(){
        openWritableDb().getUserEntityDao().deleteAll();
    }

}
