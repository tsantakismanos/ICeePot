package iceepotmobile.model;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import android.database.Cursor;

import iceepotmobile.application.DbHelper;

/**
 * Created by manos on 27/9/2015.
 */
public class Pot extends iceepotlib.entities.Pot {

    public static final String IDX_ID="ID";
    public static final String IDX_DESCR="DESCR";
    public static final String IDX_MIN_MOIST_VAL="MIN_MOIST_VAL";
    public static final String IDX_MAX_MOIST_VAL="MAX_MOIST_VAL";


    public  Pot(){
        super();
    }

    public Pot(int id, String descr, double min, double max) {
        super(descr, id, min, max);
    }


    public static Pot getPot(DbHelper dbHelper, int id){

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Pot p = new Pot();

        Cursor cur = db.query("POT", new String[]{"ID", "DESCR", "MIN_MOIST_VAL", "MAX_MOIST_VAL"}, "ID", new String[]{String.valueOf(id)}, null, null, null);

        while(!cur.isLast()){

            p = new Pot(cur.getInt(0), cur.getString(1), cur.getDouble(2), cur.getDouble(3));

            cur.moveToNext();
        }

        return p;
    }

    public static List<Pot> listPots(DbHelper dbHelper){

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<Pot> pots = new ArrayList<Pot>();

        Cursor cur = db.query("POT",new String[]{"ID","DESCR","MIN_MOIST_VAL","MAX_MOIST_VAL"},null,null,null,null,null);

        if(cur.moveToFirst()) {

            do  {
                Pot p = new Pot(cur.getInt(0), cur.getString(1), cur.getDouble(2), cur.getDouble(3));
                pots.add(p);
            }while (cur.moveToNext());
        }

        cur.close();

        return pots;
    }

    public void insert(DbHelper dbHelper){
        SQLiteDatabase db =dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Pot.IDX_ID, this.getId());
        cv.put(Pot.IDX_DESCR, this.getDescr());
        cv.put(Pot.IDX_MIN_MOIST_VAL, this.getMinMoistVal());
        cv.put(Pot.IDX_MAX_MOIST_VAL, this.getMaxMoistVal());

        db.insert("POT", null, cv);
    }
}
