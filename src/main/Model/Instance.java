package Model;

import com.google.gson.Gson;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Saurabh Paliwal on 27/8/14.
 */
public class Instance {


    public HostAndPort getHostAndPort() {
        return hostAndPort;
    }

    private HostAndPort hostAndPort;
    private Jedis jedis;
    private LinkedList<Page> pages;
    private Page searchPage;
    private Monitor monitor;
//    public int getCurrentPageIndex() {
//        return currentPageIndex;
//    }

//    int currentPageIndex;
    private String cursor;
    private int expectedPageSize;

    public Instance(String host, int port)  {
        searchPage = new Page();
        expectedPageSize = 15;
        hostAndPort = new HostAndPort(host,port);
        jedis = new Jedis(host,port);
        pages = new LinkedList<Page>();
        cursor = "";
    }    
    public boolean keyExists(String key) {
        return jedis.exists(key);
    }

    public void renameKey(String oldKeyName, String newKeyName){
        jedis.rename(oldKeyName,newKeyName);
    }
    public void addKey(String key,String type, String value)   {
        if (type.equals("string")) {
            jedis.set(key, value);

        } else if (type.equals("set")) {
            jedis.sadd(key, value);

        } else if (type.equals("list")) {
            jedis.lpush(key, value);
        } else {
            System.out.println("Invalid Data Structure");
        }
    }

    public void addKey(String key, String type, String value1, String value2)  {
        if (type.equals("zset")) {
            jedis.zadd(key, Double.parseDouble(value1), value2);

        } else if (type.equals("hash")) {
            jedis.hset(key, value1, value2);

        } else {
            System.out.println("Invalid Data Structure");
        }
    }

    public void deleteKey(String key) {
        jedis.del(key);
    }


    private boolean fetchOneMorePage() {
        if(cursor.equals("0")){
            //this was the last page, invalid operation
            return false;
        }
        else {

            Page nextPage = new Page();
            if(cursor.equals("")){
                cursor = "0";
            }
            ArrayList<String> keyList = (ArrayList<String>)scan(expectedPageSize);
            ArrayList<Key> newPageKeyList = getKeysArrayFromKeyNameArray(keyList);
            nextPage.setKeyList(newPageKeyList);
            pages.addLast(nextPage);
            return true;
        }


    }

    private ArrayList<Key> getKeysArrayFromKeyNameArray(ArrayList<String> keyList) {
        ArrayList<Key> newPageKeyList = new ArrayList<Key>();
        for (String keyName:keyList){
            newPageKeyList.add(getNewKeyByName(keyName));
        }
        return newPageKeyList;
    }

    public boolean search(String key){
        if(jedis.exists(key)){
            ArrayList<String> searchPageList = new ArrayList<String>();
            searchPageList.add(key);
            searchPage.setKeyList(getKeysArrayFromKeyNameArray(searchPageList));
            return true;
        }
        else
            return false;
    }

    private List<String> scan(int count){
        ScanParams scanParams = new ScanParams();
        scanParams.count(count);
        ScanResult<String> scanResult = jedis.scan(cursor,scanParams);
        this.cursor = scanResult.getStringCursor();
        return (scanResult.getResult());
    }

    public boolean isAlive()    {
        String pong = jedis.ping();
        return pong.equals("PONG");
    }

    public Page getPageAtIndex(int zeroBasedIndex)    {
        while(pages.size()<=zeroBasedIndex && fetchOneMorePage()){
        }
        if(pages.size()<=zeroBasedIndex){
            return null;
        }
        else if(zeroBasedIndex<0){
            return null;
        }
        else {
            return pages.get(zeroBasedIndex);
        }
    }

    public Key getNewKeyByName(String keyName){
        return new Key(jedis,keyName);
    }

    public Page getSearchPage()     {
        return searchPage;
    }

    public String getTypeOfKey(String key){
        return jedis.type(key);
    }

    public Map<String,String> getJsonValueOfAKey(String key){
        String type = getTypeOfKey(key);
        String JsonOfValue = "";
        Object value;

        System.out.println(key + " " + type);

        if (type.equals("zset")) {
            value = jedis.zrange(key, 0, -1);
            HashMap<String,Double> map = new HashMap<String, Double>();
            for (String element:(Set<String>)value){
                Double score = jedis.zscore(key,element);
                map.put(element,score);

            }
            value = map;
        } else if (type.equals("hash")) {
            value = jedis.hgetAll(key);

        } else if (type.equals("list")) {
            value = jedis.lrange(key,0,-1);

        } else if (type.equals("string")) {
            value = jedis.get(key);
            JsonOfValue = "[\"" + value + "\"]";

        } else if (type.equals("set")) {
            value = jedis.smembers(key);

        } else {
            System.out.println("Invalid Data Structure " + key);
            return new HashMap<String, String>();
        }

        if(!type.equals("string")) {
            JsonOfValue = new Gson().toJson(value);
        }
        Map<String,String> map = new HashMap<String, String>();
        map.put("json",JsonOfValue);
        map.put("type",type);
        return map;
    }

    public void startMonitor(double intervalInSeconds){
        boolean succeeded =false;
        Connection connection = SqlInterface.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String selectQuery = "Select IsMonitored FROM instances WHERE HostName=\""
                    + this.hostAndPort.getHost()+"\" AND PortNumber="
                    + Integer.toString(this.hostAndPort.getPort()) + ";";
            System.out.println(selectQuery);
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if(resultSet.wasNull())    {
                System.out.println("the instance was not found, did anyone else deleted it?");
            }

            if(resultSet.next()){
                if(resultSet.getBoolean("IsMonitored")){
                    System.out.println("already being monitored!!");
                }
                else{
                    succeeded = true;
                    String query = "UPDATE instances SET IsMonitored=1 " +
                            "WHERE HostName=\""+ this.hostAndPort.getHost()
                            +"\" AND PortNumber="+ Integer.toString(this.hostAndPort.getPort()) + ";";
                    System.out.println(query);
                    statement.execute(query);
                    System.out.println("executed");
                }
            }

            connection.close();
            statement.close();
            resultSet.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(succeeded){
            System.out.println("starting the monitor");
            monitor = new Monitor(new HostAndPort("172.16.137.228",7000),
                    this.hostAndPort,intervalInSeconds);
            Thread t = new Thread(monitor);
            t.start();
        }





    }

    public void stopMonitor(){
        boolean succeeded = false;
        Connection connection = SqlInterface.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String selectQuery = "Select IsMonitored FROM instances WHERE HostName=\""
                    + this.hostAndPort.getHost()+"\" AND PortNumber="
                    + Integer.toString(this.hostAndPort.getPort()) + ";";
            System.out.println(selectQuery);
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if(resultSet.wasNull())    {
                System.out.println("the instance was not found, did anyone else deleted it?");
            }

            if(resultSet.next()){
                if(!resultSet.getBoolean("IsMonitored")){
                    System.out.println("already stopped monitored!!");
                }
                else{
                    String query = "UPDATE instances SET IsMonitored=0 " +
                            "WHERE HostName=\""+ this.hostAndPort.getHost()
                            +"\" AND PortNumber="+ Integer.toString(this.hostAndPort.getPort()) + ";";
                    System.out.println(query);
                    statement.execute(query);
                    succeeded = true;
                }
            }
            connection.close();
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(succeeded)
            monitor.terminate();
    }
}

