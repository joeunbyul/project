package waman2.seyeongstar.se0star;

/**
 * Created by byul on 2016-08-11.
 */
public class ListModel {

    private String modify_list_kind;
    private String modify_list_name;
    private String modify_list_num;
    private String unit;
    private int list_index;
    private String modify_list_startday;
    private String modify_list_finalday;

    public ListModel(String modify_list_kind, String modify_list_name, String modify_list_num, String modify_list_startday, String modify_list_finalday, String unit, int list_index) {
        this.modify_list_kind = modify_list_kind;
        this.modify_list_name = modify_list_name;
        this.modify_list_num = modify_list_num;
        this.modify_list_startday = modify_list_startday;
        this.modify_list_finalday = modify_list_finalday;
        this.unit = unit;
        this.list_index = list_index;
    }

    public String getModify_list_kind() {
        return modify_list_kind;
    }

    public String getModify_list_name() {
        return modify_list_name;
    }

    public String getModify_list_num() {
        return modify_list_num;
    }

    public String getunit() {
        return unit;
    }

    public int getlist_index() { return list_index; }

    public String getModify_list_startday() { return modify_list_startday; }

    public String getModify_list_finalday() {
        return modify_list_finalday;
    }
}