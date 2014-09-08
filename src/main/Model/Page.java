package Model;

import java.util.ArrayList;

/**
 * Created by Saurabh Paliwal on 27/8/14.
 */
public class Page {
    private ArrayList<String> keyList;

    public ArrayList<String> getKeyList() {
        return keyList;
    }

    public void setKeyList(ArrayList<String> keyList) {
        this.keyList = keyList;
    }

    public String removeLast()  {
        return keyList.remove(keyList.size()-1);
    }

    public boolean isEmpty()    {
        return keyList.isEmpty();
    }
}
