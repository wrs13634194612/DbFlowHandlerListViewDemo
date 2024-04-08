package com.example.mepositry;




        import android.database.Cursor;
        import android.util.Log;

        import com.raizlabs.android.dbflow.sql.language.property.Property;
        import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

        import java.lang.reflect.Field;
        import java.util.ArrayList;
        import java.util.Date;
        import java.util.List;

/**
 * @author @iaindownie on 14/09/2021.
 */

public class DatabaseUtilities {

    private static String currentSQLTimeInMSecs = "CAST(strftime('%s','now') AS INTEGER) * 1000";

    public static void updatedTimestampTrigger(DatabaseWrapper db, Class modelClass) {

        StringBuilder query = new StringBuilder();
        query.append("CREATE TRIGGER IF NOT EXISTS " + modelClass.getSimpleName() + "UpdateTimestampTrigger" +
                " AFTER UPDATE On " + modelClass.getSimpleName() +
                " FOR EACH ROW BEGIN UPDATE " + modelClass.getSimpleName() + //FOR EACH ROW
                " SET updatedAt = " + currentSQLTimeInMSecs +
                " WHERE old._ID = _ID;" +
                " END; ");
        //Log.d("INFO", query.toString());
        db.execSQL(query.toString());
    }

    public static void createdTimestampTrigger(DatabaseWrapper db, Class modelClass) {

        StringBuilder query = new StringBuilder();
        query.append("CREATE TRIGGER IF NOT EXISTS " + modelClass.getSimpleName() + "InsertTimestampTrigger" +
                " AFTER INSERT ON " + modelClass.getSimpleName() +
                " FOR EACH ROW BEGIN " + //FOR EACH ROW
                " UPDATE " + modelClass.getSimpleName() +
                " SET createdAt = " + currentSQLTimeInMSecs + ", updatedAt = " + currentSQLTimeInMSecs +
                " WHERE createdAt IS NULL;" +
                " END; ");
        //Log.d("INFO", query.toString());
        db.execSQL(query.toString());
    }

//    public static void updateBirdingLocationLastUsedOnSubmissionInsert(DatabaseWrapper db) {
//        StringBuilder query = new StringBuilder();
//        query.append("CREATE TRIGGER IF NOT EXISTS UpdateBirdingLocationLastUsedOnSubmissionInsert" +
//                " AFTER INSERT ON Submission BEGIN UPDATE BirdingLocation" +
//                " SET lastUsed = MAX(NEW.startTime,CAST(COALESCE(lastUsed,0) AS int)) WHERE _ID = NEW._locationID; END;");
//        //Log.d("INFO", query.toString());
//        db.execSQL(query.toString());
//    }
//
//    public static void updateBirdingLocationLastUsedOnSubmissionUpdate(DatabaseWrapper db) {
//        StringBuilder query = new StringBuilder();
//        query.append("CREATE TRIGGER IF NOT EXISTS UpdateBirdingLocationLastUsedOnSubmissionUpdate" +
//                " AFTER UPDATE ON Submission BEGIN UPDATE BirdingLocation" +
//                " SET lastUsed = MAX(NEW.startTime,CAST(COALESCE(lastUsed,0) AS int)) WHERE _ID = NEW._locationID; END;");
//        //Log.d("INFO", query.toString());
//        db.execSQL(query.toString());
//    }

    /**
     * Get a list of the classes that inherit from BaseModel. This requires that the the class name
     * is the same as the table name and that the class is in the models package.
     *
     * @return List of class objects extending BaseModel
     */
    public static List<Class<? extends EBirdModel>> getEBirdModelClassList() {

        List<Class<? extends EBirdModel>> eBirdModelClassList = null;

        if (eBirdModelClassList == null) {
            eBirdModelClassList = new ArrayList<>();
            for (String tableName : getDatabaseTableNameList()) {
                try {
                    String className = "org.spawny.dbflowtest." + tableName;
                    Class c = Class.forName(className);
                    if (EBirdModel.class.isAssignableFrom(c) && !tableName.startsWith("Frequency")) {
                        eBirdModelClassList.add(c);
                    }
                } catch (ClassNotFoundException e) {
                    // No need to do anything. If the class isn't found it is a database table
                    // that has no corresponding model class.
                }
            }
        }
        return eBirdModelClassList;
    }

    /**
     * Get a list of the non-android specific tables in the database.
     *
     * @return List of table names.
     */
    public static List<String> getDatabaseTableNameList() {
        List<String> databaseTableNameList = null;
        if (databaseTableNameList == null) {
            databaseTableNameList = new ArrayList<>();
            DatabaseWrapper sqlListDatabase = MyDatabase.getDatabase();
            Cursor cursor = sqlListDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' "
                    + "AND name <> 'android_metadata' AND name <> 'sqlite_sequence';", new String[0]);
            int nameColumnIndex = cursor.getColumnIndex("name");
            while (cursor.moveToNext()) {
                databaseTableNameList.add(cursor.getString(nameColumnIndex));
            }
            cursor.close();
        }
        return databaseTableNameList;
    }

    /**
     * Finds the current timestamp as known to the database. Accuracy is limited to seconds.
     *
     * @return Current timestamp of the DB with second accuracy.
     */
    public static Date currentTimestamp() {
        Date beforeUpdateTimestamp;
        String timeSelectQuery = "SELECT CAST(strftime('%s','now') AS INTEGER) * 1000 AS databaseTimestamp";
        final Cursor cursor = MyDatabase.getDatabase().rawQuery(timeSelectQuery, new String[0]);
        if (cursor.moveToFirst() && !cursor.isAfterLast()) {
            beforeUpdateTimestamp = new Date(cursor.getLong(0));
        } else {
            throw new IllegalStateException("Successful DB timestamp required");
        }
        cursor.close();
        return beforeUpdateTimestamp;
    }

    public static void createIndices(DatabaseWrapper db) {
        db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS LocLocIdIndex ON Loc (locId)");
    }

    public static List<String> columnsForModelClass(Class c, boolean includeEBirdModelColumns) {
        Class tableClass = getTableClass(c);
        Log.e("INFO", "++++++++ " + tableClass.getSimpleName());
        Field[] fields = tableClass.getFields();
        ArrayList<String> columnList = new ArrayList<>();
        for (Field field : fields) {
            //Log.d("INFO", "++++++++ " + field.getName());
            if (field.getType() == Property.class) {
                //String fieldValue = (String) field.get(tableClass);
                //String fieldValue = (String) field.get(tableClass);
                switch (field.getName()) {
                    case "TABLE_NAME":
                        // Skip table name
                        break;
                    case "_ID":
                    case "archived":
                    case "createdAt":
                    case "updatedAt":
                        if (includeEBirdModelColumns) {
                            columnList.add(field.getName());
                        }
                        break;
                    default:
                        columnList.add(field.getName());
                        break;
                }
            }
        }
        return columnList;
    }

    /**
     * Get the table class for a given model class.
     *
     * @param c Model class.
     * @return Table class for the model, null if not found.
     */
    public static Class getTableClass(Class c) {
        String className = c.getName();
        String tableClassName = className + "$Table";
        Class tableClass = null;
        try {
            tableClass = Class.forName(tableClassName);
        } catch (ClassNotFoundException e) {
            // Do nothing, null will be returned
        }
        return tableClass;
    }
}
