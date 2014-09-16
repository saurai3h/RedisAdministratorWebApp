package Model;

import redis.clients.jedis.HostAndPort;

/**
 * Created by Saurabh Paliwal on 15/9/14.
 */
public class InstanceWrapper {
    String host;
    Integer port;
    Boolean isMonitor;

    public InstanceWrapper(HostAndPort hostAndPort, Boolean isMonitor)    {
        this.host = hostAndPort.getHost();
        this.port = hostAndPort.getPort();
        this.isMonitor = isMonitor;
    }

}
