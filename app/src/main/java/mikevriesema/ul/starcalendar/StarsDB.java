/*/////////////////////////////////
 * Application: StarCalendar
 *
 * Author: Mike Vriesema 17212359
 * Date: 25/04/2019
 */////////////////////////////////
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
    public static final String KEY_ID = "_id";
    public static final String KEY_STAR_NAME = "star_name";
    public static final String KEY_STAR_MASS = "star_mass";
    public static final String KEY_STAR_DIMENSION = "star_dim";
    public static final String KEY_STAR_GRAVITY = "star_grav";
    public static final String KEY_STAR_DESCRIPTION = "star_description";

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
        String[] result_columns = new String[]{KEY_ID};
        // populate the database with some data in case it is empty

        String where = null;
        String whereArgs[] = null;
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE, result_columns, where, whereArgs, groupBy, having, order);
        if (cursor.moveToFirst()) {

        }else{
            String[] planets = context.getResources().getStringArray(R.array.spaceplan_array);
            String[] planetsmass = context.getResources().getStringArray(R.array.spacemass_array);
            String[] planetsdiameter = context.getResources().getStringArray(R.array.spacedim_array);
            String[] planetsgravity = context.getResources().getStringArray(R.array.spacegrav_array);
            String[] planetsdesc = context.getResources().getStringArray(R.array.spacedesc_array);
            for (int i = 0; i < planets.length; i++) {
                this.addRow(planets[i], planetsmass[i], planetsdiameter[i],planetsgravity[i], planetsdesc[i]);
            }

        }
    }


    /************************
     * Standard Database methods
     *************************************************/

    // Called when you no longer need access to the database.
    public void closeDatabase() {
        moduleDBOpenHelper.close();
    }

    public void addRow(String starName, String starMass, String starDim, String starGrav, String starDescription) {
        // Create a new row of values to insert.
        ContentValues newValues = new ContentValues();

        // Assign values for each row.
        newValues.put(KEY_STAR_NAME, starName);
        newValues.put(KEY_STAR_MASS, starMass);
        newValues.put(KEY_STAR_DIMENSION, starDim);
        newValues.put(KEY_STAR_GRAVITY, starGrav);
        newValues.put(KEY_STAR_DESCRIPTION, starDescription);

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
        String[] result_columns = new String[]{KEY_STAR_NAME,KEY_STAR_MASS,KEY_STAR_DIMENSION,KEY_STAR_GRAVITY,KEY_STAR_DESCRIPTION};

        String starName;
        String starMass;
        String starDimension;
        String starGrav;
        String starDescription;
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
            starMass = cursor.getString(cursor.getColumnIndex(KEY_STAR_MASS));
            starDimension = cursor.getString(cursor.getColumnIndex(KEY_STAR_DIMENSION));
            starGrav = cursor.getString(cursor.getColumnIndex(KEY_STAR_GRAVITY));
            starDescription = cursor.getString(cursor.getColumnIndex(KEY_STAR_DESCRIPTION));
            outputArray.add(starName + "\n" +  starMass + "\n" + starDimension + "\n" + starGrav + "\n" + starDescription);
            result = cursor.moveToNext();

        }
        return outputArray.toArray(new String[outputArray.size()]);
    }



    /*
     * Obtain all star names from the database and return in String[]
     */
    public String[] getStarNames() {

        ArrayList<String> outputArray = new ArrayList<String>();
        outputArray.add("-");
        String[] result_columns = new String[]{
                KEY_ID, KEY_STAR_NAME};

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
     * Obtain all star mass from the database and return in String
     */
    public String getStarMass(String starName) {

        String[] result_columns = new String[]{
                KEY_ID, KEY_STAR_MASS};

        String where = KEY_STAR_NAME + " = ?";
        String whereArgs[] = {starName};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(KEY_STAR_MASS));
        } else return context.getString(R.string.error_one);
    }

    /*
     * Obtain all star dimensions from the database and return in String
     */
    public String getStarDimensions(String starName) {
        String[] result_columns = new String[]{
                KEY_ID, KEY_STAR_DIMENSION};

        String where = KEY_STAR_NAME + " = ?";
        String whereArgs[] = {starName};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(KEY_STAR_DIMENSION));
        } else return context.getString(R.string.error_one);
    }

    /*
     * Obtain all star descriptions from the database and return in String
     */
    public String getStarDescriptions(String starName) {

        String[] result_columns = new String[]{
                KEY_ID, KEY_STAR_DESCRIPTION};

        String where = KEY_STAR_NAME + " = ?";
        String whereArgs[] = {starName};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(KEY_STAR_DESCRIPTION));
        } else return context.getString(R.string.error_one);
    }

    /*
     * Obtain all star gravity from the database and return in String
     */
    public String getStarGravity(String starName) {
        String[] result_columns = new String[]{
                KEY_ID, KEY_STAR_GRAVITY};

        String where = KEY_STAR_NAME + " = ?";
        String whereArgs[] = {starName};
        String groupBy = null;
        String having = null;
        String order = null;

        SQLiteDatabase db = moduleDBOpenHelper.getWritableDatabase();
        Cursor cursor = db.query(ModuleDBOpenHelper.DATABASE_TABLE,
                result_columns, where,
                whereArgs, groupBy, having, order);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex(KEY_STAR_GRAVITY));
        } else return context.getString(R.string.error_one);
    }


    /*
     * This is a helper class that takes a lot of the hassle out of using databases. Use as is and complete the following as required:
     * 	- DATABASE_TABLE
     * 	- DATABASE_CREATE
     */
    private static class ModuleDBOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "StarCalendar.db";
        private static final String DATABASE_TABLE = "Stars";
        private static final int DATABASE_VERSION = 2;

        // SQL Statement to create a new database.
        private static final String DATABASE_CREATE = "CREATE TABLE " +
                DATABASE_TABLE + " (" +
                KEY_ID + " integer PRIMARY KEY AUTOINCREMENT, " +
                KEY_STAR_NAME + " text, " +
                KEY_STAR_MASS + " text, " +
                KEY_STAR_DIMENSION + " text, " +
                KEY_STAR_GRAVITY + " text, " +
                KEY_STAR_DESCRIPTION + " text ); ";


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
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            // Create a new one.
            onCreate(db);
        }
    }
}

