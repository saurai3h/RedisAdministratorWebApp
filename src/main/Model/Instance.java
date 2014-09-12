package Model;

import com.google.gson.Gson;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

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
    private InfoSnapshotter infoSnapshotter;
    private String cursor;
    private int expectedPageSize;
    boolean isMonitored;
    private ScheduledExecutorService executorService;
    public void resetPageList(){
        cursor = "";
        pages = new LinkedList<Page>();
        searchPage = new Page();
    }

    public Instance(String host, int port, boolean isMonitored)  {
        searchPage = new Page();
        expectedPageSize = 15;
        hostAndPort = new HostAndPort(host,port);
        jedis = new Jedis(host,port);
        pages = new LinkedList<Page>();
        cursor = "";
        this.isMonitored = isMonitored;
        infoSnapshotter = new InfoSnapshotter(new HostAndPort("172.16.137.79",7005),this.hostAndPort);
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(infoSnapshotter,0,10, TimeUnit.SECONDS);
        if(isMonitored)
            infoSnapshotter.startMonitorMode();
    }


    public boolean keyExists(String key) {
        return jedis.exists(key);
    }

    public boolean deleteField(String key,String field, String value, String type) {

        if(type.equals("string"))   {
            if(jedis.del(key)>0)return true;
            else return false;
        }
        else if(type.equals("set")) {
            if(jedis.srem(key,value)>0)return true;
            else return false;
        }
        else if(type.equals("list"))    {

            List<String> tempList = jedis.lrange(key,0,-1);
            tempList.remove(Integer.parseInt(field));
            jedis.del(key);

            for(String s : tempList) {
                jedis.rpush(key, s);
            }

            if(jedis.exists(key))return true;
            else return false;
        }
        else if(type.equals("zset"))    {
            if(jedis.zrem(key,field)>0)return true;
            else return false;
        }
        else if(type.equals("hash"))    {
            if(jedis.hdel(key,field)>0)return true;
            else return false;
        }
        else return false;
    }

    public boolean addField(String key,String field,String value,String type)   {
        if(type.equals("set")) {
            if(jedis.sadd(key, value)>0)return true;
            else return false;
        }
        else if(type.equals("list"))    {
            if(jedis.rpush(key, value)>0)return true;
            else return false;
        }
        else if(type.equals("zset"))    {
            if(jedis.zadd(key,Double.parseDouble(value),field)>0)return true;
            else return false;
        }
        else if(type.equals("hash"))    {
            if(jedis.hset(key,field,value)>0)return true;
            else return false;
        }
        else if(type.equals("string"))  {
            try {
                jedis.set(key, value);
                return true;
            }
            catch (JedisException e)    {
                return false;
            }
        }
        else return false;
    }

    public void editField(String key,String field,String value,String newField, String newValue, String type)  {
        if(type.equals("zset")) {
            deleteField(key, field, value, "zset");
            addField(key, newField, newValue, "zset");
        }
        else {
            deleteField(key, field, value, type);
            addField(key, newField, newValue, type);
        }
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

    public void addKey(String key, String type, String hashValueOrZsetElement, String hashFieldOrZsetScore)  {
        if (type.equals("zset")) {
            jedis.zadd(key, Double.parseDouble(hashFieldOrZsetScore), hashValueOrZsetElement);

        } else if (type.equals("hash")) {
            jedis.hset(key, hashFieldOrZsetScore, hashValueOrZsetElement);

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

    public List<String> myScan(long timeInSecondsToRun)    {
        long t = System.currentTimeMillis();
        long end = t + timeInSecondsToRun*1000;
        String myCursor = "0";

        List<String> list = new ArrayList<String>();

        while(System.currentTimeMillis() < end) {
            ScanResult result = jedis.scan(myCursor);
            myCursor = result.getStringCursor();
            list.addAll(result.getResult());
            if(myCursor.equals("0"))
                break;
        }

        return list;
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

        if(!keyExists(key)) return null;
        String type = getTypeOfKey(key);
        String JsonOfValue = "";
        Object value;
        if (type.equals("zset")) {
            value = jedis.zrangeWithScores(key, 0, -1);

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
            return null;
        }

        if(!type.equals("string")) {
            JsonOfValue = new Gson().toJson(value);
        }
        Map<String,String> map = new HashMap<String, String>();
        map.put("json",JsonOfValue);
        map.put("type",type);
        return map;
    }

    public void startMonitor() {
        if(setMonitorBitInDb(true)) {
            infoSnapshotter.startMonitorMode();
            this.isMonitored = true;
        }
    }

    private boolean setMonitorBitInDb(boolean newValueOfIsMonitored) {
        String isMonitored;
        if(newValueOfIsMonitored)
            isMonitored = "1";
        else
            isMonitored = "0";
        boolean succeeded =true;
        Connection connection = SqlInterface.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            String query = "UPDATE instances SET IsMonitored=" + isMonitored +
                    " WHERE HostName=\""+ this.hostAndPort.getHost()
                    +"\" AND PortNumber="+ Integer.toString(this.hostAndPort.getPort()) + ";";
            System.out.println(query);
            statement.execute(query);
            connection.close();
            statement.close();
        } catch (SQLException e) {
            succeeded = false;
            e.printStackTrace();
        }
        return succeeded;
    }



    public void stopMonitor(){
        if(setMonitorBitInDb(false)) {
            infoSnapshotter.stopMonitorMode();
            this.isMonitored = false;
        }
    }

    public void toggleMonitorMode(){
        if(isMonitored){
            isMonitored = false;
            stopMonitor();
        }
        else {
            isMonitored = true;
            startMonitor();
        }
    }

    public void close(){
        jedis.close();
        pages = null;
        stopMonitor();
        executorService.shutdown();
    }
}

