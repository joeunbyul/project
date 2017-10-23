package com.nadoo.tacademy.eunbyul_nanu;

/**
 * Created by Kyoonho on 2017-05-26.
 */

public class BoardItem {
    String image,reply_img;
    String title ,recommand_num,contents,board_num, nickname,reply_contents,reply_nickname,reply_num;

    public String getImage(){
        return image;
    }
    public String getTitle(){
        return title;
    }
    public String getContents(){return contents;}
    public BoardItem(){}
    public BoardItem(String board_num, String title, String image, String contents, String reply_num, String recommand_num, String nickname, String reply_img, String reply_contents, String reply_nickname){
        this.title = title;
        this.image = image;
        this.contents = contents;
        this.reply_num = reply_num;
        this.recommand_num = recommand_num;
        this.nickname = nickname;
        this.board_num = board_num;
        this.reply_img = reply_img;
        this.reply_nickname = reply_nickname;
        this.reply_contents = reply_contents;
    }
}
