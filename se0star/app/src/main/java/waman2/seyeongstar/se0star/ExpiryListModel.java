package waman2.seyeongstar.se0star;

public class ExpiryListModel {
    private String Expiry_index;
    private String Expiry_kind;
    private String Expiry_day;

    public ExpiryListModel(String Expiry_index, String Expiry_kind, String Expiry_day) {
        this.Expiry_index = Expiry_index;
        this.Expiry_kind = Expiry_kind;
        this.Expiry_day = Expiry_day;
    }

    public String getExpiry_index() {
        return Expiry_index;
    }

    public String getExpiry_kind() {
        return Expiry_kind;
    }

    public String getExpiry_day() {
        return Expiry_day;
    }
}
