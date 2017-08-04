package waman2.seyeongstar.se0star;

public class AddListModel {

    private String add_list_kind;
    private String add_list_name;
    private String add_list_num;
    private String add_unit;
    private String add_list_startday;
    private String add_list_finalday;

    public AddListModel(String add_list_kind, String add_list_name, String add_list_num, String add_unit, String add_list_startday, String add_list_finalday) {
        this.add_list_kind = add_list_kind;
        this.add_list_name = add_list_name;
        this.add_list_num = add_list_num;
        this.add_list_startday = add_list_startday;
        this.add_list_finalday = add_list_finalday;
        this.add_unit = add_unit;
    }

    public String getAdd_list_kind() {
        return add_list_kind;
    }

    public String getAdd_list_name() {
        return add_list_name;
    }

    public String getAdd_list_num() {
        return add_list_num;
    }

    public String getAdd_unit() {
        return add_unit;
    }

    public String getAdd_list_startday() { return add_list_startday; }

    public String getAdd_list_finalday() {
        return add_list_finalday;
    }
}