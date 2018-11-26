package com.example.amita.simplycs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import com.example.amita.simplycs.Database.SectionTable;


public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "simply-cs";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Section table
        db.execSQL(SectionTable.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SectionTable.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String note) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(SectionTable.COLUMN_NAME, note);

        // insert row
        long id = db.insert(SectionTable.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public SectionTable getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(SectionTable.TABLE_NAME,
                new String[]{SectionTable.COLUMN_ID, SectionTable.COLUMN_NAME, SectionTable.COLUMN_TIMESTAMP},
                SectionTable.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        SectionTable note = new SectionTable(
                cursor.getInt(cursor.getColumnIndex(SectionTable.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(SectionTable.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(SectionTable.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<SectionTable> getAllNotes() {
        List<SectionTable> notes = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + SectionTable.TABLE_NAME + " ORDER BY " +
                SectionTable.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SectionTable note = new SectionTable();
                note.setId(cursor.getInt(cursor.getColumnIndex(SectionTable.COLUMN_ID)));
                note.setNote(cursor.getString(cursor.getColumnIndex(SectionTable.COLUMN_NAME)));
                note.setTimestamp(cursor.getString(cursor.getColumnIndex(SectionTable.COLUMN_TIMESTAMP)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }

    public int getNotesCount() {
        String countQuery = "SELECT  * FROM " + SectionTable.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateNote(SectionTable note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SectionTable.COLUMN_NAME, note.getNote());

        // updating row
        return db.update(SectionTable.TABLE_NAME, values, SectionTable.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

}
