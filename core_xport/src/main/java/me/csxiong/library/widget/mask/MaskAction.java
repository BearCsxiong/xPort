package me.csxiong.library.widget.mask;

/**
 * Created by csxiong on 2018/10/22.
 */

public class MaskAction {

    private int id;

    private String tag;

    public MaskAction(int id, String tag) {
        this.id = id;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
