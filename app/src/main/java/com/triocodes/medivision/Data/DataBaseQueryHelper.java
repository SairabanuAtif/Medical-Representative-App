package com.triocodes.medivision.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by admin on 10-02-16.
 */
public class DataBaseQueryHelper {
    static DataBaseQueryHelper databasequeryhelper;
    static SQLiteDatabase db;
    private DataBaseHelper dbhelper;
    public DataBaseQueryHelper(Context myContext, SQLiteDatabase db) {
        // TODO Auto-generated constructor stub
        databasequeryhelper = this;
        dbhelper = new DataBaseHelper(myContext);

        this.db = db;

    }
    public static DataBaseQueryHelper getInstance() {
        return databasequeryhelper;
    }
    public String getUserId() {
        String EmpId = null;
        Cursor mCur = db.rawQuery("Select * from LoginDetails", null);
        if (mCur != null && mCur.moveToFirst()) {
            EmpId = mCur.getString(mCur.getColumnIndex("UserId"));
        }
        return EmpId;
    }
    public String getPassword() {
        String EmpId = null;
        Cursor mCur = db.rawQuery("Select * from LoginDetails", null);
        if (mCur != null && mCur.moveToFirst()) {
            EmpId = mCur.getString(mCur.getColumnIndex("Password"));
        }
        return EmpId;
    }
    public void insertEntry(String UserId, String Password) {
        // SQLiteDatabase db = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserId", UserId);
        values.put("Password", Password);
        db.insert("LoginDetails", null, values);

    }

    public int updateEntry(String UserId, String Password) {
       /* String EmpId = null;
        Cursor mCur = db.rawQuery("update EmployeeDetails set Password="+Password, null);
        if (mCur != null && mCur.moveToFirst()) {
            EmpId = mCur.getString(mCur.getColumnIndex("EmployeeCode"));
        }*/
        ContentValues args = new ContentValues();
        args.put("UserId", UserId);
        args.put("Password", Password);
        return db.update("LoginDetails", args, "UserId=" + UserId, null);

    }

    public int isNewUser() {
        int isNew = 0;
        Cursor mCur = db.rawQuery("Select * from LoginDetails", null);
        if (mCur != null && mCur.moveToFirst()) {
            isNew = 1;
        }
        return isNew;
    }

}
