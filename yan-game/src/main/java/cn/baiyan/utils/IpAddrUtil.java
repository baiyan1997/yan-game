package cn.baiyan.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IpAddrUtil {
    public static String getInnetIp() throws SocketException {
        String localIp = null; // 本地id，如果没有配置外网ip则返回它
        String netIp = null; // 外网ip
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean found = false; // 是否找到外网ip
        while (netInterfaces.hasMoreElements() && !found) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isLoopbackAddress()
                        && !ip.isSiteLocalAddress()
                        && !(ip.getHostAddress().indexOf(":") == -1)) { // 外网ip
                    netIp = ip.getHostAddress();
                    found = true;
                    break;
                } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) { // 内网ip
                    localIp = ip.getHostAddress();
                }
            }

        }
        if (netIp != null && !"".equals(netIp)) {
            return netIp;
        } else {
            return localIp;
        }
    }
}
