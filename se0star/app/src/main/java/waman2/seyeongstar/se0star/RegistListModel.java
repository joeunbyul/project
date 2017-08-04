package waman2.seyeongstar.se0star;

public class RegistListModel {

    private String regist_list_kind;
    private String regist_list_name;
    private String regist_list_num;
    private String regist_unit;
    private String regist_list_startday;
    private String regist_list_finalday;

    public RegistListModel(String regist_list_kind, String regist_list_name, String regist_list_num, String regist_unit, String regist_list_startday, String regist_list_finalday) {
        this.regist_list_kind = regist_list_kind;
        this.regist_list_name = regist_list_name;
        this.regist_list_num = regist_list_num;
        this.regist_list_startday = regist_list_startday;
        this.regist_list_finalday = regist_list_finalday;
        this.regist_unit = regist_unit;
    }

    public String getRegist_list_kind() {
        return regist_list_kind;
    }

    public String getRegist_list_name() {
        return regist_list_name;
    }

    public String getRegist_list_num() {
        return regist_list_num;
    }

    public String getRegist_unit() {
        return regist_unit;
    }

    public String getRegist_list_startday() { return regist_list_startday; }

    public String getRegist_list_finalday() {
        return regist_list_finalday;
    }

}