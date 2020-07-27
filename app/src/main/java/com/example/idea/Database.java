package com.example.idea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

class Database extends SQLiteOpenHelper {
    private static Context context;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Ideas.db";
    private static final String DB_PATH_SUFFIX = "/databases/";
    private static SQLiteDatabase database;
    private static Database mInstance;
    private String tableName = "ideas";
    private String id = "id";
    private String columnName = "name";
    private String columnAuthor = "author";
    private String columnCategory = "category";
    private String columnBody = "body";
    private Idea idea;
    private ArrayList<Idea> data;

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase aDatabase, int oldVersion, int newVersion) {

    }

    public void initialise() {
        if (mInstance == null) {
            if (!checkDatabase()) {
                copyDataBase();
            }
            mInstance = new Database(context);
            database = mInstance.getWritableDatabase();
        }
    }

    public Database getInstance(){
        return mInstance;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }


    private static void copyDataBase() {

        try {
            InputStream myInput = context.getAssets().open("databases/"+DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(context.getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean checkDatabase() {
        SQLiteDatabase checkDB = null;

        try {
            try {
                String myPath = getDatabasePath();
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READONLY);
                checkDB.close();
            } catch (Exception e) { }
        } catch (Throwable ex) {
        }
        return checkDB != null ? true : false;
    }

    private static String getDatabasePath() {
        return context.getApplicationInfo().dataDir + DB_PATH_SUFFIX
                + DATABASE_NAME;
    }

    public ArrayList<Idea> selectCategoryData(String aCategory) {
        data = new ArrayList<Idea>();

        String selectQuery = "SELECT  * FROM " + tableName + " where category = '" + aCategory + "';";
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        addIntoListObjectIdea(cursor);

        while (cursor.moveToNext()) {
            addIntoListObjectIdea(cursor);
        }
        cursor.close();
        database.close();
        return data;
    }

    public ArrayList<Idea> selectData() {
        try {
            data = new ArrayList<Idea>();

            String selectQuery = "SELECT  * FROM " + tableName;
            database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery(selectQuery, null);

            cursor.moveToFirst();
            addIntoListObjectIdea(cursor);

            while (cursor.moveToNext()) {
                addIntoListObjectIdea(cursor);
            }
            cursor.close();
            database.close();
            return data;
        } catch (CursorIndexOutOfBoundsException exception) {
            return data;
        }
    }

    public void addIntoListObjectIdea(Cursor aCursor) {
        idea = new Idea();
        idea.setId(aCursor.getString(aCursor.getColumnIndex(id)));
        idea.setName(aCursor.getString(aCursor.getColumnIndex(columnName)));
        idea.setBody(aCursor.getString(aCursor.getColumnIndex(columnBody)));
        idea.setAuthor(aCursor.getString(aCursor.getColumnIndex(columnAuthor)));

        data.add(idea);
    }

    public long addIdeaIntoDatabase(String aName, String aCategory, String aAuthor, String aBody) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(columnName, aName);
        contentValues.put(columnCategory, aCategory);
        contentValues.put(columnAuthor, aAuthor);
        contentValues.put(columnBody, aBody);

        long result = database.insert(tableName, null, contentValues);

        return result;
    }


    public ArrayList selectCategory(){
        ArrayList selectedCategory = new ArrayList();

        String selectQuery = "SELECT DISTINCT"+columnCategory +"FROM " + tableName;
        database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        cursor.moveToFirst();
        String category = cursor.getString(cursor.getColumnIndex(columnCategory));
        selectedCategory.add(category);

        while (cursor.moveToNext()) {
            category = cursor.getString(cursor.getColumnIndex(columnCategory));
            selectedCategory.add(category);
        }

        cursor.close();
        database.close();

        return selectedCategory;
    }


}
