package com.cc.android.pdf;

import com.goodow.realtime.android.AndroidPlatform;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.impl.SimpleBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Description: AndroidModule
 * Author: danhantao
 * Update: danhantao(2014-11-18 17:29)
 * Email: danhantao@yeah.net
 */
public class AndroidModule extends AbstractModule {
  static {
    AndroidPlatform.register();
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  Bus provideBus() {
    Bus bus = new SimpleBus();
    return bus;
  }

}
