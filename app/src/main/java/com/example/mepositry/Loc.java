package com.example.mepositry;




        import com.raizlabs.android.dbflow.annotation.Column;
        import com.raizlabs.android.dbflow.annotation.Index;
        import com.raizlabs.android.dbflow.annotation.IndexGroup;
        import com.raizlabs.android.dbflow.annotation.PrimaryKey;
        import com.raizlabs.android.dbflow.annotation.Table;
        import com.raizlabs.android.dbflow.annotation.Unique;

/**
 * Created by iaindownie on 23/06/2016.
 */

@Table(database = MyDatabase.class, name = "Loc")
public class Loc extends ArchivableModel {

    @Column
    public String locId;

    @Column
    public String userId;

    public Loc() {
    }

    public Loc(int id, String locId, String userId) {
        this.locId = locId;
        this.userId = userId;
    }

    public String getLocId() {
        return locId;
    }

    public String getUserId() {
        return userId;
    }

    public void setLocId(String locId) {
        this.locId = locId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}