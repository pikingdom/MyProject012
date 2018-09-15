package com.nd.hilauncherdev.plugin.navigation.loader;

import android.text.TextUtils;

import com.nd.hilauncherdev.plugin.navigation.bean.HotwordItemInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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


	public static List<HotwordItemInfo> parseSmHotWorlds(String jsonContent){
		if(TextUtils.isEmpty(jsonContent)){
			return null;
		}
		try {
			JSONArray array = new JSONArray(jsonContent);
			List<HotwordItemInfo> list = new ArrayList<HotwordItemInfo>();
			for (int i = 0; i < array.length(); i++) {
				HotwordItemInfo info = new HotwordItemInfo();
				JSONObject jsonObj 	= 		(JSONObject) array.opt(i);
				info.name 			= 		jsonObj.getString("title");
				info.url = jsonObj.getString("url");
				list.add(info);
			}
			return list;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
