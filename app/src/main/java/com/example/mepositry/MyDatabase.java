package com.example.mepositry;



        import android.util.Log;

        import com.raizlabs.android.dbflow.annotation.Database;
        import com.raizlabs.android.dbflow.config.DatabaseDefinition;
        import com.raizlabs.android.dbflow.config.FlowManager;
        import com.raizlabs.android.dbflow.structure.BaseModel;
        import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by iaindownie on 23/06/2016.
 */
@Database(name = MyDatabase.NAME, version = MyDatabase.VERSION, generatedClassSeparator = "$")
public class MyDatabase {

    public static final String NAME = "MyDatabase"; // we will add the .db extension
    public static final int VERSION = 1;
    private static MyDatabase myDatabase;

    private MyDatabase() {
        super();
        createTriggers(getDatabase());
        DatabaseUtilities.createIndices(getDatabase());
    }

    public static void initializeDatabase() {
        if (myDatabase == null) {
            myDatabase = new MyDatabase();
        } else {
            throw new IllegalStateException("Database already initialized");
        }
    }

    /**
     * Returns the database instance, creating it if necessary
     *
     * @return Instance of the database
     */
    public static MyDatabase getInstance() {
        if (myDatabase == null) {
            initializeDatabase();
        }
        return myDatabase;
    }

    public static DatabaseWrapper getDatabase() {
        return FlowManager.getDatabase(NAME).getWritableDatabase();
    }

    public static DatabaseDefinition getDbDefinition() {
        return FlowManager.getDatabase(MyDatabase.NAME);
    }

    private void createTriggers(DatabaseWrapper db) {

        //db.execSQL("PRAGMA recursive_triggers = OFF;");

        try {
            for (Class<? extends BaseModel> modelClass : DatabaseUtilities.getEBirdModelClassList()) {
                DatabaseUtilities.createdTimestampTrigger(db, modelClass);
                DatabaseUtilities.updatedTimestampTrigger(db, modelClass);

            }
        } catch (Exception e) {
            Log.e("ERROR", "Failed to create triggers");
        }
    }


}