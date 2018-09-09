package com.nd.hilauncherdev.plugin.navigation.loader;

import android.text.TextUtils;

import com.nd.hilauncherdev.plugin.navigation.bean.HotwordItemInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 热词数据加载<br/>
 *
 * @author chenzhihong_9101910
 *
 */
public class NaviWordLoader {

	public static int ONE_BATCH_SIZE = 6;

	public static String HOTWORD_ATTR_NAME = "name_";
	public static String HOTWORD_ATTR_DETAILURL = "detailUrl_";
	public static String HOTWORD_ATTR_COLOR = "color_";
	public static String HOTWORD_ATTR_RESID = "resId_";

	public static ArrayList<HotwordItemInfo> convertHotwordList(List<Object> list) {

		ArrayList<HotwordItemInfo> itemList = convertHotwordListWithoutCheckSize(list);
		ArrayList<HotwordItemInfo> resultList = new ArrayList<HotwordItemInfo>();

		if(itemList == null || itemList.size() ==0){
			return itemList;
		}
		int total = itemList.size();
		for (int i = 0; i < total && (i % NaviWordLoader.ONE_BATCH_SIZE) + (total - i) >= NaviWordLoader.ONE_BATCH_SIZE; i++) {
			HotwordItemInfo hotwordItemInfo = itemList.get(i);
			if (!TextUtils.isEmpty(hotwordItemInfo.name) && !TextUtils.isEmpty(hotwordItemInfo.detailUrl)) {
				resultList.add(hotwordItemInfo);
			}
		}
		return resultList;
	}

	public static ArrayList<HotwordItemInfo> convertHotwordListWithoutCheckSize(List<Object> list) {

		ArrayList<HotwordItemInfo> itemList = new ArrayList<>();

		if(list == null){
			return itemList;
		}
		int total = list.size();
		for (int i = 0; i < total; i++) {
			Object obj = list.get(i);
			try {
				HotwordItemInfo hotwordItemInfo = new HotwordItemInfo();
				Field[] fieldS = obj.getClass().getDeclaredFields();
				for (Field field : fieldS) {
					Class fieldC = field.getType();
					if (fieldC != String.class) {
						continue;
					}
					String value = (String) field.get(obj);
					if (TextUtils.isEmpty(value))
						continue;
					if (value.startsWith(HOTWORD_ATTR_NAME)) {
						hotwordItemInfo.name = value.replace(HOTWORD_ATTR_NAME, "");
					} else if (value.startsWith(HOTWORD_ATTR_DETAILURL)) {
						hotwordItemInfo.detailUrl = value.replace(HOTWORD_ATTR_DETAILURL, "");
					} else if (value.startsWith(HOTWORD_ATTR_COLOR)) {
						int color = 0xff333333;
						try {
							color = Integer.parseInt(value.replace(HOTWORD_ATTR_COLOR, ""));
						} catch (Exception e) {
							e.printStackTrace();
						}
						hotwordItemInfo.color = color;
					} else if (value.startsWith(HOTWORD_ATTR_RESID)) {
						int resId = 0;
						try {
							resId = Integer.parseInt(value.replace(HOTWORD_ATTR_RESID, ""));
						} catch (Exception e) {
							e.printStackTrace();
						}
						hotwordItemInfo.resId = resId;
					}

				}

				if (!TextUtils.isEmpty(hotwordItemInfo.name) && !TextUtils.isEmpty(hotwordItemInfo.detailUrl)) {
					itemList.add(hotwordItemInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return itemList;

	}
}
