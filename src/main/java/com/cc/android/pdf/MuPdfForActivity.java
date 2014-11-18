package com.cc.android.pdf;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.Inject;
import com.krxkid.android.pdf.R;

/**
 * Created by dpw on 7/10/14.
 */
public class MuPdfForActivity extends MuPDFActivity implements View.OnClickListener {
  public static final String AUTHORITY = "com.krxkid.android.provider.play";
  public static final String KEY_PLAY_DATA = "playData";
  public static final String INSERT = "/insert";

  @Inject
  private Bus bus;
  private WindowManager windowManager;
  private WindowManager.LayoutParams paramsBack;
  private WindowManager.LayoutParams paramsControllBar;
  private Button back;
  private View controllBar;
  private Handler handler=new Handler();

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    this.windowManager =
        (WindowManager) this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    this.back = new Button(this);
    this.back.setBackgroundResource(R.drawable.common_selector_back);
    this.back.setOnClickListener(this);
    this.controllBar = this.getLayoutInflater().inflate(R.layout.cc_pdf_controller, null);
    this.paramsBack = new WindowManager.LayoutParams();
    this.paramsBack.width = this.paramsBack.WRAP_CONTENT;
    this.paramsBack.height = this.paramsBack.WRAP_CONTENT;
    this.paramsBack.gravity = Gravity.LEFT | Gravity.TOP;
    this.paramsBack.type = WindowManager.LayoutParams.TYPE_PHONE;
    this.paramsBack.format = PixelFormat.RGBA_8888;
    this.paramsBack.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

    this.paramsControllBar = new WindowManager.LayoutParams();
    this.paramsControllBar.width = WindowManager.LayoutParams.WRAP_CONTENT;
    this.paramsControllBar.height = WindowManager.LayoutParams.WRAP_CONTENT;
    this.paramsControllBar.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    this.paramsControllBar.type = WindowManager.LayoutParams.TYPE_PHONE;
    this.paramsControllBar.format = PixelFormat.RGBA_8888;
    this.paramsControllBar.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
  }

  @Override
  protected void onResume() {
    super.onResume();
    this.windowManager.addView(this.back, this.paramsBack);
    this.windowManager.addView(this.controllBar, this.paramsControllBar);
    handler.postDelayed(new Runnable() {
      @Override public void run() {
        bus.sendLocal(Constant.ADDR_PLAYER, Json.createObject().set("zoomBy",2.5), null);
      }
    }, 100);
  }

  @Override
  protected void onPause() {
    super.onPause();
    this.windowManager.removeView(this.back);
    this.windowManager.removeView(this.controllBar);
  }

  @Override
  public void onClick(View v) {
    if (v == back) {
//      saveOnDatabases();
      saveData();
      this.finish();
      finishMuPdfCore();
    }
    if (v.getId() == R.id.bt_pdf_pre_page) {
      bus.sendLocal(Constant.ADDR_PLAYER,
          Json.createObject().set("page", Json.createObject().set("move", -1)), null);
    }
    if (v.getId() == R.id.bt_pdf_next_page) {
      bus.sendLocal(Constant.ADDR_PLAYER,
          Json.createObject().set("page", Json.createObject().set("move", 1)), null);
    }
    if (v.getId() == R.id.bt_pdf_max) {
      bus.sendLocal(Constant.ADDR_PLAYER, Json.createObject().set("zoomBy", 1.2), null);
    }
    if (v.getId() == R.id.bt_pdf_min) {
      bus.sendLocal(Constant.ADDR_PLAYER, Json.createObject().set("zoomBy", 0.8), null);
    }
  }

//  @Override
//  public void onBackPressed() {
//    saveOnDatabases();
//    finishMuPdfCore();
//    super.onBackPressed();
//  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
//      saveOnDatabases();
      saveData();
      finish();
      finishMuPdfCore();
    }
    return super.onKeyDown(keyCode, event);
  }

//  protected void saveOnDatabases() {
//    SharedPreferences usagePreferences =
//        getSharedPreferences(BaseActivity.USAGE_STATISTIC, Context.MODE_MULTI_PROCESS);
//    String fileName = usagePreferences.getString("tmpFileName", "");
//    long openTime = usagePreferences.getLong("tmpOpenTime", -1);
//    // 播放时间，小于5s，忽略
//    long lastTime = SystemClock.uptimeMillis() - usagePreferences.getLong("tmpSystemLast", -1);
//    if (lastTime > 5000 & !TextUtils.isEmpty(fileName)) {
//      // 将播放数据存储到数据库
//      DBOperator.addUserData(this, "T_PLAYER", "FILE_NAME", "OPEN_TIME", "LAST_TIME", Json
//          .createObject().set("FILE_NAME", fileName).set("OPEN_TIME", openTime).set("LAST_TIME",
//              lastTime));
//      if (bus.getReadyState() == State.OPEN) {
//        bus.sendLocal(Constant.ADDR_PLAYER_ANALYTICS_REQUEST, null, null);
//      }
//    }
//  }

  protected void saveData(){
    String fileName = sharedPreferences.getString("tmpFileName", "");
    long openTime = sharedPreferences.getLong("tmpOpenTime", -1);
    // 播放时间，小于5s，忽略
    long lastTime = SystemClock.uptimeMillis() - sharedPreferences.getLong("tmpSystemLast", -1);
    // 将数据插入
    if (lastTime > 5000 & !TextUtils.isEmpty(fileName)){
      System.out.println("-------------");
      ContentValues values = new ContentValues();
      JsonObject jsonObject = Json.createObject().set("FILE_NAME", fileName).set("OPEN_TIME", openTime).set("LAST_TIME", lastTime);
      values.put(KEY_PLAY_DATA, jsonObject.toJsonString());
      this.getContentResolver().insert(Uri.parse("content://" + AUTHORITY + INSERT), values);
    }
  }

}
