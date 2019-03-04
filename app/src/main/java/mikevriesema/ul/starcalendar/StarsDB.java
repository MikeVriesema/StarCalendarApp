package mikevriesema.ul.starcalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class StarsDB {

    /*********************
     * Definition of table columns
     ********************/
    //The index (key) column name for use in where clauses.
    public static final String KEY_ID = "_id";
    //The name and column index
    public static final String KEY_STAR_NAME = "star_name";

    private Context context;

    // Database open/upgrade helper
    private ModuleDBOpenHelper moduleDBOpenHelper;
    /************************
     * Constructor
     ***************************************************************/
    public StarsDB(Context context) {
        this.context = context;
        moduleDBOpenHelper = new ModuleDBOpenHelper(context, ModuleDBOpenHelper.DATABASE_NAME, null,
                ModuleDBOpenHelper.DATABASE_VERSION);

        // populate the database with some data in case it is empty
        if (getAll().length == 0) {
            this.addRow("Moon");
            this.addRow("Sun");
            this.addRow("Eclipse");
            this.addRow("Meteors");
            this.addRow("ISS");
            this.addRow("Spacecraft");
        }


    }

    /************************
     * Standard Database methods
     *************************************************/

    // Called when you no longer need access to the database.
    public void closeDatabase() {
        moduleDBOpenHelper.close();
    }

    public void addRow(String starName) {
        // Create a new row of values to insert.
        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put(KEY_STAR_NAME, starName);

        // Insert the row into your table
        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        db.insert(ModuleDBOpenHelper.DATABASE_TABLE, null, newValues);
    }

    public void deleteRow(int idNr) {
        // Specify a where clause that determines which row(s) to delete.
        // Specify where arguments as necessary.
        String where = KEY_ID + "=" + idNr;
        String whereArgs[] = null;

        // Delete the rows that match the where clause.
        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        db.delete(ModuleDBOpenHelper.DATABASE_TABLE, where, whereArgs);
    }

    public void deleteAll() {
        String where = null;
        String whereArgs[] = null;

        // Delete the rows that match the where clause.
        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        db.delete(ModuleDBOpenHelper.DATABASE_TABLE, where, whereArgs);
    }

    /************************
     * User specific database queries
     *******************************************/
   
  /*
   * Obtain all database entries and return as human readable content in a String array
   * A query with all fields set to null will result in the whole database being returned
   * The following SQL query is implemented: SELECT * FROM  Stars
   */
    public String[] getAll() {

        ArrayList<String> outputArray = new ArrayList<String>();
        String[] result_columns = new String[]{
                KEY_STAR_NAME};

        String starName;
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;
        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);
        //
        boolean result = cursor.moveToFirst();
        while (result) {
            starName = cursor.getString(cursor.getColumnIndex(KEY_STAR_NAME));

            outputArray.add(starName);
            result = cursor.moveToNext();

        }
        return outputArray.toArray(new String[outputArray.size()]);
    }



    /*
     * Obtain all star names from the database and return in String[]
     */
    public String[] getStarNames() {

        ArrayList<String> outputArray = new ArrayList<String>();
        String[] result_columns = new String[]{
                KEY_STAR_NAME};

        String starName;
        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);
        boolean result = cursor.moveToFirst();
        while (result) {
            starName = cursor.getString(cursor.getColumnIndex(KEY_STAR_NAME));
            outputArray.add(starName);
            result = cursor.moveToNext();
        }
        return outputArray.toArray(new String[outputArray.size()]);
    }




    /*
     * This is a helper class that takes a lot of the hassle out of using databases. Use as is and complete the following as required:
     * 	- DATABASE_TABLE
     * 	- DATABASE_CREATE
     */
    private static class ModuleDBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "myDatabase.db";
        private static final String DATABASE_TABLE = "Stars";
        private static final int DATABASE_VERSION = 1;

        // SQL Statement to create a new database.
        private static final String DATABASE_CREATE = "create table " +
                DATABASE_TABLE + " (" + KEY_ID +
                " integer primary key autoincrement, " +
                KEY_STAR_NAME + " text not null);";


        public ModuleDBOpenHelper(Context context, String name,
                                  CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // Called when no database exists in disk and the helper class needs
        // to create a new one.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        // Called when there is a database version mismatch meaning that
        // the version of the database on disk needs to be upgraded to
        // the current version.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            // Log the version upgrade.
            Log.w("TaskDBAdapter", "Upgrading from version " +
                    oldVersion + " to " +
                    newVersion + ", which will destroy all old data");

            // Upgrade the existing database to conform to the new
            // version. Multiple previous versions can be handled by
            // comparing oldVersion and newVersion values.

            // The simplest case is to drop the old table and create a new one.
            db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
            // Create a new one.
            onCreate(db);
        }
    }
}

