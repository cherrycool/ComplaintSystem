package com.example.cherry.complaintsystem;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class DBContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.cherry.complaintsystem.DBContentProvider";
    static final String URL_COMPLAINTS = "content://" + PROVIDER_NAME + "/complaints_uri";
    static final Uri CONTENT_URI_COMPLAINTS = Uri.parse(URL_COMPLAINTS);

    static final String _ID = "_id";
    static final String _SID = "_server_id"; //ID of the row in the complaint_details table in server -- If Null -- is not synced
    static final String COMPLAINT_CLASS = "complaint_class";
    static final String COMPLAINT_ISSUE = "complaint_issue";
    static final String COMPLAINT_DATE = "complaint_date";
    static final String COMPLAINT_STATUS = "complaint_status";

    private static HashMap<String, String> COMPLAINT_PROJECTION_MAP;

    static final int complaints = 1;
    static final int complaint_id = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "complaints_uri", complaints);
        uriMatcher.addURI(PROVIDER_NAME, "complaints_uri/#", complaint_id);
    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "complaints_local_db";
    static final String TABLE_NAME_1 = "complaints_local_table";

    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE_1 =
            " CREATE TABLE IF NOT EXISTS " + TABLE_NAME_1 +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " _server_id INT, " +
                    " complaint_class TEXT NOT NULL, " +
                    " complaint_issue TEXT NOT NULL," +
                    " complaint_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    " complaint_status INT NOT NULL);";

    public DBContentProvider() {
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE_1);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME_1);
            onCreate(db);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.

        int count = 0;
        switch (uriMatcher.match(uri)) {
            case complaints:
                count = db.delete(TABLE_NAME_1, selection, selectionArgs);
                break;

            case complaint_id:
                String id = uri.getPathSegments().get(1);
                count = db.delete(TABLE_NAME_1, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return count;
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        //Switch case to handle multiple tables
        Uri _uri = null;
        long rowID;

        switch (uriMatcher.match(uri)) {
            case complaints:
                //Insert into local complaints table
                rowID = db.insert(TABLE_NAME_1, "", values);

                if (rowID > 0) {
                    _uri = ContentUris.withAppendedId(CONTENT_URI_COMPLAINTS, rowID);
                    getContext().getContentResolver().notifyChange(_uri, null);
                    return _uri;
                }
                break;

            default: throw new SQLException("Failed to add a record into " + uri);
        }

        return uri;

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)) {
            case complaints:
                qb.setTables(TABLE_NAME_1);
                qb.setProjectionMap(COMPLAINT_PROJECTION_MAP);
                if (sortOrder == null || sortOrder == "") {

                    sortOrder = _ID;
                }
                break;

            case complaint_id:
                qb.setTables(TABLE_NAME_1);
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                if (sortOrder == null || sortOrder == "") {

                    sortOrder = _ID;
                }
                break;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;

        //throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.

        int count = 0;
        switch (uriMatcher.match(uri)) {
            case complaints:
                count = db.update(TABLE_NAME_1, values, selection, selectionArgs);
                break;

            case complaint_id:
                count = db.update(TABLE_NAME_1, values,
                        _ID + " = " + uri.getPathSegments().get(1) +
                                (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);
                break;

        }


        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
