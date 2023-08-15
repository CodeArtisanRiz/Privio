package com.t3g.privio.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.t3g.privio.Model.dbModel;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "data.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE HistoryTable (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT , LINK TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS HistoryTable");
        onCreate(db);
    }

//    public void onDel() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DROP TABLE IF EXISTS HistoryTable");
////        db.execSQL("CREATE TABLE HistoryTable (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT , LINK TEXT)");
//        onCreate(db);
//    }
    public boolean insertData(String name, String link){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME",name);
        contentValues.put("LINK",link);

        long result = db.insert("HistoryTable",null,contentValues);

        return result != -1;
}
//    public Cursor getAllData(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM HistoryTable",null);
//        return cursor;
//    }

    public ArrayList<dbModel> getAllData() {
        ArrayList<dbModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM HistoryTable",null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String link = cursor.getString(2);
            dbModel dbModel = new dbModel(id,name,link);

            arrayList.add(dbModel);


        }
        return arrayList;
    }
}
