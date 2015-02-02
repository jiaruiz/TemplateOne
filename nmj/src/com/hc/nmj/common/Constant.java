package com.hc.nmj.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.nmj.bean.Clothes;


public class Constant {

   public static List<Clothes> shopCarList = new ArrayList<Clothes>();
   public static Map<Integer, Clothes> mSelectedMap = new HashMap<Integer, Clothes>();
   
   public static boolean buy;
   
   public static boolean login;
   
   public static boolean pay;
}
