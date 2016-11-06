package com.meticulus.codinalteparts.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

/**
 * Created by meticulus on 4/7/14.
 */
public class FunctionsMain {

    private static final String TAG = "Codinalte Parts";

    /* General */

    private static final String TEMP_DIR_CMD = "mkdir -p /data/local/tmp";

    /* Offline Charging */

    private static final String CHARGER_SETTINGS_PATH = "/cache/misc/charger";

    private static final String CHARGER_SHOWDATETIME_PATH = CHARGER_SETTINGS_PATH + "/charger_show_datetime";

    private static final String CHARGER_SHOWDATETIME_ENABLE = "touch " + CHARGER_SHOWDATETIME_PATH;

    private static final String CHARGER_SHOWDATETIME_DISABLE = "rm " + CHARGER_SHOWDATETIME_PATH;

    private static final String CHARGER_NOSUSPEND_PATH = CHARGER_SETTINGS_PATH + "/charger_no_suspend";

    private static final String CHARGER_NOSUSPEND_ENABLE = "touch " + CHARGER_NOSUSPEND_PATH;

    private static final String CHARGER_NOSUSPEND_DISABLE = "rm " + CHARGER_NOSUSPEND_PATH;

    /* Logging Vars */

    private static final String CMD_KMSG = "cat /proc/kmsg | while read LINE;do " + "" +
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f5) - $LINE\" >> "+
            "/sdcard/autolog_kmsg_\"$DATE\".txt;done &";

    private static final String CMD_LOGCAT = "logcat | while read LINE;do "+
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f4) - $LINE\" >> "+
            "/sdcard/autolog_logcat_\"$DATE\".txt;done &";

    private static final String CMD_RILLOG = "cat /dev/log/radio | while read LINE;do "+
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f4) - $LINE\" >> "+
            "/sdcard/autolog_rillog_\"$DATE\".txt;done &";

    public static void setChargerShowDateTime(boolean enabled) {

        try {
            if (enabled) {
                Log.i(TAG, "Enabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn("mkdir -p " + CHARGER_SETTINGS_PATH,false,false);
                CommandUtility.ExecuteNoReturn(CHARGER_SHOWDATETIME_ENABLE, false, false);
            } else {
                Log.i(TAG, "Disabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn(CHARGER_SHOWDATETIME_DISABLE, false, false);
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    public static boolean getChargerShowDateTime() {

        return new File(CHARGER_SHOWDATETIME_PATH).exists();

    }
    public static void setChargerNoSuspend(boolean enabled) {

        try {
            if (enabled) {
                Log.i(TAG, "Enabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn("mkdir -p " + CHARGER_SETTINGS_PATH,false,false);
                CommandUtility.ExecuteNoReturn(CHARGER_NOSUSPEND_ENABLE, false, false);
            } else {
                Log.i(TAG, "Disabling ChargerShowDateTime");
                CommandUtility.ExecuteNoReturn(CHARGER_NOSUSPEND_DISABLE, false, false);
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    public static boolean getChargerNoSuspend() {

        return new File(CHARGER_NOSUSPEND_PATH).exists();

    } 

    public static void startAutokmsg()
    {
        try
        {
            Log.i(TAG, "Running auto kmsg...");
            CommandUtility.ExecuteNoReturn(CMD_KMSG,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void startAutologcat()
    {
        try
        {
            Log.i(TAG, "Running auto logcat...");
            CommandUtility.ExecuteNoReturn(CMD_LOGCAT,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void startAutorillog()
    {
        try
        {
            Log.i(TAG, "Running auto ril log...");
            CommandUtility.ExecuteNoReturn(CMD_RILLOG,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static boolean glove_mode_is_on() {
       String result = "";
       try {
           result = CommandUtility.ExecuteShellCommandTrimmed("cat /sys/devices/platform/huawei_touch/touch_glove",false,false);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"Glove mode is" + result);
       return result.equals("1");
    }

    public static void set_glove(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Setting Glove Mode ON");
	        CommandUtility.ExecuteNoReturn("echo 1 > /sys/devices/platform/huawei_touch/touch_glove", false, false);
	    } else {
		Log.i(TAG, "Settings Glove Mode OFF");
	        CommandUtility.ExecuteNoReturn("echo 0 > /sys/devices/platform/huawei_touch/touch_glove", false, false); 
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static boolean usb_host_mode_is_on() {
       String result = "";
       try {
           result = CommandUtility.ExecuteShellCommandTrimmed("cat /sys/devices/ff100000.hisi_usb/plugusb",false,false);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"USB HOST is" + result);
       return result.contains("OTG_DEV_HOST");
    }

    public static void set_otg(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Settings USB HOST ON");
	        CommandUtility.ExecuteNoReturn("echo hoston > /sys/devices/ff100000.hisi_usb/plugusb", false, false);
	    } else {
		Log.i(TAG, "Settings USB HOST OFF");
	        CommandUtility.ExecuteNoReturn("echo hostoff > /sys/devices/ff100000.hisi_usb/plugusb", false, false); 
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static void testProp()
    {
        Log.e(TAG, "Device is: " + Build.MODEL);
    }
}
