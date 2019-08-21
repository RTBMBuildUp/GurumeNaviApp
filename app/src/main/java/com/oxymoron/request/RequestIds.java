package com.oxymoron.request;

public enum RequestIds {
    key_id("keyid"), restaurant_id("restaurant_id"), latitude("latitude"),
    longitude("longitude"), range("range"), hit_per_page("hit_per_page"),
    offset_page("offset_page");
//    key_id {
//        @Override
//        public String toString() {
//            return "keyid";
//        }
//    }, restaurant_id {
//        @Override
//        public String toString() {
//            return "id";
//        }
//    }, latitude {
//    }, longitude {
//    }, range {
//    }, hit_per_page {
//    }, offset_page {
//    },
//    ;

    private String id;
    RequestIds(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}
