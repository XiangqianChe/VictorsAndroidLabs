package algonquin.cst2335.victorsandroidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyOpenHelper extends SQLiteOpenHelper {

    public static final String name = "TheDatabase";
    public static final int version = 1;
    private static final String TABLE_NAME = "Messages";
    private static final String col_message = "Message";
    private static final String col_send_receive = "SendOrReceive";
    private static final String col_time_sent = "TimeSent";

    public MyOpenHelper(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + col_message + " TEXT,"
                + col_send_receive + " INTEGER,"
                + col_time_sent + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
