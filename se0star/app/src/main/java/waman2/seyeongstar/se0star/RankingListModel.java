package waman2.seyeongstar.se0star;

public class RankingListModel {
    private String Ranking_list_kind;
    private String Ranking_list_name;
    private String Ranking_list_num;
    private String Ranking_unit;
    private String Ranking_list_finalday;
    private String difference;

    public RankingListModel(String Ranking_list_kind, String Ranking_list_name, String Ranking_list_num, String Ranking_unit, String Ranking_list_finalday, String difference) {
        this.Ranking_list_kind = Ranking_list_kind;
        this.Ranking_list_name = Ranking_list_name;
        this.Ranking_list_num = Ranking_list_num;
        this.Ranking_unit = Ranking_unit;
        this.Ranking_list_finalday = Ranking_list_finalday;
        this.difference = difference;
    }

    public String getRanking_list_kind() {
        return Ranking_list_kind;
    }

    public String getRanking_list_name() {
        return Ranking_list_name;
    }

    public String getRanking_list_num() {
        return Ranking_list_num;
    }

    public String getRanking_unit() {
        return Ranking_unit;
    }

    public String getRanking_list_finalday() {
        return Ranking_list_finalday;
    }

    public String getDifference() { return difference;}
}
