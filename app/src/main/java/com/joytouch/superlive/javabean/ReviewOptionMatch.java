package com.joytouch.superlive.javabean;

/**
 * Created by lzx on 2016/5/9.
 * 联赛具体名称lei
 */
public class ReviewOptionMatch {
    private String parent_id;
    private String display_name;
    private String cat_id;
    private boolean isSelected;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
}
