package Model;

import redis.clients.jedis.HostAndPort;

/**
 * Created by kartik.k on 9/11/2014.
 */
public final class Constants {
    public static final String INSTANCE_NOT_FOUND_STATUS_CODE = "instance not found";
    public static final String SUCCESS_STATUS_CODE ="success";
    public static final String PERMISSION_DENIED_STATUS_CODE = "permission denied";
    public static final String SQL_ERROR_STATUS_CODE = "SQL error";
    public static final String REDIS_ERROR_STATUS_CODE = "redis error";
    public static final String UNKNOWN_ERROR = "strange error!";
    public static final String SERVLET_ERROR_CODE = "servlet error";
    public static final String SQL_DB_HOST = "172.16.137.79";
    public static final HostAndPort INFO_STORE = new HostAndPort("172.16.137.228",7000);

    public static final int INFO_SNAPSHOT_EXPIRY_TIME = 120;
    public static final String ALREADY_PRESENT_IN_INSTANCES = "anotherViewerAdded";




}
