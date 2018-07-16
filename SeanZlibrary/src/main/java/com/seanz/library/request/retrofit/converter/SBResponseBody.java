/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.seanz.library.request.retrofit.converter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.seanz.library.configs.BaseProjectConfig;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * 创建者: wwd
 * 创建日期:16/6/28
 * 类的功能描述:相应body
 */
public final class SBResponseBody<T> implements Converter<ResponseBody, T> {
  private final Type type;

  public SBResponseBody(Type type) {
    this.type = type;
  }

  /**
   * 切换成fastjson解析
   *
   * @throws IOException
   */
  @Override public T convert(ResponseBody value) throws IOException {
    BufferedSource bufferedSource = Okio.buffer(value.source());
    String response = bufferedSource.readUtf8();
    Log.d(BaseProjectConfig.TAG, "接口响应返回的数据 " + response);
    return JSON.parseObject(response, type);
  }
}
