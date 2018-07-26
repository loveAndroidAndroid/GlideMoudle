package com.pa.glide.pahglideapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * sp工具类
 */
public class SharedPreUtils {

    public static SharedPreferences sp;

    public static void getSharedPreference(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences("Pah", Context.MODE_PRIVATE);
        }
    }

    /**
     * boolean操作
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        getSharedPreference(context);
        boolean aBoolean = sp.getBoolean(key, defValue);
        return aBoolean;
    }

    public static void putBoolean(Context context, String key, boolean defValue) {
        getSharedPreference(context);
        sp.edit().putBoolean(key, defValue).commit();
    }

    /**
     * int操作
     */
    public static int getInt(Context context, String key, int defValue) {
        getSharedPreference(context);
        int result = sp.getInt(key, defValue);
        return result;
    }

    public static void putInt(Context context, String key, int defValue) {
        getSharedPreference(context);
        sp.edit().putInt(key, defValue).commit();
    }

    /**
     * 字符串操作
     */
    public static String getString(Context context, String key, String defValue) {
        getSharedPreference(context);
        String result = sp.getString(key, defValue);
        return result;
    }

    public static void putString(Context context, String key, String defValue) {
        getSharedPreference(context);
        sp.edit().putString(key, defValue).commit();
    }

    /**
     * 集合操作
     */
    public static Set<String> getStringSet(Context context, String key, Set<String> set) {
        getSharedPreference(context);
        Set<String> result = sp.getStringSet(key, set);
        return result;
    }

    public static void putStringSet(Context context, String key, Set<String> set) {
        getSharedPreference(context);
        sp.edit().putStringSet(key, set).commit();
    }

    /**
     * 保存long值
     */
    public static void putLong(Context context, String key, long value) {
        getSharedPreference(context);
        sp.edit().putLong(key, value).commit();
    }

    /**
     * 获取long值
     */
    public static long getLong(Context context, String key, long defValue) {
        getSharedPreference(context);
        return sp.getLong(key, defValue);
    }

    /**
     * 用于保存集合
     */
    public static <T> boolean putListData(String key, List<T> list) {
        boolean result;
        SharedPreferences.Editor editor = sp.edit();
        JsonArray array = new JsonArray();
        if (list == null || list.size() == 0) {
            editor.putString(key, array.toString());
            editor.apply();
            return true;
        }
        String type = list.get(0).getClass().getSimpleName();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            editor.putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 获取保存的List
     */
    public static <T> List<T> getListData(String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = sp.getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }
}
