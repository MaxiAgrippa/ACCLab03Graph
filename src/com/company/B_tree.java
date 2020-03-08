package com.company;

import com.sun.source.tree.Tree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Maxi Agrippa
 */

//TODO: Write B tree!!!!!
public class B_tree<Key extends Comparable<Key>, Value>
{
    // Maxi Node Children Number
    private static final int MaxNodeChildrenNumber = 4;
    // Root
    private B_tree.Node root;
    // Tree Height
    private int TreeHight;
    // Number of Key-Value pairs
    private int numberOfPairs;

    // Node of the tree
    private static final class Node
    {
        // Number of the children
        private int numberOfChildren;
        // Children (List Array), Each child carry node, leaf have no node.
        private List[] children = new List[MaxNodeChildrenNumber];

        // Node Constructor, create a node with numberOfChildren.
        private Node (int numberOfChildren)
        {
            this.numberOfChildren = numberOfChildren;
        }
    }

    // If a node is internal node, it will only use and contain key(Comparable) and next(Node)
    // If a node is external node(leaf), it will only use and contain key(Comparable) and value(Value)
    private static class List
    {
        // the Key (Comparable)
        private Comparable key;
        // the value (Object)
        private Object value;
        // Node that Connect to next node
        private Node next;     // helper field to iterate over array entries

        // Constructor, offer null value or null next to differ internal node or external node(leaf)
        public List (Comparable key, Object value, Node next)
        {
            // initialize
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // Constructor
    public B_tree ()
    {
        // Create root Node with 0 children.
        root = new Node(0);
    }

    // Get the number of key-value pairs in the B-tree
    public int size ()
    {
        return numberOfPairs;
    }

    // Get the height of B-tree
    public int height ()
    {
        return TreeHight;
    }

    // Recursive search Nodes.
    private Value search (Node node, Key key, int treeHight)
    {
        // get node's children.
        List[] children = node.children;

        // if the node is an external(leaf) node
        if (treeHight == 0)
        {
            // for each child
            for (int j = 0; j < node.numberOfChildren; j++)
            {
                // if the key equal the child's key. return the value of the child.
                if (keyEqual(key, children[j].key))
                {
                    return (Value) children[j].value;
                }
            }
        }

        // if the node is an internal node
        else
        {
            // for each possible child in node
            for (int j = 0; j < node.numberOfChildren; j++)
            {
                // if there is the child node, and the key is smaller than the key of the child's key,
                if (j + 1 == node.numberOfChildren || KeySmaller(key, children[j + 1].key))
                {
                    // recursive search with the child's child with treeHight - 1 as treeHight (in subtree)
                    return search(children[j].next, key, treeHight - 1);
                }
            }
        }
        // if there is no match, return null.
        return null;
    }

    // insert key-value pair
    // add code to check for duplicate keys
    public void put (Key key, Value value)
    {
        // insert the node, check from root.
        Node node = insert(root, key, value, TreeHight);
        // increase the number of key value pair
        numberOfPairs++;
        // if no need to split. the root was not split
        if (node == null)
        {
            return;
        }
        // if need to split root, and the root was split, create a new root.
        Node newRoot = new Node(2);
        // put the split root's "first" child' key and the node root under the new root(internal node).
        newRoot.children[0] = new List(root.children[0].key, null, root);
        // put the split node's(contain old root's first amd second children) first child's key and the new node to the new root(internal node)/
        newRoot.children[1] = new List(node.children[0].key, null, node);
        // set new root
        root = newRoot;
        // add Tree Height.
        TreeHight++;
    }

    // Recursive insert a node
    private B_tree.Node insert (Node node, Key key, Value value, int treeHight)
    {
        // Which child it should be in the node? (0 to 3)
        int j;
        // Using key and value to create a new external(leaf) node
        List list = new List(key, value, null);

        // if the treeHight == 0, external(root) node
        if (treeHight == 0)
        {
            // for each child of the node
            for (j = 0; j < node.numberOfChildren; j++)
            {
                // if the key smaller than the child's key.
                if (KeySmaller(key, node.children[j].key))
                {
                    break;
                }
            }
        }
        // if the treeHight !=0, internal node
        else
        {
            // for each child of the node
            for (j = 0; j < node.numberOfChildren; j++)
            {
                // find the biggest child or the key number smaller than the one bigger child's key
                if ((j + 1 == node.numberOfChildren) || KeySmaller(key, node.children[j + 1].key))
                {
                    // Recursive insert it to the subtree of the node's child
                    Node newNode = insert(node.children[j++].next, key, value, treeHight - 1);
                    // if there is no new node return,
                    if (newNode == null)
                    {
                        // return null
                        return null;
                    }
                    // if there is a new node return, set the list's key to new node's first child's key
                    list.key = newNode.children[0].key;
                    // set list's next to new node.
                    list.next = newNode;
                    // break the loop.
                    break;
                }
            }
        }
        // for each child of the node
        for (int i = node.numberOfChildren; i > j; i--)
        {
            node.children[i] = node.children[i - 1];
        }
        // put list in ith child
        node.children[j] = list;
        // increase the number of children of this node
        node.numberOfChildren++;
        // if the number of children is smaller than the Maximum children number,
        if (node.numberOfChildren < MaxNodeChildrenNumber)
        {
            // return null indicate the successful insert.
            return null;
        }
        // if not smaller,
        else
        {
            // split the node.
            return split(node);
        }
    }

    // split node in half
    private Node split (Node node)
    {
        // Create the new node.
        Node newNode = new Node(MaxNodeChildrenNumber / 2);
        // Set the number of children in to half of the Maximum of Children(B tree)
        node.numberOfChildren = MaxNodeChildrenNumber / 2;
        // for each child in the new node.
        for (int j = 0; j < MaxNodeChildrenNumber / 2; j++)
        {
            // put the old node's bigger half children as new node's children
            newNode.children[j] = node.children[MaxNodeChildrenNumber / 2 + j];
        }
        // return new node.
        return newNode;
    }

    // comparison functions - make Comparable instead of Key to avoid casts
    // is k1 < k2? true : false
    private boolean KeySmaller (Comparable k1, Comparable k2)
    {
        return k1.compareTo(k2) < 0;
    }

    // is k1 == k2? true : false
    private boolean keyEqual (Comparable k1, Comparable k2)
    {
        return k1.compareTo(k2) == 0;
    }

    // public pre-order to file(recursive entry)
    public void toFile (Path path)
    {
        this.toFile(root, TreeHight, "", path);
    }

    // private pre-order to file(recursive)
    private void toFile (Node node, int treeHight, String indent, Path path)
    {
        // Set file object
        File file = new File(path.toAbsolutePath().toString());
        // if file exist, delete it and try to recreate
        if (file.exists())
        {
            file.delete();
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        // set file reader.
        FileWriter fw = null;
        // try to write file
        try
        {
            fw = new FileWriter(path.toAbsolutePath().toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        // get node's children
        List[] children = node.children;

        // for external node.
        if (treeHight == 0)
        {
            // for each child of the node
            for (int j = 0; j < node.numberOfChildren; j++)
            {
                // try to write the child's key and value in node.
                try
                {
                    fw.write(indent + children[j].key + " " + children[j].value + "\n");
                    // close file to avoid multi write since it's recursive.
                    fw.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        // for internal node.
        else
        {
            // for each child in node.
            for (int j = 0; j < node.numberOfChildren; j++)
            {
                // Skip the first child.
                if (j > 0)
                {
                    // write the child's key and value with indent.
                    try
                    {
                        fw.write(indent + "(" + children[j].key + ")\n");
                        // close file to avoid multi write since it's recursive.
                        fw.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                toFile(children[j].next, treeHight - 1, indent + "     ", path);
            }
        }
    }

    // public In-order to file(recursive entry)
    public void InOrderSearchToFile (Path path)
    {
        this.InOrderSearchToFile(root, TreeHight, "", path);
    }

    // private In-order to file(recursive)
    private void InOrderSearchToFile (Node node, int treeHight, String indent, Path path)
    {
        // Set file object
        File f = new File(path.toAbsolutePath().toString());
        // if file exist, delete it and try to recreate
        if (f.exists())
        {
            f.delete();
            try
            {
                f.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        // set file reader.
        FileWriter fw = null;
        // try to write file
        try
        {
            fw = new FileWriter(path.toAbsolutePath().toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        // get node's children.
        List[] children = node.children;
        String s = "";

        // if the node is an external(leaf) node
        if (treeHight == 0)
        {
            // for each child
            for (int i = 0; i < node.numberOfChildren; i++)
            {
                // try to write the child's key and value in node.
                try
                {
                    s = indent + children[i].key + " " + children[i].value + "\n";
                    fw.write(indent + children[i].key + " " + children[i].value + "\n");
                    // close file to avoid multi write since it's recursive.
                    fw.close();
                    System.out.println(s);
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        // if the node is an internal node
        else
        {
            // for each possible child in node
            for (int i = 0; i < node.numberOfChildren; i++)
            {
                // In-order search.
                InOrderSearchToFile(children[i].next, treeHight - 1, indent + "     ", path);
                // Skip the first child.
                if (i > 0)
                {
                    // write the child's key and value with indent.
                    try
                    {
                        s = indent + "(" + children[i].key + ")\n";
                        fw.write(indent + "(" + children[i].key + ")\n");
                        // close file to avoid multi write since it's recursive.
                        fw.close();
                        System.out.println(s);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String toString ()
    {
        return toString(root, TreeHight, "") + "\n";
    }

    private ArrayList<String> InorderSearchToArrayList (Node node, int treeHight, String indent)
    {
        ArrayList<String> as = new ArrayList<String>();
        String s = "";
        List[] children = node.children;
        // if the node is an external(leaf) node
        if (treeHight == 0)
        {
            // for each child
            for (int i = 0; i < node.numberOfChildren; i++)
            {
                // write the child's key and value in node.
                s += indent + children[i].key + " " + children[i].value + "\n";
                as.add(s);
            }
        }
        // if the node is an internal node
        else
        {
            // for each possible child in node
            for (int i = 0; i < node.numberOfChildren; i++)
            {
                // In-order search.
                as.addAll(InorderSearchToArrayList(children[i].next, treeHight - 1, indent + "     "));
                s += toString(children[i].next, treeHight - 1, indent + "     ");
                // Skip the first child.
                if (i > 0)
                {
                    // write the child's key and value with indent.
                    s += indent + "(" + children[i].key + ")\n";
                    as.add(s);
                }
            }
        }
        return as;
    }

    private String toString (Node node, int treeHight, String indent)
    {
        String s = "";
        List[] children = node.children;
        // if the node is an external(leaf) node
        if (treeHight == 0)
        {
            // for each child
            for (int i = 0; i < node.numberOfChildren; i++)
            {
                // write the child's key and value in node.
                s += indent + children[i].key + " " + children[i].value + "\n";
            }
        }
        // if the node is an internal node
        else
        {
            // for each possible child in node
            for (int i = 0; i < node.numberOfChildren; i++)
            {
                // In-order search.
                s += toString(children[i].next, treeHight - 1, indent + "  ");
                // Skip the first child.
                if (i > 0)
                {
                    // write the child's key and value with indent.
                    s += indent + "(" + children[i].key + ")\n";
                }
            }
        }
        return s;
    }

    public static void main (String[] args)
    {
        B_tree<String, String> bTree = new B_tree<String, String>();
        Path ChIP_seqPath = Paths.get("./src/Data/ChIP-seq-reads-1M.dat");
        Path Data = Paths.get("./src/Data/in-order-result.dat");
        File ChIP_seqFile = null;
        Scanner scanner = null;
        try
        {
            // get ChIP_seq file
            ChIP_seqFile = new File(ChIP_seqPath.toAbsolutePath().toString());
            // initial Scanner of directed graph file
            scanner = new Scanner(ChIP_seqFile);
            int i = 0;
            String s = "";
            while (scanner.hasNextLine() && i < 100)
            {
                s = scanner.next("\\S{36}");
                bTree.put(String.valueOf(i), s);
                i++;
            }
        } catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }
        String result = bTree.toString();
        System.out.println(result);
    }
}



