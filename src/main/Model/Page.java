package Model;

import java.util.ArrayList;

/**
 * Created by Saurabh Paliwal on 27/8/14.
 */
public class Page {
    private ArrayList<Key> keyList;
    public ArrayList<Key> getKeyList() {
        return keyList;
    }

    public void setKeyList(ArrayList<Key> keyList) {
        this.keyList = keyList;
    }

    public Key removeLast()  {
        return keyList.remove(keyList.size()-1);
    }

    public boolean isEmpty()    {
        return keyList.isEmpty();
    }
}
