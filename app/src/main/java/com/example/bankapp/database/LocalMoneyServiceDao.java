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
        public final static Property IntroduceType = new Property(1, String.class, "introduceType", false, "INTRODUCE_TYPE");
        public final static Property IntroduceQuestion = new Property(2, String.class, "introduceQuestion", false, "INTRODUCE_QUESTION");
        public final static Property IntroduceAnswer = new Property(3, String.class, "introduceAnswer", false, "INTRODUCE_ANSWER");
        public final static Property IntroduceAction = new Property(4, String.class, "introduceAction", false, "INTRODUCE_ACTION");
        public final static Property IntroduceActionData = new Property(5, String.class, "introduceActionData", false, "INTRODUCE_ACTION_DATA");
        public final static Property IntroduceExpression = new Property(6, String.class, "introduceExpression", false, "INTRODUCE_EXPRESSION");
        public final static Property IntroduceExpressionData = new Property(7, String.class, "introduceExpressionData", false, "INTRODUCE_EXPRESSION_DATA");
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
                "\"INTRODUCE_TYPE\" TEXT," + // 1: introduceType
                "\"INTRODUCE_QUESTION\" TEXT," + // 2: introduceQuestion
                "\"INTRODUCE_ANSWER\" TEXT," + // 3: introduceAnswer
                "\"INTRODUCE_ACTION\" TEXT," + // 4: introduceAction
                "\"INTRODUCE_ACTION_DATA\" TEXT," + // 5: introduceActionData
                "\"INTRODUCE_EXPRESSION\" TEXT," + // 6: introduceExpression
                "\"INTRODUCE_EXPRESSION_DATA\" TEXT);"); // 7: introduceExpressionData
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
 
        String introduceType = entity.getIntroduceType();
        if (introduceType != null) {
            stmt.bindString(2, introduceType);
        }
 
        String introduceQuestion = entity.getIntroduceQuestion();
        if (introduceQuestion != null) {
            stmt.bindString(3, introduceQuestion);
        }
 
        String introduceAnswer = entity.getIntroduceAnswer();
        if (introduceAnswer != null) {
            stmt.bindString(4, introduceAnswer);
        }
 
        String introduceAction = entity.getIntroduceAction();
        if (introduceAction != null) {
            stmt.bindString(5, introduceAction);
        }
 
        String introduceActionData = entity.getIntroduceActionData();
        if (introduceActionData != null) {
            stmt.bindString(6, introduceActionData);
        }
 
        String introduceExpression = entity.getIntroduceExpression();
        if (introduceExpression != null) {
            stmt.bindString(7, introduceExpression);
        }
 
        String introduceExpressionData = entity.getIntroduceExpressionData();
        if (introduceExpressionData != null) {
            stmt.bindString(8, introduceExpressionData);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, LocalMoneyService entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String introduceType = entity.getIntroduceType();
        if (introduceType != null) {
            stmt.bindString(2, introduceType);
        }
 
        String introduceQuestion = entity.getIntroduceQuestion();
        if (introduceQuestion != null) {
            stmt.bindString(3, introduceQuestion);
        }
 
        String introduceAnswer = entity.getIntroduceAnswer();
        if (introduceAnswer != null) {
            stmt.bindString(4, introduceAnswer);
        }
 
        String introduceAction = entity.getIntroduceAction();
        if (introduceAction != null) {
            stmt.bindString(5, introduceAction);
        }
 
        String introduceActionData = entity.getIntroduceActionData();
        if (introduceActionData != null) {
            stmt.bindString(6, introduceActionData);
        }
 
        String introduceExpression = entity.getIntroduceExpression();
        if (introduceExpression != null) {
            stmt.bindString(7, introduceExpression);
        }
 
        String introduceExpressionData = entity.getIntroduceExpressionData();
        if (introduceExpressionData != null) {
            stmt.bindString(8, introduceExpressionData);
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
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // introduceType
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // introduceQuestion
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // introduceAnswer
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // introduceAction
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // introduceActionData
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // introduceExpression
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // introduceExpressionData
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, LocalMoneyService entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setIntroduceType(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setIntroduceQuestion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setIntroduceAnswer(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIntroduceAction(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIntroduceActionData(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIntroduceExpression(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIntroduceExpressionData(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
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
