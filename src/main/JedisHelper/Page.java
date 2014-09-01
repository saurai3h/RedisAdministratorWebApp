package JedisHelper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kartik.k on 8/20/2014.
 */
public abstract class Page {
    protected int pageSize;
    protected String cursorForScan;
    protected Set<String> keySet;

    public Set<String> getKeySet() {
        return keySet;
    }



    public Page(int pageSize, String cursor){
        this.pageSize = pageSize;
        this.cursorForScan = cursor;
        this.keySet = new HashSet<String>();
    }
    abstract protected void fillKeySet();
    abstract Page nextPage();
    void display(){
        System.out.println(keySet.size());
        for(String key: keySet){
            System.out.println(key);
        }
    };
}