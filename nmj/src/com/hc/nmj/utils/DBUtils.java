package com.hc.nmj.utils;

import java.util.ArrayList;
import java.util.List;

import org.kymjs.kjframe.KJDB;
import org.kymjs.kjframe.utils.PreferenceHelper;

import com.hc.nmj.bean.Clothes;
import com.hc.nmj.bean.User;
import com.hc.nmj.common.Constant;

public class DBUtils {
    private static KJDB kjdb;
    List<Clothes> ls = new ArrayList<Clothes>();

    public static KJDB getInstance() {
//        return kjdb = KJDB.create(LocationApplication.context, PreferenceHelper.readString(LocationApplication.context, "user", "username") + ".db", true);
        return kjdb = KJDB.create(LocationApplication.context,  "user.db", true);
    }
    
    public static void createUsrData(String username, String password) {
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        kjdb.save(user);
    }
    
    public static boolean checkUserExist(String username) {
        List<User> list = kjdb.findAll(User.class);
        int len = list.size();
        for (int i = 0; i < len; i++) {
            User user = list.get(i);
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static void createTestData() {
        getInstance();
        Clothes clothes1 = new Clothes();
        clothes1.setUrl("assets://images" + 1 + "/" + 1 + ".jpg");
        clothes1.setTitle("title");
        clothes1.setPrice("￥" + "0.01");
        clothes1.setAmount(98);

        Clothes clothes2 = new Clothes();
        clothes2.setUrl("assets://images" + 2 + "/" + 1 + ".jpg");
        clothes2.setTitle("title");
        clothes2.setPrice("￥" + "128");
        clothes2.setAmount(100);

        Clothes clothes3 = new Clothes();
        clothes3.setUrl("assets://images" + 2 + "/" + 2 + ".jpg");
        clothes3.setTitle("title");
        clothes3.setPrice("￥" + "138");
        clothes3.setAmount(44);

        Clothes clothes4 = new Clothes();
        clothes4.setUrl("assets://images" + 2 + "/" + 3 + ".jpg");
        clothes4.setTitle("title");
        clothes4.setPrice("￥" + "149");
        clothes4.setAmount(455);

        Clothes clothes5 = new Clothes();
        clothes5.setUrl("assets://images" + 2 + "/" + 4 + ".jpg");
        clothes5.setTitle("title");
        clothes5.setPrice("￥" + "129");
        clothes5.setAmount(99);
        
        Clothes clothes6 = new Clothes();
        clothes6.setUrl("assets://images" + 2 + "/" + 5 + ".jpg");
        clothes6.setTitle("title");
        clothes6.setPrice("￥" + "136");
        clothes6.setAmount(160);
        
        Clothes clothes7 = new Clothes();
        clothes7.setUrl("assets://images" + 2 + "/" + 6 + ".jpg");
        clothes7.setTitle("title");
        clothes7.setPrice("￥" + "148");
        clothes7.setAmount(60);
        
        Clothes clothes8 = new Clothes();
        clothes8.setUrl("assets://images" + 2 + "/" + 7 + ".jpg");
        clothes8.setTitle("title");
        clothes8.setPrice("￥" + "148");
        clothes8.setAmount(10);

        kjdb.save(clothes1);
        kjdb.save(clothes2);
        kjdb.save(clothes3);
        kjdb.save(clothes4);
        kjdb.save(clothes5);
        kjdb.save(clothes6);
        kjdb.save(clothes7);
        kjdb.save(clothes8);
    }
    
    public static List<Clothes> queryAll() {
        getInstance();
        return kjdb.findAll(Clothes.class);
    }
    
    public static void delete() {
        int len = Constant.shopCarList.size();
        for (int i = 0; i < len; i++) {
            kjdb.delete(Constant.shopCarList.get(i));
        }
    }

    public static void update() {
        int len = Constant.shopCarList.size();
        for (int i = 0; i < len; i++) {
            Clothes clothes = Constant.shopCarList.get(i);
            clothes.setAmount(clothes.getAmount() - 1);
            kjdb.update(clothes);
        }
        Constant.shopCarList.clear();
    }

    public static boolean checkLogin(String username, String password) {
        List<User> list = kjdb.findAll(User.class);
        int len = list.size();
        for (int i = 0; i < len; i++) {
            User user = list.get(i);
            if (user.getUsername().equalsIgnoreCase(username) && user.getPassword().equalsIgnoreCase(password)) {
                return true;
            }
        }
        return false;
    }

}
