package cn.baiyan.game;

public class HostAndPort {

    /**
     * ip address
     */
    private String host;

    /**
     * socket port
     */
    private int port;

    public static HostAndPort valueOf(String host, int port) {
        HostAndPort hostAndPort = new HostAndPort();
        hostAndPort.host = host;
        hostAndPort.port = port;
        return hostAndPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
