package com.example.mepositry;




        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.raizlabs.android.dbflow.annotation.Column;
        import com.raizlabs.android.dbflow.annotation.PrimaryKey;
        import com.raizlabs.android.dbflow.structure.BaseModel;

        import java.util.Date;

/**
 * @author @iaindownie on 14/09/2021.
 */

public abstract class EBirdModel extends BaseModel {

    @JsonIgnore
    @Column
    @PrimaryKey(autoincrement = true)
    public Long _ID;

    @JsonIgnore
    @Column
    public Date createdAt;

    @JsonIgnore
    @Column
    public Date updatedAt;
}