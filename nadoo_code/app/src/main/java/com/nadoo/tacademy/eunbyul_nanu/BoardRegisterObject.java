package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by Kyoonho on 2017-06-14.
 */

public class BoardRegisterObject {
    String board_boardID;
    String board_article;
    String board_userID;
    String board_category;
    String board_title;
    int board_anonymity;

    public BoardRegisterObject(){}
    public BoardRegisterObject( String board_boardID,
            String board_article,
            String board_userID,
            String board_category,
            String board_title,
            int board_anonymity){
        this.board_boardID = board_boardID;
        this.board_article = board_article;
        this.board_userID = board_userID;
        this.board_category=board_category;
        this.board_title = board_title;
        this.board_anonymity=board_anonymity;
    }
}
