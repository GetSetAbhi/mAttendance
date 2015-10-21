package shugal.com.mattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 21/10/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Attendance";

    // Table Names
    private static final String KEY_ID = "id";
    private static final String LECTURE_TABLE_NAME = "lectures";


    //Lectures Table Columns
    private static final String LECTURE_NAME = "lecture_name";
    private static final String KEY_PRESENTS = "presents";
    private static final String KEY_ABSENTS = "absents";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table if not exists " + LECTURE_TABLE_NAME + "(" +
                KEY_ID + " integer primary key autoincrement, " +
                LECTURE_NAME + " string, " +
                KEY_PRESENTS + " real, " +
                KEY_ABSENTS + " real);";

        db.execSQL(query);

        Log.d("Error", "Creating Table");
        Log.d("Error", query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LECTURE_TABLE_NAME);
        onCreate(db);
    }

    public void deleteFirstLecture() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LECTURE_TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(1)});
        Log.d("Error", "Deleted Lecture");
        db.close();
    }

    public void addLecture(LectureData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LECTURE_NAME, data.get_lecture_name());
        values.put(KEY_PRESENTS, data.get_presents());
        values.put(KEY_ABSENTS, data.get_absents());


        db.insert(LECTURE_TABLE_NAME, null, values);
        Log.d("Error", "Added Balance");
        db.close();
    }

    public List<LectureData> showAllLectures() {
        List<LectureData> expenseList = new ArrayList<LectureData>();

        String selectQuery = "SELECT  * FROM " + LECTURE_TABLE_NAME + " order by id desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LectureData lectureData = new LectureData();
                lectureData.set_id(Integer.parseInt(cursor.getString(0)));
                lectureData.set_lecture_name(cursor.getString(1));
                lectureData.set_present(Float.parseFloat(cursor.getString(2)));
                lectureData.set_absent(Float.parseFloat(cursor.getString(3)));
                // Adding contact to list
                expenseList.add(lectureData);
            } while (cursor.moveToNext());
        }
        db.close();
        return expenseList;
    }

    public boolean isLectureListEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + LECTURE_TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if (icount > 0) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }
}