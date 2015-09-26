package iceepotmobile.application;

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
