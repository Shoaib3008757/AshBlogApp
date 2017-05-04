package ashir.ashblogapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    Context context;

    public static final String DATABASE_NAME = "ashirblog.db";
    private static final int DatabaseVersion = 1;
    private static final String NAME_OF_TABLE = "ashirblogTable";
    public static final String Col_1 = "id";
    public static final String Col_2 = "Text";
    public static final String Col_3 = "Image";
    public static final String virtualTable = "a";
    public static final String virtual_Col_1 = "virtual_id";
    public static final String virtua_Col_2 = "virtual_Text";

    String CREATE_TABLE_CALL = "CREATE TABLE " + NAME_OF_TABLE + "(" + Col_1 + " integer PRIMARY KEY AUTOINCREMENT," + Col_2 + " TEXT, " + Col_3 + " BLOB, " + "tag TEXT" + ")";
    String Create_Virtual_Table_Call = "CREATE TABLE " + virtualTable + "(" + virtual_Col_1 + " integer PRIMARY KEY," + virtua_Col_2 + " TEXT" + ")";;

    DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DatabaseVersion);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_CALL);
        db.execSQL(Create_Virtual_Table_Call);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + Create_Virtual_Table_Call);

    }

    //inserting post in databse
    public long ashirPostCreating(AshirBlogPostingHelper post) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_2, post.getPost());
        values.put(Col_3, post.getImage());
        //inserting valuse into table columns
        result = db.insert(NAME_OF_TABLE, null, values);
        db.close();
        return result;

    }



    /* fetching records from Database Table*/
    public ArrayList<AshirBlogPostingHelper> getAllPots() {
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        ArrayList<AshirBlogPostingHelper> ashirPostsList = new ArrayList<AshirBlogPostingHelper>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                String post = c.getString(c.getColumnIndex(Col_2));
                try {
                    byte[] getImageFromDb = c.getBlob(c.getColumnIndex(Col_3));
                    AshirBlogPostingHelper postHelper = new AshirBlogPostingHelper();

                    postHelper.setDataPost(post);
                    postHelper.setImage(getImageFromDb);

                    ashirPostsList.add(postHelper);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        db.close();
        return ashirPostsList;

    }

    //Updatating post
    public boolean updateTable(int id, String postToUpdate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_2,postToUpdate);
        db.update(NAME_OF_TABLE, contentValues, "id = ?", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    //deleting post
    public boolean deleteFromTable(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_OF_TABLE, Col_1 + "=" + rowId, null);
        db.close();

        return true;

    }

}