package com.triasbrata.foodhunter.etc;

/**
 * Created by triasbrata on 07/07/16.
 */
public class Config {
    public static String base_url = URL.base_url ;

    public static class URL {
        public static String base_url = "http://10.0.2.2:3000/";
        public static String user_like = "food?fav";
        public static String makeUrl (String s){
            return  base_url+s;
        }

    }
}
