package graphs;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/*************************************************************************
 *  Compilation:  javac BTree.java
 *  Execution:    java BTree
 *
 *  B-tree.
 *
 *  Limitations
 *  -----------
 *   -  Assumes M is even and M >= 4
 *   -  should b be an array of children or list (it would help with
 *      casting to make it a list)
 *
 *************************************************************************/


public class BTree<Key extends Comparable<Key>, Value>
{
    private static final int M = 4;    // max children per B-tree node = M-1

    private Node root;             // root of the B-tree
    private int HT;                // height of the B-tree
    private int N;                 // number of key-value pairs in the B-tree

    // helper B-tree node data type
    private static final class Node
    {
        private int m;                             // number of children
        private Entry[] children = new Entry[M];   // the array of children

        private Node (int k)
        {
            m = k;
        }             // create a node with k children
    }

    // internal nodes: only use key and next
    // external nodes: only use key and value
    private static class Entry
    {
        private Comparable key;
        private Object value;
        private Node next;     // helper field to iterate over array entries

        public Entry (Comparable key, Object value, Node next)
        {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    // constructor
    public BTree ()
    {
        root = new Node(0);
    }

    // return number of key-value pairs in the B-tree
    public int size ()
    {
        return N;
    }

    // return height of B-tree
    public int height ()
    {
        return HT;
    }


    // search for given key, return associated value; return null if no such key
    public Value get (Key key)
    {
        return search(root, key, HT);
    }

    private Value search (Node x, Key key, int ht)
    {
        Entry[] children = x.children;

        // external node
        if (ht == 0)
        {
            for (int j = 0; j < x.m; j++)
            {
                if (eq(key, children[j].key)) return (Value) children[j].value;
            }
        }

        // internal node
        else
        {
            for (int j = 0; j < x.m; j++)
            {
                if (j + 1 == x.m || less(key, children[j + 1].key)) return search(children[j].next, key, ht - 1);
            }
        }
        return null;
    }


    // insert key-value pair
    // add code to check for duplicate keys
    public void put (Key key, Value value)
    {
        Node u = insert(root, key, value, HT);
        N++;
        if (u == null) return;

        // need to split root
        Node t = new Node(2);
        t.children[0] = new Entry(root.children[0].key, null, root);
        t.children[1] = new Entry(u.children[0].key, null, u);
        root = t;
        HT++;
    }


    private Node insert (Node h, Key key, Value value, int ht)
    {
        int j;
        Entry t = new Entry(key, value, null);

        // external node
        if (ht == 0)
        {
            for (j = 0; j < h.m; j++)
            {
                if (less(key, h.children[j].key)) break;
            }
        }

        // internal node
        else
        {
            for (j = 0; j < h.m; j++)
            {
                if ((j + 1 == h.m) || less(key, h.children[j + 1].key))
                {
                    Node u = insert(h.children[j++].next, key, value, ht - 1);
                    if (u == null) return null;
                    t.key = u.children[0].key;
                    t.next = u;
                    break;
                }
            }
        }

        for (int i = h.m; i > j; i--) h.children[i] = h.children[i - 1];
        h.children[j] = t;
        h.m++;
        if (h.m < M) return null;
        else return split(h);
    }

    // split node in half
    private Node split (Node h)
    {
        Node t = new Node(M / 2);
        h.m = M / 2;
        for (int j = 0; j < M / 2; j++)
            t.children[j] = h.children[M / 2 + j];
        return t;
    }

    // for debugging
    public String toString ()
    {
        return toString(root, HT, "") + "\n";
    }

    private String toString (Node h, int ht, String indent)
    {
        String s = "";
        Entry[] children = h.children;

        if (ht == 0)
        {
            for (int j = 0; j < h.m; j++)
            {
                s += indent + children[j].key + " " + children[j].value + "\n";
            }
        }
        else
        {
            for (int j = 0; j < h.m; j++)
            {
                if (j > 0)
                {
                    s += indent + "(" + children[j].key + ")\n";
                }
                s += toString(children[j].next, ht - 1, indent + "     ");
            }
        }
        return s;
    }

    public void toFile (Path path)
    {
        this.toFile(root, HT, "", path);
    }

    private void toFile (Node h, int ht, String indent, Path path)
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
        FileWriter fw = null;
        // try to write file
        try
        {
            fw = new FileWriter(path.toAbsolutePath().toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Entry[] children = h.children;

        if (ht == 0)
        {
            for (int j = 0; j < h.m; j++)
            {
                // try to write file
                try
                {
                    fw.write(indent + children[j].key + " " + children[j].value + "\n");
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            for (int j = 0; j < h.m; j++)
            {
                if (j > 0)
                {
                    try
                    {
                        fw.write(indent + "(" + children[j].key + ")\n");
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                toFile(children[j].next, ht - 1, indent + "     ", path);
            }
        }
    }


    // comparison functions - make Comparable instead of Key to avoid casts
    private boolean less (Comparable k1, Comparable k2)
    {
        return k1.compareTo(k2) < 0;
    }

    private boolean eq (Comparable k1, Comparable k2)
    {
        return k1.compareTo(k2) == 0;
    }


    /*************************************************************************
     *  test client
     *************************************************************************/
    public static void main (String[] args)
    {

        BTree<String, String> st = new BTree<String, String>();

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
            while (scanner.hasNextLine() && i < 10)
            {
                //scanner.next("\\[....................................]");
                s = scanner.next("\\S{36}");
                System.out.println(s);
                st.put(String.valueOf(i), s);
                i++;
            }
        } catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }


        /*
        st.put("www.uwindsor.ca", "137.207.71.197");
        st.put("www.cs.princeton.edu", "128.112.136.11");
        st.put("www.princeton.edu", "128.112.128.15");
        st.put("www.yale.edu", "130.132.143.21");
        st.put("www.simpsons.com", "209.052.165.60");
        st.put("www.apple.com", "17.112.152.32");
        st.put("www.amazon.com", "207.171.182.16");
        st.put("www.ebay.com", "66.135.192.87");
        st.put("www.cnn.com", "64.236.16.20");
        st.put("www.google.com", "216.239.41.99");
        st.put("www.nytimes.com", "199.239.136.200");
        st.put("www.microsoft.com", "207.126.99.140");
        st.put("www.dell.com", "143.166.224.230");
        st.put("www.slashdot.org", "66.35.250.151");
        st.put("www.espn.com", "199.181.135.201");
        st.put("www.weather.com", "63.111.66.11");
        st.put("www.yahoo.com", "216.109.118.65");

        StdOut.println("uwindsor.ca:       " + st.get("www.uwindsor.ca"));
        StdOut.println("cs.princeton.edu:  " + st.get("www.cs.princeton.edu"));
        StdOut.println("hardvardscam.com: " + st.get("www.harvardscam.com"));
        StdOut.println("simpsons.com:      " + st.get("www.simpsons.com"));
        StdOut.println("apple.com:         " + st.get("www.apple.com"));
        StdOut.println("ebay.com:          " + st.get("www.ebay.com"));
        StdOut.println("dell.com:          " + st.get("www.dell.com"));

         */
        StdOut.println();

        System.out.println(st);
        //StdOut.println(st);
        StdOut.println("size:    " + st.size());
        StdOut.println("height:  " + st.height());


        FileWriter fw = null;
        try
        {
            fw = new FileWriter(Data.toAbsolutePath().toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            fw.write(st.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
