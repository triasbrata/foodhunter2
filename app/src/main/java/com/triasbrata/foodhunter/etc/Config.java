package com.triasbrata.foodhunter.etc;

import android.util.Log;

/**
 * Created by triasbrata on 07/07/16.
 */
public class Config {
    public static String base_url = URL.baseUrl;
    public static class TagBundle{
        public static String Tag = TagBundle.class.getSimpleName();
        public static String storeModel = Tag+"STORE_MODEL";
    }

    public static class URL {
        private static final String TAG = Config.URL.class.getSimpleName();
        //        public static String baseUrl = "http://192.168.42.79:3000/";
        private static String host =  "http://10.0.2.2";
        private static String hostLocal =  "http://localhost";
        private static String port =  "3000";
        private static String baseUrl = host+":"+port+"/";
        private static String userLike = "food?fav";
        private static String store = "stores/:id?_embed=foods";
        private static String foods = "foods/:id?_expand=store";
        private static String likeFood = "like";

        public static String makeUrl (String s){
            String o = baseUrl +s;
            Log.d(TAG, "makeUrl: "+o.replace(host,hostLocal));
            return  o;

        }

        public static String store_detail(String idItem) {
            return makeUrl(store.replace(":id",idItem));
        }

        public static String store_all() {
            return  makeUrl(store.replace("/:id",""));
        }

        public static String food_detail(String idItem) {

            return makeUrl(foods.replace(":id",idItem));
        }

        public static String like_food(String idItem) {
            return  makeUrl(likeFood +"/"+idItem);
        }

        public static String food_all() {
            return makeUrl(foods.replace("/:id",""));
        }

        public static String userLike() {
            return userLike;
        }
    }
}
