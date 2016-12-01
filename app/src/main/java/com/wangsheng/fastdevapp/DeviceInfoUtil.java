package com.wangsheng.fastdevapp;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeviceInfoUtil {

	private Context mContext;
	private Map<String, String> map = new HashMap<String, String>();;
	private JSONObject js = null;

	public DeviceInfoUtil(Context context) {
		super();
		this.mContext = context;
	}

	public JSONObject getDeviceInfo4js() {
		js = new JSONObject();
		try {
			// IMEI
			js.put("DeviceId", getDeviceIMEI());
			// 判断sd卡是否存在,1存在，0不存在
			js.put("SDFlag", String.valueOf(isSDCard()));
			// 获取设备型号
			js.put("DeviceModel", getModel());
			// 设备类型,0：手机1：平板2：电视3：其他
			js.put("DeviceType", "0");
			js.put("DeviceOsType", "android");
			// Mac地址
			js.put("DeviceMac", getMac());
			// 系统版本号
			js.put("SYSVersion", String.valueOf(getAndroidSDKVersion()));
			// 版本号
			js.put("VersionCode", String.valueOf(getAppVersionCode()));
			// 版本名称
			js.put("VersionName", getAppVersionName());
			// 客户端类型
			js.put("ClientType", "0");
			// 屏幕信息
			js.put("DeviceMetrics", getDisplayMetrics());
			js.put("VersionType", "");
			js.put("VersionSource", "");
			// Root标志0：没有root（未越狱）1：已经root（越狱）
			js.put("RootFlag", String.valueOf(isRootSystem()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return js;
	}

	public Map<String, String> getDeviceInfo() {
		map.clear();
		// IMEI
		map.put("DeviceId", getDeviceIMEI());
		// 判断sd卡是否存在,1存在，0不存在
		map.put("SDFlag", String.valueOf(isSDCard()));
		// 获取设备型号
		map.put("DeviceModel", getModel());
		// 设备类型,0：手机1：平板2：电视3：其他
		map.put("DeviceType", "1");
		// Mac地址
		map.put("DeviceMac", getMac());
		// 系统版本号
		map.put("SYSVersion", String.valueOf(getAndroidSDKVersion()));
		// 版本号
		map.put("VersionCode", String.valueOf(getAppVersionCode()));
		// 版本名称
		map.put("VersionName", getAppVersionName());
		// 客户端类型
		map.put("DeviceOsType", "android");
		// 屏幕信息
		map.put("DisplayMetrics", getDisplayMetrics());
		return map;

	}

	/**
	 * 判断sd卡是否存在
	 * 
	 * @return boolean
	 */
	public int isSDCard() {
		String SDState = android.os.Environment.getExternalStorageState();
		if (SDState.equals(android.os.Environment.MEDIA_MOUNTED)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * 获取手机型号
	 * 
	 * @return String
	 */
	public String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取手机MAC地址
	 * 
	 * @return String
	 */
	public String getMac() {
		WifiManager wifi = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();

		if (info.getMacAddress() != null) {
			return info.getMacAddress();
		} else {
			return "";
		}

	}

	/**
	 * 获取手机设别android SDK版本号
	 * 
	 * @return int
	 * */
	public int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 硬件设备类型
	 * 
	 * @return String
	 * */
	public String getDeviceType() {
		return "android";
	}

	/**
	 * 获取手机设备IMEI
	 * 
	 * @return String
	 * */
	public String getDeviceIMEI() {
		TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService("phone");
		return tm.getDeviceId();
	}

	/**
	 * 获取手机屏幕尺寸
	 * 
	 * @return String
	 */
	public String getDisplayMetrics() {
		// String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;// 屏幕宽（像素，如：480px）
		int screenHeight = dm.heightPixels;// 屏幕高（像素，如：800px）
		return String.valueOf(screenWidth) + "*" + String.valueOf(screenHeight);
	}

	/**
	 * 获取手机屏幕尺寸 高度
	 * 
	 * @return int
	 */
	public int getDisplayMetricsHeight() {
		// String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int screenHeight = dm.heightPixels;// 屏幕高（像素，如：800px）
		return screenHeight;
	}

	/**
	 * 获取手机屏幕尺寸 宽度
	 * 
	 * @return int
	 */
	public int getDisplayMetricsWidth() {
		// String str = "";
		DisplayMetrics dm = new DisplayMetrics();
		dm = mContext.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;// 屏幕高（像素，如：800px）
		return screenWidth;
	}

	/**
	 * 获取应用程序版本名
	 * 
	 * @return String
	 * */
	public String getAppVersionName() {
		String AppVersionName = "";
		try {
			AppVersionName = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AppVersionName;
	}

	/**
	 * 获取应用程序版本号
	 * 
	 * @return int
	 * */
	public int getAppVersionCode() {
		int AppVersionCode = 0;
		try {
			AppVersionCode = mContext.getPackageManager().getPackageInfo(
					mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return AppVersionCode;
	}

	/**
	 * 判断系统是否root
	 * 
	 * @return boolean
	 * */
	public int isRoot() {
		int isroot = 0;
		try {
			if (Runtime.getRuntime().exec("su").getOutputStream() == null) {
				// 没有root
				isroot = 0;
			} else {
				isroot = 1;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isroot;
	}

	// public int isTabletDevice() {
	// int i=0;
	// TelephonyManager telephony = (TelephonyManager)
	// mContext.getSystemService(Context.TELEPHONY_SERVICE);
	// int type = telephony.getPhoneType();
	// if (type == TelephonyManager.PHONE_TYPE_NONE) {
	// i=1;
	// } else {
	// i=0;
	// }
	// return i;
	// }
	private final static int kSystemRootStateUnknow = -1;
	private final static int kSystemRootStateDisable = 0;
	private final static int kSystemRootStateEnable = 1;
	private static int systemRootState = kSystemRootStateUnknow;

	public static int isRootSystem() {
		if (systemRootState == kSystemRootStateEnable) {
			return 1;
		} else if (systemRootState == kSystemRootStateDisable) {

			return 0;
		}
		File f = null;
		final String kSuSearchPaths[] = { "/system/bin/", "/system/xbin/",
				"/system/sbin/", "/sbin/", "/vendor/bin/" };
		try {
			for (int i = 0; i < kSuSearchPaths.length; i++) {
				f = new File(kSuSearchPaths[i] + "su");
				if (f != null && f.exists()) {
					systemRootState = kSystemRootStateEnable;
					return 1;
				}
			}
		} catch (Exception e) {
		}
		systemRootState = kSystemRootStateDisable;
		return 0;
	}

	/**
	 * 判断设备是否是平板 平板：true 手机：false
	 * */
	private String isTabletDevice() {
		return "0";
		// if (android.os.Build.VERSION.SDK_INT >= 11) { // honeycomb
		// // test screen size, use reflection because isLayoutSizeAtLeast is
		// // only available since 11
		// Configuration con = mContext.getResources().getConfiguration();
		// try {
		// Method mIsLayoutSizeAtLeast = con.getClass().getMethod(
		// "isLayoutSizeAtLeast", int.class);
		// Boolean r = (Boolean) mIsLayoutSizeAtLeast.invoke(con,
		// 0x00000004); // Configuration.SCREENLAYOUT_SIZE_XLARGE
		// if (r) {
		// return "1";
		// } else {
		// return "0";
		// }
		// } catch (Exception x) {
		// x.printStackTrace();
		// return "0";
		// }
		// }
		// return "0";
	}

}
