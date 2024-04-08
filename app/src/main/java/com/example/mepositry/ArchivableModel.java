package com.example.mepositry;




        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.raizlabs.android.dbflow.annotation.Column;

/**
 * @author @iaindownie on 14/09/2021.
 */

public abstract class ArchivableModel extends EBirdModel {

    @JsonIgnore
    @Column
    public boolean archived;

}
