package org.ivmlab.proloop.proloop.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.ivmlab.proloop.proloop.RecyclerView.RecyclerViewClasses.Search;

import java.util.LinkedList;
import java.util.List;

import static android.support.v7.media.MediaItemMetadata.KEY_AUTHOR;

public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ProloopSQLite";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE searches ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "barCode TEXT, "+
                "styleNum TEXT )";

        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS books");

        // create fresh books table
        this.onCreate(db);
    }
    //---------------------------------------------------------------------

    /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */

    // Books table name
    private static final String TABLE_SEARCHES = "searches";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_BARCODE = "barCode";
    private static final String KEY_STYLENUM = "styleNum";

    private static final String[] COLUMNS = {KEY_ID,KEY_BARCODE,KEY_STYLENUM};


    public void addSearch(Search search){
        //for logging
        Log.d("addSearchHistory", search.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_BARCODE, search.getBarCode());
        values.put(KEY_STYLENUM, search.getStyleNum());

        // 3. insert
        db.insert(TABLE_SEARCHES, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Search getSearch(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor =
                db.query(TABLE_SEARCHES, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Search search = new Search();
        search.setId(Integer.parseInt(cursor.getString(0)));
        search.setBarCode(cursor.getString(1));
        search.setStyleNum(cursor.getString(2));

        //log
        Log.d("getSearch("+id+")", search.toString());

        return search;
    }

    public List<Search> getLastFiveSearches() {
        List<Search> searches = new LinkedList<Search>();

        String query = "SELECT  * FROM " + TABLE_SEARCHES + " ORDER BY id DESC LIMIT 5";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Search search = null;
        if (cursor.moveToFirst()) {
            do {
                search = new Search();
                search.setId(Integer.parseInt(cursor.getString(0)));
                search.setBarCode(cursor.getString(1));
                search.setStyleNum(cursor.getString(2));

                searches.add(search);
            } while (cursor.moveToNext());
        }

        Log.d("getLastFiveSearches()", searches.toString());

        return searches;
    }

    /*

    public int updateBook(Book book) {

    // 1. get reference to writable DB
    SQLiteDatabase db = this.getWritableDatabase();

    // 2. create ContentValues to add key "column"/value
    ContentValues values = new ContentValues();
    values.put("title", book.getTitle()); // get title
    values.put("author", book.getAuthor()); // get author

    // 3. updating row
    int i = db.update(TABLE_BOOKS, //table
            values, // column/value
            KEY_ID+" = ?", // selections
            new String[] { String.valueOf(book.getId()) }); //selection args

    // 4. close
    db.close();

    return i;

    }

    public void deleteBook(Book book) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_BOOKS, //table name
                KEY_ID+" = ?",  // selections
                new String[] { String.valueOf(book.getId()) }); //selections args

        // 3. close
        db.close();

        //log
    Log.d("deleteBook", book.toString());

    }

     */

}