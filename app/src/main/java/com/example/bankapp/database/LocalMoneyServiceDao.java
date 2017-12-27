package com.example.bankapp.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.bankapp.modle.LocalMoneyService;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LOCAL_MONEY_SERVICE".
*/
public class LocalMoneyServiceDao extends AbstractDao<LocalMoneyService, Long> {

    public static final String TABLENAME = "LOCAL_MONEY_SERVICE";

    /**
     * Properties of entity LocalMoneyService.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property IntroduceQuestion = new Property(1, String.class, "introduceQuestion", false, "INTRODUCE_QUESTION");
        public final static Property IntroduceAnswer = new Property(2, String.class, "introduceAnswer", false, "INTRODUCE_ANSWER");
    }


    public LocalMoneyServiceDao(DaoConfig config) {
        super(config);
    }
    
    public LocalMoneyServiceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LOCAL_MONEY_SERVICE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"INTRODUCE_QUESTION\" TEXT," + // 1: introduceQuestion
                "\"INTRODUCE_ANSWER\" TEXT);"); // 2: introduceAnswer
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LOCAL_MONEY_SERVICE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, LocalMoneyService entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String introduceQuestion = entity.getIntroduceQuestion();
        if (introduceQuestion != null) {
            stmt.bindString(2, introduceQuestion);
        }
 
        String introduceAnswer = entity.getIntroduceAnswer();
        if (introduceAnswer != null) {
            stmt.bindString(3, introduceAnswer);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LocalMoneyService entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String introduceQuestion = entity.getIntroduceQuestion();
        if (introduceQuestion != null) {
            stmt.bindString(2, introduceQuestion);
        }
 
        String introduceAnswer = entity.getIntroduceAnswer();
        if (introduceAnswer != null) {
            stmt.bindString(3, introduceAnswer);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public LocalMoneyService readEntity(Cursor cursor, int offset) {
        LocalMoneyService entity = new LocalMoneyService( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // introduceQuestion
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // introduceAnswer
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LocalMoneyService entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIntroduceQuestion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIntroduceAnswer(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(LocalMoneyService entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(LocalMoneyService entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(LocalMoneyService entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
