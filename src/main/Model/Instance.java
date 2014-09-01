package Model;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Saurabh Paliwal on 27/8/14.
 */
public class Instance {

    HostAndPort hostAndPort;
    Jedis jedis;
    LinkedList<Page> pages;
    Page searchPage;
    int currentPageIndex;
    String cursor;
    int expectedPageSize;

    public Instance(String host, int port)  {
        expectedPageSize = 30;
        hostAndPort = new HostAndPort(host,port);
        jedis = new Jedis(host,port);
        pages = new LinkedList<Page>();
        searchPage = new Page();
        currentPageIndex = -1;
        cursor = "";
        goToNextPage();

    }

    void addKey(String key,String type, String value)   {
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

    void addKey(String key, String type, String value1, String value2)  {
        if (type.equals("sortedSet")) {
            jedis.zadd(key, Double.parseDouble(value1), value2);

        } else if (type.equals("hashMap")) {
            jedis.hset(key, value1, value2);

        } else {
            System.out.println("Invalid Data Structure");
        }
    }

    void deleteKey(String key) {
        jedis.del(key);
//        (pages.get(currentPageIndex).getKeyList().remove(key));
        Page currentPage = pages.get(currentPageIndex);
        ArrayList<String> currentPageKeyList = currentPage.getKeyList();
        int spaceRemainingOnPage = expectedPageSize - currentPageKeyList.size();
        if (spaceRemainingOnPage > 0 && !cursor.equals("0")) {
            currentPageKeyList.addAll(scan(spaceRemainingOnPage));
        }
        if(cursor.equals("0")) {
            String lastKey = pages.getLast().removeLast();
            currentPageKeyList.add(lastKey);
        }

        currentPageKeyList.remove(key);

        if (currentPageKeyList.isEmpty()) {
            pages.remove(currentPage);
            currentPageIndex -= 1;
        }
        if (currentPageIndex < 0) {
//            this means database is empty: otherwise scan would have got some keys, and they
// would have been added to currentPageKeyList. Since Scan is unreliable, we add the following check
            if (!pages.isEmpty()) {
                currentPageIndex = 0;
            } else {
                //database is really empty
                pages.add(new Page());
                currentPageIndex = 0;
            }
        }
        currentPage.setKeyList(currentPageKeyList);
        pages.remove(currentPageIndex);
        pages.add(currentPageIndex,currentPage);
    }

   void goToNextPage(){

       currentPageIndex++;
       if(currentPageIndex>=pages.size()){
           if(cursor.equals("0")){
               //this was the last page, invalid operation
           }
           else {
               Page nextPage = new Page();
               if(cursor.equals("")){
                   cursor = "0";
               }
               ArrayList<String> newPageKeyList = (ArrayList<String>)scan(expectedPageSize);
               nextPage.setKeyList(newPageKeyList);
               pages.addLast(nextPage);

           }
       }
   }

    void goToPrevPage(){
        if(currentPageIndex<= 0){
//            this was the first page, so invalid operation
        }
        else {
            currentPageIndex--;
        }
    }

    void search(String key){
        if(jedis.exists(key)){
            ArrayList<String> searchPageList = new ArrayList<String>();
            searchPageList.add(jedis.get(key));
            searchPage.setKeyList(searchPageList);
        }
        else
            ;
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

    public Page getCurrentPage()    {
        return pages.get(currentPageIndex);
    }

    public Page getSearchPage()     {
        return searchPage;
    }
}
