package com.minwooseonno.hitthetimetoandrod;

import android.media.Image;

public class RecyclerItem {
    private String Rank;
    private String name;
    private String record;
    public void setRank(String _rank) {
        Rank = _rank ;
    }
    public void setName(String _title) {
        name = _title ;
    }
    public void setRecord(String _record) {
        record = _record ;
    }

    public String getRank() {
        return this.Rank;
    }
    public String getName() {
        return this.name;
    }
    public String  getRecord() {
        return this.record;
    }
}
