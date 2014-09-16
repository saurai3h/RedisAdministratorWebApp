package Model;

import java.util.ArrayList;

/**
 * Created by Saurabh Paliwal on 15/9/14.
 */
public class Node {
    public String label;
    public ArrayList<Node> children;
    public Node(String value)  {
        label = value;
        children = new ArrayList<Node>();
    }
}
