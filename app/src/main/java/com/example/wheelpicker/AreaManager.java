package com.example.wheelpicker;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class AreaManager {
    private static final String PROVINCE_FILE = "province.json";
    private static final String CITY_FILE = "city.json";
    private static AreaManager instance;

    //1.static方法省去New確認是否有物件實體,沒有的話去物件實體初始化
    public static AreaManager getInstance(Context context) {
        if (instance == null) instance = new AreaManager(context);
        return instance;
    }

    /***
     * 宣告
     * list of province 資料結構
     *陣列,物件 [{}] => List<KeyValuePair<String, String>>
     *解Provinces[{ key":"110000","value": "北京" }, { "key":"120000","value": "天津"}
     */
    private List<KeyValuePair<String, String>> provinces;

    /***
     * 宣告
     * Key: province_id, value: list of cities
     * //物件,陣列,物件 =>  HashMap<1.String(key == 11000),2.value List<KeyValuePair<key:11000, value:北京市>>
     * //解city{"110000":[{"key":"110000","value": "北京市" }],"120000": [{"key":"120000","value": "天津市" }]}
     */
    private HashMap<String, List<KeyValuePair<String, String>>> provinceCityHashMap;


    //3.從assests讀取檔案並且解析Json到對應的類別
    private AreaManager(Context context) {
        try {
            Gson gson = new Gson();
            InputStreamReader sr = new InputStreamReader(context.getAssets().open(PROVINCE_FILE));
            provinces = gson.fromJson(sr, new TypeToken<List<KeyValuePair<String, String>>>() {
            }.getType());
            sr.close();

            sr = new InputStreamReader(context.getAssets().open(CITY_FILE));
            provinceCityHashMap = gson.fromJson(sr, new TypeToken<HashMap<String, List<KeyValuePair<String, String>>>>() {
            }.getType());
            sr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //取得Provinces資料
    public List<KeyValuePair<String, String>> getProvinces() {
        return provinces;
    }

    //取得city資料
    public HashMap<String, List<KeyValuePair<String, String>>> getProvinceCityHashMap() {
        return provinceCityHashMap;
    }

    //用KeyValuePair 代替HashMap好處是不用再另外開類別Model,直接帶入<T,S> == <Key類別,跟Value類別>
    public static class KeyValuePair<T, S> {
        public T key;
        public S value;

        public KeyValuePair() {
        }

        public KeyValuePair(T key, S value) {
            this.key = key;
            this.value = value;
        }

        @NonNull
        @Override
        public String toString() {
            return value instanceof String ? (String) value : value.toString();
        }
    }
}


