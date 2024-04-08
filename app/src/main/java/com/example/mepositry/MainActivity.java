package com.example.mepositry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private DatabaseDefinition dbDef;
    private List<Loc> locs2 = new ArrayList<>();
    private List<Loc> locs3 = new ArrayList<>();
    private Button db_add, db_query, db_delete, db_delete_all, db_update;
    private LanguageRecycleAdapter languageRecycleAdapter;
    private RecyclerView rvLanguage;
    private EditText et_id, et_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbDef = MyDatabase.getDbDefinition();
        db_add = findViewById(R.id.db_add);
        et_id = findViewById(R.id.et_id);
        et_user = findViewById(R.id.et_user);
        db_query = findViewById(R.id.db_query);
        db_delete = findViewById(R.id.db_delete);
        db_update = findViewById(R.id.db_update);
        db_delete_all = findViewById(R.id.db_delete_all);
        rvLanguage = findViewById(R.id.rvLanguage);
        db_add.setOnClickListener(this);
        db_query.setOnClickListener(this);
        db_delete.setOnClickListener(this);
        db_update.setOnClickListener(this);
        db_delete_all.setOnClickListener(this);

        RecyclerView.LayoutManager layoutManager = new
                LinearLayoutManager(MainActivity.this);
        rvLanguage.setLayoutManager(layoutManager);
        languageRecycleAdapter = new LanguageRecycleAdapter(locs3);
        rvLanguage.setAdapter(languageRecycleAdapter);
    }

    private void insertSomeNewRecords(String id, String user) {
        int random = (int) (Math.random() * 100000);
        Log.e("INFO", "LOC00" + random + id + user);
        ContentValues contentValues = new ContentValues();
        contentValues.put(Loc$Table.locId.toString(), id);
        contentValues.put(Loc$Table.userId.toString(), user);
        MyDatabase
                .getDatabase()
                .insertWithOnConflict("Loc", null, contentValues, SQLiteDatabase.CONFLICT_ROLLBACK);
    }


    private void querySomeNewRecords() {
        dbDef.beginTransactionAsync(new QueryTransaction.Builder<>(
                SQLite.select().from(Loc.class))
                .queryResult(new QueryTransaction.QueryResultCallback<Loc>() {
                    @Override
                    public void onQueryResult(@NonNull QueryTransaction<Loc> transaction, @NonNull CursorResult<Loc> tResult) {
                        locs2 = tResult.toListClose();
                        Log.e("INFO", "##### Sub: " + transaction + ": " + tResult + locs2);
                        Message msg = new Message();
                        msg.what = 100;
                        msg.obj = locs2;
                        mHandler.sendMessage(msg);

                    }
                }).build()).build().execute();
    }


    private void deleteSomeNewRecords(String id) {
        SQLite.delete(Loc.class)
                .where(Loc$Table.locId.eq(id))
                .query();
    }

    private void deleteAllRecords() {
        Delete.table(Loc.class);
    }

    private void updateRecords(String id, String user) {
        SQLite.update(Loc.class)
                .set(Loc$Table.userId.eq(user))
                .where(Loc$Table.locId.eq(id))
                .execute();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.db_add:
                String idString = et_id.getText().toString();
                String userString = et_user.getText().toString();
                Log.e("INFO", "LOC00" + idString + userString);
                insertSomeNewRecords(idString, userString);
                break;
            case R.id.db_query:
                querySomeNewRecords();
                break;
            case R.id.db_delete:
                String idString2 = et_id.getText().toString();
                deleteSomeNewRecords(idString2);
                break;
            case R.id.db_delete_all:
                deleteAllRecords();
                break;
            case R.id.db_update:
                String idString3 = et_id.getText().toString();
                String userString3 = et_user.getText().toString();
                updateRecords(idString3, userString3);
                break;
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 100) {
                if (locs3 != null) {
                    locs3.clear();
                }
                locs3 = (List<Loc>) message.obj;
                Log.e("INFO", "##### Sub: " + ": " + locs3);
                languageRecycleAdapter.setData(locs3);
                languageRecycleAdapter.notifyDataSetChanged();
            }
            return false;
        }
    });
}
