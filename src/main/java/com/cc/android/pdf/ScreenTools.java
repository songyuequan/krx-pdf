package com.cc.android.pdf;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
/**
 * Description: ScreenTools
 * Author: danhantao
 * Update: danhantao(2014-11-17 11:12)
 * Email: danhantao@yeah.net
 */


public class ScreenTools {
  /*
   * 获取屏幕高度
   */
  public static int getScreenHeight(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    if (Build.VERSION.SDK_INT < 17) {
      ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    } else {
      ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
    }
    int height = dm.heightPixels;
    return height;
  }

  /*
   * 获取屏幕宽度
   */
  public static int getScreenWidth(Context context) {
    DisplayMetrics dm = new DisplayMetrics();
    if (Build.VERSION.SDK_INT < 17) {
      //NEXUS 5 DisplayMetrics{density=3.0, width=1080, height=1776, scaledDensity=3.0, xdpi=442.451, ydpi=443.345}
      ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
    } else {
      //NEXUS 5 DisplayMetrics{density=3.0, width=1080, height=1920, scaledDensity=3.0, xdpi=442.451, ydpi=443.345}
      ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(dm);
    }
    int width = dm.widthPixels;
    return width;
  }


}