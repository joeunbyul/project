/*
 *   Http 요청 상수값
 */
package com.nadoo.tacademy.eunbyul_nanu;

public class NetworkDefineConstant {
    public static final String HOST_URL = "http://34.226.128.28:3000";
    public static final String REQUEST_GET = "GET";
    public static final String REQUEST_POST = "POST";


    //요청 URL path
    public static String SERVER_URL_NADO_WANTITEM_LIST;
    public static String SERVER_URL_NADO_WANTITEM_INSERT;
    public static String SERVER_URL_NADOO_USERWANTITEM_LIST;
    public static String SERVER_URL_NADO_ITEM_LIST;
    public static String SERVER_URL_NADOO_USERITEM_LIST;
    public static String SERVER_URL_NADO_ITEM_INSERT;
    public static String SERVER_URL_NADOO_JOINEMAILCHECK;
    public static String SERVER_URL_NADOO_JOINNICKNAMECHECK;
    public static String SERVER_URL_NADOO_JOIN;
    public static String SERVER_URL_NADOO_LOGIN;
    public static String SERVER_URL_NADOO_USER;

    public static String SERVER_URL_NADOO_OTHERUSER;
    public static String SERVER_URL_NADOO_OTHERUSER_FOLLOW;
    public static String SERVER_URL_NADOO_OTHERUSER_ITEM;
    public static String SERVER_URL_NADOO_OTHERUSER_NEED;
    public static String SERVER_URL_NADOO_OTHERUSER_FOLLOWER;

    public static String SERVER_URL_NADOO_STATUS;

    public static String SERVER_URL_NADO_BOARD_LIST;
    public static String SERVER_URL_NADO_BOARD_DETAIL;
    public static String SERVER_URL_NADO_REGISTER_BOARD;

    static{
        SERVER_URL_NADO_WANTITEM_LIST =
                HOST_URL + "/need";
        SERVER_URL_NADOO_USERWANTITEM_LIST =
                HOST_URL + "/usergetneed";
        SERVER_URL_NADO_WANTITEM_INSERT =
                HOST_URL+ "/need";
        SERVER_URL_NADO_ITEM_LIST =
                HOST_URL + "/item";
        SERVER_URL_NADO_ITEM_INSERT =
                HOST_URL + "/item";
        SERVER_URL_NADOO_USERITEM_LIST =
                HOST_URL + "/usergetitem";
        SERVER_URL_NADOO_JOINEMAILCHECK =
                HOST_URL + "/joinidcheck";
        SERVER_URL_NADOO_JOINNICKNAMECHECK =
                HOST_URL + "/joinnickcheck";
        SERVER_URL_NADOO_JOIN =
                HOST_URL + "/join";
        SERVER_URL_NADOO_LOGIN =
                HOST_URL + "/login";
        SERVER_URL_NADOO_USER =
                HOST_URL + "/usergetid";

        SERVER_URL_NADOO_OTHERUSER =
                HOST_URL + "/usersearch";
        SERVER_URL_NADOO_OTHERUSER_FOLLOW =
                HOST_URL + "/following";
        SERVER_URL_NADOO_OTHERUSER_ITEM =
                HOST_URL + "/usersearchitem";
        SERVER_URL_NADOO_OTHERUSER_NEED =
                HOST_URL + "/usersearchneed";
        SERVER_URL_NADOO_OTHERUSER_FOLLOWER =
                HOST_URL + "/user";

        SERVER_URL_NADOO_STATUS =
                HOST_URL + "/item/changestatus";
        SERVER_URL_NADO_BOARD_LIST = HOST_URL + "/board?category=sub1&page=1";
        SERVER_URL_NADO_BOARD_DETAIL = HOST_URL + "/board/";
        SERVER_URL_NADO_REGISTER_BOARD = HOST_URL + "/board";
    }
}