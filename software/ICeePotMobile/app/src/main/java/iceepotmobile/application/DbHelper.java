package iceepotmobile.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by manos on 26/9/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static String DATABASE = "database.db";

    private static DbHelper dbHelper;

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();

        db.execSQL("CREATE TABLE POT (ID INTEGER PRIMARY_KEY, DESCR TEXT, MIN_MOIST_VAL FLOAT, MAX_MOIST_VAL FLOAT);");
        db.execSQL("CREATE TABLE SETTINGS (HOST TEXT, PORT INTEGER, TIMEOUT INTEGER);");

        /*ContentValues cv;

        cv = new ContentValues();
        cv.put("ID",0);
        cv.put("DESCR","Basil");
        cv.put("MIN_MOIST_VAL", 0);
        cv.put("MAX_MOIST_VAL", 0);
        db.insert("POT", null, cv);

        cv = new ContentValues();
        cv.put("ID",1);
        cv.put("DESCR","Mint");
        cv.put("MIN_MOIST_VAL",0);
        cv.put("MAX_MOIST_VAL",0);
        db.insert("POT", null, cv);

        cv = new ContentValues();
        cv.put("ID",2);
        cv.put("DESCR","Lemon Tree");
        cv.put("MIN_MOIST_VAL",0);
        cv.put("MAX_MOIST_VAL",0);
        db.insert("POT", null, cv);*/

        db.setTransactionSuccessful();

        db.endTransaction();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static DbHelper getIntance(Context ctx){
        if(dbHelper != null)
            return dbHelper;
        else {
            dbHelper = new DbHelper(ctx, DbHelper.DATABASE, null, 1);
            return dbHelper;
        }
    }
}
