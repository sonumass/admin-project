package com.sonu.resdemo.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nitish on 1/25/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="vrsdemo";
    public static final int DATABASE_VERSION = 1;
    public static final String id="id";
    public static final String item_code="item_code";
    public static final String quantity="quantity";
    public static final String item_price="item_price";
    public static final String item_name="item_name";
    public static final String item_actual_price="actual_price";

    SQLiteDatabase sqLiteDatabase;

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String carttable= "CREATE TABLE carttable ("+id+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "+item_code+ " TEXT(50) ,"+item_actual_price+ " TEXT(50) , "+quantity+" TEXT(50), "+item_name+" TEXT(100), "+item_price+" TEXT(50)  )";
        sqLiteDatabase.execSQL(carttable);
    }







    public long insertitem(String item_code, String quantity,String price,String item_name,String actual_price)
    {
        ContentValues cv=new ContentValues();
        cv.put(this.item_code,item_code);
        cv.put(this.quantity,quantity);
        cv.put(this.item_price,price);
        cv.put(this.item_name,item_name);
        cv.put(this.item_actual_price,actual_price);
        Log.e("inert",price);
        return sqLiteDatabase.insert("carttable",null,cv);
    }
    public Cursor getItem(String itemcode)
    {


        return sqLiteDatabase.rawQuery("Select * FROM carttable where item_code='"+itemcode+"'",null);
    }
    public Cursor getOrderItem()
    {


        return sqLiteDatabase.rawQuery("Select * FROM carttable",null);
    }

    public Cursor getItemcount()
    {


        return sqLiteDatabase.rawQuery("Select * FROM carttable",null);
    }

    public String getTotalItem()
    {
        String count="0";
        int   countt=0;
        Cursor cur = sqLiteDatabase.rawQuery("SELECT SUM(quantity) FROM carttable", null);
        if(cur.moveToFirst())
        {
            countt=  cur.getInt(0);
        }
        return String.valueOf(countt);
    }

    public String getTotalItemprice()
    {
        String count="0";
        int   countt=0;
        Cursor cur = sqLiteDatabase.rawQuery("SELECT SUM(item_price) FROM carttable", null);
        if(cur.moveToFirst())
        {
            countt=  cur.getInt(0);
        }
        return String.valueOf(countt);
    }




    public int updatecart(String item_code,String quantity,String Price)
    {
        ContentValues cv=new ContentValues();
        cv.put(this.item_code,item_code);
        cv.put(this.quantity,quantity);
        cv.put(this.item_price,Price);
        Log.e("update",quantity);
       return sqLiteDatabase.update("carttable",cv,"item_code='"+item_code+"'",null);
    }

    public boolean delete(String item_code) {
        SQLiteDatabase database = this.getReadableDatabase();
        final String whereClause = "item_code" + "=?";
        final String whereArgs[] = {item_code};
        int affectedRows = database.delete("carttable", whereClause, whereArgs);
        return affectedRows > 0;
    }
 public  void clearorder(){
        Log.e("delete",item_code);
   SQLiteDatabase database = this.getWritableDatabase();

   database.execSQL("delete from carttable");
    }

}
