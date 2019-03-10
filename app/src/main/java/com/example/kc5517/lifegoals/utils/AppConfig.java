package com.example.kc5517.lifegoals.utils;

public class AppConfig {

    //webserver
    //public static String URL = "http://wildswap.com/API/index.php";

    //localhost
    //public static String URL = "http://192.168.0.14:81/WildScotland_API/";

    private int goalMax = 10;

    public int getGoalMax(){
        return this.goalMax;
    }

    public void setGoalMax(int goalMax){
        this.goalMax = goalMax;
    }

    public static String firebase = "https://fcm.googleapis.com/fcm/send";

    public static String serverKey = "AAAA78qREAk:APA91bHbJULtohupXtbXF6_IcbneFo26aiaGyI-2bJE0E44C7rUhqRni0AUw5m-My-8CJXAbj2Qarre4sv5J9G6Erefs2cizunKZKvijJ5GLY9FDzcC0lmv9BqQaLFFCfCoygJ03W_me";

    public static String myToken = "ed_pZSkR4HA:APA91bHSwBls2GYxx0RPNC6nkWvV5v-6o8R4iew8EC9d0zOxRxxXTQbRGeCVdW3xbeMyNXCmI8hkerCaloAR3HKc_ufsuU9JSs5nSO5PbsrSNoWOHjLohiZscd49gENcfIkNpLEg-K5S";

}
