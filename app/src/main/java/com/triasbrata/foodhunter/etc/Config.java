package com.triasbrata.foodhunter.etc;

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
//        public static String baseUrl = "http://192.168.42.79:3000/";
        public static String baseUrl = "http://10.0.2.2:3000/";
        public static String userLike = "food?fav";
        private static String storeDetail = "stores/:id?_embed=food";
        public static String storeAll = "store";
        public static String foodAll = "foods/:id?_expand=store";
        private static String likeFood = "like";

        public static String makeUrl (String s){
            return  baseUrl +s;
        }

        public static String store_detail(String idItem) {
            return makeUrl(storeDetail.replace(":id",idItem));
        }

        public static String store_all() {
            return  makeUrl(storeAll);
        }

        public static String food_detail(String idItem) {

            return makeUrl(foodAll.replace(":id",idItem));
        }

        public static String like_food(String idItem) {
            return  makeUrl(likeFood +"/"+idItem);
        }

        public static String food_all() {
            return makeUrl(foodAll.replace("/:id",""));
        }
    }
}
