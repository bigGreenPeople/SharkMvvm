package com.shark.mvvm.utils

import android.app.Application
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import java.lang.Exception
import java.lang.StringBuilder
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.UnknownHostException
import java.security.NoSuchAlgorithmException
import java.util.*

object DeviceInfo {
    var application: Application? = null

    /**
     * 获得唯一设备号
     * @param context Context
     * @return String?
     */
    fun getUniqueId(): String? {
        val id = Build.SERIAL
        return try {
            toMD5(id)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            id
        }
    }

    //获取wf状态下的IP地址
    fun getLocalIpAddress(): String? {
        val wifiManager =
            application?.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        try {
            val result: String = InetAddress.getByName(
                String.format(
                    "%d.%d.%d.%d",
                    ipAddress and 0xff, ipAddress shr 8 and 0xff,
                    ipAddress shr 16 and 0xff, ipAddress shr 24 and 0xff
                )
            ).toString()
            return result.replace("/", "").trim()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        }
        return null
    }


    //wifiMac
     fun getMac():String {
        try {
            val all: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (nif in all) {
                if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                val macBytes = nif.hardwareAddress ?: return ""
                val res1 = StringBuilder()
                for (b in macBytes) {
                    res1.append(String.format("%02X:", b))
                }
                if (res1.isNotEmpty()) {
                    res1.deleteCharAt(res1.length - 1)
                }
                return res1.toString()
            }
        } catch (ex: Exception) {
            Log.e("SharkChilli", "getMac: ",ex)
        }
        return ""
    }
}