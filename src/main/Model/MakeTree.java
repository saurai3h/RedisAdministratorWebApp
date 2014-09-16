package Model;

import java.util.Set;

/**
 * Created by Saurabh Paliwal on 15/9/14.
 */

public class MakeTree {

    public static Node makeTree(Set<String> setOfKeys) {

        Node root = new Node("Keys");


        for (String s : setOfKeys) {

            int counter = 0;
            Node node = root;

            String[] sChildren = s.split(":");
            int sLen = sChildren.length;

            while (counter < sLen) {

                String nodeData = sChildren[counter++];

                boolean found = false;

                int len = node.children.size();
                for (int i = 0; i < len; ++i) {
                    if (node.children.get(i).label.equals(nodeData)) {
                        node = node.children.get(i);
                        found = true;
                        break;
                    }
                }

                if (found == false) {
                    Node newNode = new Node(nodeData);
                    node.children.add(newNode);
                    node = newNode;
                }
            }

        }

        return root;
    }
}
