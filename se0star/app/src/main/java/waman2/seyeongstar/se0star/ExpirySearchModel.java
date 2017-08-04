package waman2.seyeongstar.se0star;

public class ExpirySearchModel {

    private String modify_list_name;
    private String modify_list_num;
    private String unit;
    private int diff;
    private String strfinalday;

    public ExpirySearchModel(String modify_list_name, String modify_list_num, String unit, int diff, String strfinalday) {
        this.modify_list_name = modify_list_name;
        this.modify_list_num = modify_list_num;
        this.unit = unit;
        this.diff = diff;
        this.strfinalday = strfinalday;
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

    public int getdiff() { return diff; }

    public String getStrfinalday() {return strfinalday; }

}