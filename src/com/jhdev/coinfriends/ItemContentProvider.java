package com.jhdev.coinfriends;

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
import android.util.Log;

public class ItemContentProvider extends ContentProvider {
    public static final String PROVIDER_NAME = "com.jhdev.coinfriends.items";

    /** A uri to do operations on locations table. A content provider is identified by its uri */
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/items" );

    /** Constant to identify the requested operation */
    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;

    private static final UriMatcher uriMatcher ;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "items", ITEMS);
        uriMatcher.addURI(PROVIDER_NAME, "items/#", ITEM_ID);
    }

    //instance variable for SQLiteDatabase
    private SQLiteDatabase mDB;
    //table name, a constant
    private static final String DATABASE_TABLE = "items";

    // Fields for locations table. keep this in order.
    public static final String FIELD_ROW_ID ="_id";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_COIN_TYPE = "type";
    public static final String FIELD_COIN_ADDRESS = "coin_address";
    public static final String FIELD_COMMENTS = "comments";
    public static final String FIELD_ACTIVE = "active";


    // SQLITE SECTION
    public class ItemsDB extends SQLiteOpenHelper{

        //Database Name
        private static final String DBNAME = "itemsqlitedb";

        //Version number of database
        private static final int VERSION = 2;

        //constructor
        public ItemsDB(Context context) {
            super(context, DBNAME, null, VERSION);
            //this.mDB = getWritableDatabase();
        }

        /** This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called
         * provided the database does not exists
         * */
        @Override
        public void onCreate(SQLiteDatabase db) {
            String create_table_items =     "create table " + DATABASE_TABLE + " ( " +
                    FIELD_ROW_ID + " integer primary key autoincrement , " +
                    FIELD_NAME + " TEXT NULL , " +
                    FIELD_COIN_TYPE + " TEXT NOT NULL , " +
                    FIELD_COIN_ADDRESS + " TEXT NOT NULL, " +
                    FIELD_COMMENTS + " TEXT NULL , " +
                    FIELD_ACTIVE + " BOOLEAN YES " +
                    " ) ";

            db.execSQL(create_table_items);
            Log.e("DB", "DB: items table created" );

            ContentValues first_coin = new ContentValues();
            first_coin.put(FIELD_NAME, "Reddit donation BTC address");
            first_coin.put(FIELD_COIN_ADDRESS, "14ttsooq42L35imoNWThdJ57x5FcytwwQh");
            first_coin.put(FIELD_COIN_TYPE, "BTC");
            db.insert(DATABASE_TABLE, null, first_coin);
            Log.d("DB", "First coin inserted");
            ContentValues second_coin = new ContentValues(); 
            second_coin.put(FIELD_NAME, "Reddit donation LTC address");
            second_coin.put(FIELD_COIN_ADDRESS, "LTmwQvJ9Bh4sAgz3mFrQXMrxHUYmd17kAc");
            second_coin.put(FIELD_COIN_TYPE, "LTC");
            db.insert(DATABASE_TABLE, null, second_coin);
            Log.d("DB", "Second coin inserted");

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(ItemsDB.class.getName(), "Upgrading database from version "
                    + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            mDB.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(mDB);
        }
    }
    // END SqliteHelper code
    // Return back to ContentProvider code

    /** A callback method which is invoked when the content provider is starting up */
    @Override
    public boolean onCreate() {
        //mDB = new LocationsDB(getContext());
        Context context = getContext();
        ItemsDB dbHelper = new ItemsDB(context);

        mDB = dbHelper.getWritableDatabase();
        return (mDB == null)? false:true;
    }

    /** A callback method which is invoked when insert operation is requested on this content provider */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDB.insert(DATABASE_TABLE, null, values);
        Uri _uri=null;
        if(rowID>0){
            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        }else {
            try {
                throw new SQLException("Failed to insert : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return _uri;
    }

    /** A callback method which is invoked when update operation is requested on this content provider */
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    /** A callback method which is invoked when delete operation is requested on this content provider */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //int cnt = mDB.delete(DATABASE_TABLE, null, null);
        int count = 0;

        switch (uriMatcher.match(uri)){
            case ITEMS:
                count = mDB.delete(DATABASE_TABLE, selection, selectionArgs);
                break;
            case ITEM_ID:
                String id = uri.getPathSegments().get(1);
                count = mDB.delete(DATABASE_TABLE, FIELD_ROW_ID + " = " + id, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /** A callback method which is invoked when query operation is requested on this content provider */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(DATABASE_TABLE);

        switch (uriMatcher.match(uri)) {
            case ITEMS:
//        	builder = "SELECT a._id, a.lat, a.lng, a.sub_map_id, a.zoom, a.address, a.title, b._id AS b_ID, b.sub_map FROM locations a" +
//        			" LEFT JOIN sub_map_table b ON a.sub_map_id=b._id";;
                break;
            case ITEM_ID:
                builder.appendWhere(FIELD_ROW_ID + " = " + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == ""){
            sortOrder = FIELD_ROW_ID;
        }

        Cursor c = builder.query(mDB, projection, selection, selectionArgs,
                null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }
}

