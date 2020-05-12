package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class petdbhelper extends SQLiteOpenHelper {

    private static String LOG_TAG= petdbhelper.class.getSimpleName();
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME= "shelter.db";


    public petdbhelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PETS_TABLE= "CREATE TABLE " + petsContract.petsEntry.TABLE_NAME + "("
                                    + petsContract.petsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + petsContract.petsEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                                    + petsContract.petsEntry.COLUMN_PET_BREED + " TEXT, "
                                    + petsContract.petsEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                                    + petsContract.petsEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
