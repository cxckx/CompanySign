package cx.companysign.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cx.companysign.bean.UserEntity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "USER_ENTITY".
 */
public class UserEntityDao extends AbstractDao<UserEntity, Long> {

    public static final String TABLENAME = "USER_ENTITY";

    /**
     * Properties of entity UserEntity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, Integer.class, "userId", false, "USER_ID");
        public final static Property UserName = new Property(2, String.class, "userName", false, "USER_NAME");
        public final static Property UserPhone = new Property(3, String.class, "userPhone", false, "USER_PHONE");
        public final static Property UserSex = new Property(4, Integer.class, "userSex", false, "USER_SEX");
        public final static Property UserBirthday = new Property(5, Long.class, "userBirthday", false, "USER_BIRTHDAY");
        public final static Property UserCompanyId = new Property(6, Integer.class, "userCompanyId", false, "USER_COMPANY_ID");
        public final static Property UserIconHeader = new Property(7, String.class, "userIconHeader", false, "USER_ICON_HEADER");
        public final static Property UserBranch = new Property(8, String.class, "userBranch", false, "USER_BRANCH");
        public final static Property UserPartCompany = new Property(9, String.class, "userPartCompany", false, "USER_PART_COMPANY");
    }


    public UserEntityDao(DaoConfig config) {
        super(config);
    }

    public UserEntityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" INTEGER," + // 1: userId
                "\"USER_NAME\" TEXT," + // 2: userName
                "\"USER_PHONE\" TEXT UNIQUE ," + // 3: userPhone
                "\"USER_SEX\" INTEGER," + // 4: userSex
                "\"USER_BIRTHDAY\" INTEGER," + // 5: userBirthday
                "\"USER_COMPANY_ID\" INTEGER," + // 6: userCompanyId
                "\"USER_ICON_HEADER\" TEXT," + // 7: userIconHeader
                "\"USER_BRANCH\" TEXT," + // 8: userBranch
                "\"USER_PART_COMPANY\" TEXT);"); // 9: userPartCompany
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, UserEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String userPhone = entity.getUserPhone();
        if (userPhone != null) {
            stmt.bindString(4, userPhone);
        }
 
        Integer userSex = entity.getUserSex();
        if (userSex != null) {
            stmt.bindLong(5, userSex);
        }
 
        Long userBirthday = entity.getUserBirthday();
        if (userBirthday != null) {
            stmt.bindLong(6, userBirthday);
        }
 
        Integer userCompanyId = entity.getUserCompanyId();
        if (userCompanyId != null) {
            stmt.bindLong(7, userCompanyId);
        }
 
        String userIconHeader = entity.getUserIconHeader();
        if (userIconHeader != null) {
            stmt.bindString(8, userIconHeader);
        }
 
        String userBranch = entity.getUserBranch();
        if (userBranch != null) {
            stmt.bindString(9, userBranch);
        }
 
        String userPartCompany = entity.getUserPartCompany();
        if (userPartCompany != null) {
            stmt.bindString(10, userPartCompany);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, UserEntity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer userId = entity.getUserId();
        if (userId != null) {
            stmt.bindLong(2, userId);
        }
 
        String userName = entity.getUserName();
        if (userName != null) {
            stmt.bindString(3, userName);
        }
 
        String userPhone = entity.getUserPhone();
        if (userPhone != null) {
            stmt.bindString(4, userPhone);
        }
 
        Integer userSex = entity.getUserSex();
        if (userSex != null) {
            stmt.bindLong(5, userSex);
        }
 
        Long userBirthday = entity.getUserBirthday();
        if (userBirthday != null) {
            stmt.bindLong(6, userBirthday);
        }
 
        Integer userCompanyId = entity.getUserCompanyId();
        if (userCompanyId != null) {
            stmt.bindLong(7, userCompanyId);
        }
 
        String userIconHeader = entity.getUserIconHeader();
        if (userIconHeader != null) {
            stmt.bindString(8, userIconHeader);
        }
 
        String userBranch = entity.getUserBranch();
        if (userBranch != null) {
            stmt.bindString(9, userBranch);
        }
 
        String userPartCompany = entity.getUserPartCompany();
        if (userPartCompany != null) {
            stmt.bindString(10, userPartCompany);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public UserEntity readEntity(Cursor cursor, int offset) {
        UserEntity entity = new UserEntity( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // userId
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // userName
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userPhone
                cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // userSex
                cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // userBirthday
                cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6), // userCompanyId
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // userIconHeader
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // userBranch
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // userPartCompany
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, UserEntity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setUserSex(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setUserBirthday(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setUserCompanyId(cursor.isNull(offset + 6) ? null : cursor.getInt(offset + 6));
        entity.setUserIconHeader(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setUserBranch(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setUserPartCompany(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(UserEntity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(UserEntity entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(UserEntity entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
