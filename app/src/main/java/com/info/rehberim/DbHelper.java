package com.info.rehberim;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class DbHelper {
    public ArrayList<Person> getAllData(Database database){
        ArrayList<Person> personArrayList = new ArrayList<>();
        SQLiteDatabase db = database.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM rehberim ORDER BY name",null);


        while (c.moveToNext()){
            byte[] bytes =  c.getBlob(c.getColumnIndex("image"));
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            Person p = new Person(c.getInt(c.getColumnIndex("id")),c.getString(c.getColumnIndex("name")),
                    c.getString(c.getColumnIndex("phoneNumber")),
                    c.getString(c.getColumnIndex("email")),bitmap);
            personArrayList.add(p);
        }
        db.close();
        return personArrayList;
    }
    public void addPerson(Database database,String name,String phoneNumber, String email,byte[] image){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",name);
        values.put("phoneNumber",phoneNumber);
        values.put("email",email);
        values.put("image",image);

        db.insertOrThrow("rehberim",null,values);
        db.close();

    }
    public void deletePerson(Database database,int id){
        SQLiteDatabase db = database.getWritableDatabase();
        db.delete("rehberim","id=?",new String[]{String.valueOf(id)});
        db.close();
    }
    public void updatePerson (Database database,int id,String name,String phoneNumber,String email,byte[] image){
        SQLiteDatabase db = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",id);
        values.put("name",name);
        values.put("phoneNumber",phoneNumber);
        values.put("email",email);
        values.put("image",image);


        db.update("rehberim",values,"id=?",new String[]{String.valueOf(id)});

        db.close();
    }
}
