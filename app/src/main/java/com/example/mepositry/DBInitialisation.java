package com.example.mepositry;





        import android.app.Application;

        import com.raizlabs.android.dbflow.config.FlowConfig;
        import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by iaindownie on 23/06/2016.
 */
public class DBInitialisation extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(new FlowConfig.Builder(this).build());
        MyDatabase.initializeDatabase();
    }
}