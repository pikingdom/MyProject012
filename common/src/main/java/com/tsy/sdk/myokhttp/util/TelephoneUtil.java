package com.tsy.sdk.myokhttp.util;

/**
 * 获取手机相关参数的工具类
 */
public class TelephoneUtil {

	/**
	 * 是否是步步高手机
	 * @return boolean
	 **/
	public static boolean isVivoPhone() {
		try {
			return (getMachineName().toLowerCase().contains("vivo") || getManufacturer().toLowerCase().contains("vivo") || getManufacturer().equalsIgnoreCase("BBK"));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 是否Oppo手机
	 * @return
	 */
	public static boolean isOppoPhone() {
		try {
			return (getMachineName().toLowerCase().contains("oppo") || getManufacturer().toLowerCase().contains("oppo"));
		} catch (Exception e) {
			return false;
		}
	}

	public static String getMachineName() {
		/**
		 * 开关打开，添加后缀
		 */
		return android.os.Build.MODEL;
	}

	/**
	 * 获取制造商
	 * @return String
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}
}
