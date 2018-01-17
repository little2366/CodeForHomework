package cn.edu.hznu.labdataforsqlite;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DatabaseProvider extends ContentProvider {
    public static final int CONTACT_DIR = 0;   //访问contact表中的所有数据
    public static final int CONTACT_ITEM = 1;  //访问contact表中的姓名信息

    public static final String AUTHORITY = "cn.edu.hznu.labdataforsqlite.provider";
    private static UriMatcher uriMatcher;
    private MyDatabaseHelper dbHelper;

    /*对UriMatcher进行了初始化，将期望匹配的几种URI格式添加进去*/
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "contact", CONTACT_DIR);
        uriMatcher.addURI(AUTHORITY, "contact/#", CONTACT_ITEM);
    }

    public DatabaseProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //删除数据
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deleteRows = 0;
        switch(uriMatcher.match(uri)){
            case CONTACT_DIR:
                deleteRows = db.delete("contact", selection, selectionArgs);
                break;
            case CONTACT_ITEM:
                String contactId = uri.getPathSegments().get(1);
                deleteRows = db.delete("contact", "id = ?", new String[] {contactId});
                break;
            default:
                break;
        }
        return deleteRows;
    }

    @Override
    public String getType(Uri uri) {
        switch(uriMatcher.match(uri)){
            case CONTACT_DIR:
                return "vnd.android.cursor.dir/vnd.cn.edu.hznu.labdataforsqlite.provider.contact";
            case CONTACT_ITEM:
                return "vnd,android.cursor.item/vnd.cn.edu.hznu.labdataforsqlite.provider.contact";
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //添加数据

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Uri uriReturn = null;
        switch(uriMatcher.match(uri)){
            case CONTACT_DIR:
            case CONTACT_ITEM:
                long newContactId = db.insert("contact", null, values);
                uriReturn = Uri.parse("content://" + AUTHORITY + "/contact/" + newContactId);
                break;
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new MyDatabaseHelper(getContext(), "contact.db", null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //查询数据
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        switch(uriMatcher.match(uri)) {
            case CONTACT_DIR:
                cursor = db.query("contact", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CONTACT_ITEM:
                String contactId = uri.getPathSegments().get(1);
                cursor = db.query("contact", projection, "id = ?", new String[]{contactId}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //更新数据库
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int updateRows = 0;
        switch(uriMatcher.match(uri)){
            case CONTACT_DIR:
                updateRows = db.update("contact", values, selection, selectionArgs);
                break;
            case CONTACT_ITEM:
                String contactId = uri.getPathSegments().get(1);
                updateRows = db.update("contact", values, "id = ?", new String[] {contactId});
                break;
            default:
                break;
        }
        return updateRows;
    }
}
