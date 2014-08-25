package JedisHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kartik.k on 8/20/2014.
 */
public abstract class Page {
    protected int pageSize;
    protected String cursorForScan;
    protected Set<String> keyList;

    public Set<String> getKeyList() {
        return keyList;
    }



    public Page(int pageSize, String cursor){
        this.pageSize = pageSize;
        this.cursorForScan = cursor;
        this.keyList = new HashSet<String>();
    }

    abstract Page nextPage();
    void display(){
        System.out.println(keyList.size());
        for(String key: keyList){
            System.out.println(key);
        }
    };
}