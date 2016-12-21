package com.joytouch.superlive.utils;

import com.google.gson.Gson;

public class GsonUitls {

	
	public static <T> T json2Bean(String result,Class<T> clazz){
		 Gson gson = new Gson();
		 T t = gson.fromJson(result, clazz);
		 return t;
	}
}
