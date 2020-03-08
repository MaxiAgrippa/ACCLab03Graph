package com.company;

import graphs.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @author Maxi Agrippa
 */
public class Main
{
    /**
     * Data Filter: read data from file and form it into graph
     */
    private static DataFilter dataFilter = null;

    /**
     * claim Directed Graph
     */
    private static Digraph digraph = null;

    /**
     * claim Edge Weighted Direct Graph
     */
    private static EdgeWeightedDigraph edgeWeightedDigraph = null;

    /**
     * claim Edge Weighted Indirect Graph
     */
    private static EdgeWeightedGraph edgeWeightedGraph = null;

    /**
     * claim Indirect Graph
     */
    private static Graph graph = null;

    /**
     * claim ChIP_seq Data
     */
    private static ArrayList<String> ChIP_seq = null;

    /**
     * Data path
     */
    private static Path dataPath = null;

    /**
     * In-order result path
     */
    private static Path InOrderResult = Paths.get("./src/Data/B-tree.dat");


    /**
     * ---------------Below is algorithm---------------
     */

    /**
     * Depth First Order algorithm on Directed Graph.
     */
    private static DepthFirstOrder depthFirstOrder = null;

    /**
     * Dijkstra Shortest Path on Edge Weighted Directed Graph
     */
    private static DijkstraSP dijkstraSP = null;

    /**
     * Kruskal Minimum Spanning Trees on Edge Weighted Indirected Graph
     */
    private static KruskalMST kruskalMST = null;

    /**
     * DFS to find all connected components on Indirect Graph
     */
    private static CC cc = null;

    /**
     * B tree for DNA and In-order print, as you want
     */
    private static B_tree<String, String> b_tree = null;


    /**
     * Main function
     *
     * @param args
     */
    public static void main (String[] args)
    {
        // For User Friendly
        System.out.println("Please be patient, drink a cup of tea. It gonna take a while.");
        System.out.println("Precessing...");

        // initial Data Filter
        dataFilter = new DataFilter();

        // initial Directed Graph
        digraph = dataFilter.getDigraph();
        // initial Edge Weighted Directed Graph
        edgeWeightedDigraph = dataFilter.getEdgeWeightedDigraph();
        // initial Edge Weighted Indirect Graph
        edgeWeightedGraph = dataFilter.getEdgeWeightedGraph();
        // initial Indirect Graph
        graph = dataFilter.getGraph();


        // initial ChIP_seq Data
        ChIP_seq = dataFilter.getChIP_seq();
        // initial Data Path
        dataPath = dataFilter.getDataPath();
        // initial B_Tree
        b_tree = new B_tree<String, String>();

        System.out.println("Initialize Finished.");

        //FORTEST
        /*
        StdOut.println(digraph);
        StdOut.println(edgeWeightedDigraph);
         */
        //FIXME: java.lang.OutOfMemoryError: Java heap space
        /*
        StdOut.println(edgeWeightedGraph);
         */
        //StdOut.println(graph);
        UIController();
    }

    /**
     * UI Controller
     */
    public static void UIController ()
    {
        // App controller
        boolean AppOn = true;
        while (AppOn)
        {
            ComputingOptimizer();
            Task01();
            Task02();
            Task03();
            Task04();
            Task05();
            Task06();
            AppOn = false;
        }
    }

    /**
     * Task 06
     */
    public static void Task06 ()
    {
        System.out.println("Task #6 B-tree Processing...");
        BTreeInOrder();
        System.out.println("Task #6 B-tree Finished.");
    }

    /**
     * B-Tree In-Order
     */
    public static void BTreeInOrder ()
    {
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        startTime = System.nanoTime();
        ChIP_seqSource();
        System.out.println(ChIP_seq.size());
        for (int i = 0; i < ChIP_seq.size(); i++)
        {
            b_tree.put(String.valueOf(i), ChIP_seq.get(i));
        }
        String s = b_tree.toString();
        File f = new File(InOrderResult.toAbsolutePath().toString());
        if (f.exists())
        {
            f.delete();
        }
        try
        {
            f.createNewFile();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        try
        {
            FileWriter fw = new FileWriter(InOrderResult.toAbsolutePath().toString());
            fw.write(s);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println(time + " BTree In-Order time nanosecond");
    }

    /**
     * Task 05
     */
    public static void Task05 ()
    {
        System.out.println("Task #5 Sort Processing...");
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        startTime = System.nanoTime();
        SortChIP_Seq();
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println(time + " Sort ChIP_Seq time nanosecond");
        System.out.println("Task #5 Sort Finished.");
    }

    /**
     * Sort ChIP_Seq
     */
    public static void SortChIP_Seq ()
    {
        // Get Data
        ChIP_seqSource();
        // Initial temp storage.
        ArrayList<String> CA = null;
        ArrayList<String> CB = null;
        ArrayList<String> CC = null;
        ArrayList<String> CD = null;
        //Noitce inclusive and exclusive in subList()
        CA = new ArrayList<String>(ChIP_seq.subList(0, ((ChIP_seq.size() / 4))));
        CB = new ArrayList<String>(ChIP_seq.subList(((ChIP_seq.size() / 4)), ((ChIP_seq.size() / 4 * 2))));
        CC = new ArrayList<String>(ChIP_seq.subList(((ChIP_seq.size() / 4 * 2)), ((ChIP_seq.size() / 4 * 3))));
        CD = new ArrayList<String>(ChIP_seq.subList(((ChIP_seq.size() / 4 * 3)), (ChIP_seq.size())));
        // Set file object
        File fa = new File(dataPath.toAbsolutePath().toString() + "/A.dat");
        File fb = new File(dataPath.toAbsolutePath().toString() + "/B.dat");
        File fc = new File(dataPath.toAbsolutePath().toString() + "/C.dat");
        File fd = new File(dataPath.toAbsolutePath().toString() + "/D.dat");
        DataWriter(CA, "/A.dat");
        DataWriter(CB, "/B.dat");
        DataWriter(CC, "/C.dat");
        DataWriter(CD, "/D.dat");
        // Sort ChIP_seq
        Collections.sort(CA);
        Collections.sort(CB);
        Collections.sort(CC);
        Collections.sort(CD);
        DataWriter(CA, "/AS.dat");
        DataWriter(CB, "/BS.dat");
        DataWriter(CC, "/CS.dat");
        DataWriter(CD, "/DS.dat");
        // Set file object
        File fas = new File(dataPath.toAbsolutePath().toString() + "/AS.dat");
        File fbs = new File(dataPath.toAbsolutePath().toString() + "/BS.dat");
        File fcs = new File(dataPath.toAbsolutePath().toString() + "/CS.dat");
        File fds = new File(dataPath.toAbsolutePath().toString() + "/DS.dat");
        // Read, merge, and sort
        ArrayList<String> result = MergeAndSort(fas, fbs, fcs, fds);
        // Store
        DataWriter(result, "/Chip-seq-reads-1M-sorted.dat");
    }

    /**
     * Read 4 Sorted File and Merge and Sort and Store
     *
     * @param file1
     * @param file2
     * @param file3
     * @param file4
     * @return
     */
    public static ArrayList<String> MergeAndSort (File file1, File file2, File file3, File file4)
    {
        Scanner scanner = null;
        ArrayList<String> result = null;
        try
        {
            // initial Scanner of directed graph file
            scanner = new Scanner(file1);
            // initial result
            result = new ArrayList<String>();
            String s = "";
            while (scanner.hasNextLine())
            {
                s = scanner.nextLine();
                s.replace(" ", "");
                result.add(s);
            }
            scanner = new Scanner(file2);
            s = "";
            while (scanner.hasNextLine())
            {
                s = scanner.nextLine();
                s.replace(" ", "");
                result.add(s);
            }
            scanner = new Scanner(file3);
            s = "";
            while (scanner.hasNextLine())
            {
                s = scanner.nextLine();
                s.replace(" ", "");
                result.add(s);
            }
            scanner = new Scanner(file4);
            s = "";
            while (scanner.hasNextLine())
            {
                s = scanner.nextLine();
                s.replace(" ", "");
                result.add(s);
            }
        } catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }
        Collections.sort(result);
        return result;
    }

    /**
     * Write Data To File
     *
     * @param arrayList
     * @param filename
     */
    public static void DataWriter (ArrayList<String> arrayList, String filename)
    {
        File f = new File(dataPath.toAbsolutePath().toString() + filename);
        try
        {
            // if file exist, delete it and try to recreate
            if (f.exists())
            {
                f.createNewFile();

            }
            FileWriter fw = new FileWriter(dataPath.toAbsolutePath().toString() + filename);
            for (String s : arrayList)
            {
                fw.write(s);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Read Data from Chip-seq-reads-1M.dat
     */
    public static void ChIP_seqSource ()
    {
        Scanner scanner = null;
        try
        {
            // get ChIP_seq file
            File ChIP_seqFile = dataFilter.getChIP_seqFile();
            // initial Scanner to read ChIP_seqFile
            scanner = new Scanner(ChIP_seqFile);
            // initial Chip_seq
            ChIP_seq = new ArrayList<String>();
            String s = "";
            while (scanner.hasNextLine())
            {
                s = scanner.next("\\S{36}");
                ChIP_seq.add(s);
            }
        } catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }
    }

    /**
     * Task 04
     */
    public static void Task04 ()
    {
        System.out.println("Task #4 Finds the movies starred by a particular actor Processing...");
        FindsTheMoviesStarredBy();
        System.out.println("Task #4 Finds the movies starred by a particular actor Finished.");
    }

    /**
     * Finds the movies and the star.
     */
    public static void FindsTheMoviesStarredBy ()
    {
        // Process controller
        boolean ProcessOn = true;
        // BufferedReader to read a line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // UserCommand
        String cmd = "";
        // Processed command collection
        ArrayList<String> processedCMDS = null;
        // Store split string
        String[] temp = null;
        // A processed command
        String processedCMD = "";
        // some key words have roman numbers followed, need to match it.
        String[] romanNumbers = {"", " (I)", " (II)", " (III)", " (XI)", " (X)", " (IX)"};
        // Error flag, when there is an invalid input, it raise.
        boolean errorFlag = true;
        // multi-stars result
        ArrayList<String> results = new ArrayList<String>();
        // result buffer
        ArrayList<String> resultsBuffer = new ArrayList<String>();
        // second result buffer
        ArrayList<String> resultBuffer02 = new ArrayList<String>();
        while (ProcessOn)
        {
            System.out.print("Enter content to check." + "\n\r" + "Enter \"exit\" to Exit." + "\n\r");
            // UserCommand
            cmd = "";
            // Processed command collection
            processedCMDS = new ArrayList<String>();
            // Store split string
            temp = null;
            // A processed command
            processedCMD = "";
            // multi-stars result
            results = new ArrayList<String>();
            // Error flag, when there is an invalid input, it raise.
            errorFlag = true;
            // result buffer
            resultsBuffer = new ArrayList<String>();
            // second result buffer
            resultBuffer02 = new ArrayList<String>();
            try
            {
                // Read cmd or content
                cmd = StdIn.readLine();
            } catch (Exception e)
            {
                System.out.println(e.toString());
            }
            // check command
            if (cmd.equals("exit"))
            {
                // Stop process
                ProcessOn = false;
            }
            else
            {
                // Start to manipulate the string we get.
                // if cmd is not empty
                if (!cmd.isEmpty())
                {
                    // split the string base on non-word characters.
                    temp = cmd.split("([^a-zA-Z]+)");
                    // if the string array is not empty and the first element is not empty,
                    if (temp.length >= 1 && (!temp[0].isEmpty()))
                    {
                        // if the string contains more than 2 elements and are not odd in number.
                        if ((temp.length >= 2) && ((temp.length % 2) == 0))
                        {
                            for (int i = 0; i < temp.length; i += 2)
                            {
                                // organize the names that need process
                                processedCMDS.add(temp[i + 1] + ", " + temp[i]);
                            }
                        }
                    }
                }
                // if there is result or results
                if (!processedCMDS.isEmpty())
                {
                    // if there is only one result
                    if (processedCMDS.size() == 1)
                    {
                        // reset error flag (just in case)
                        errorFlag = true;
                        String s = processedCMDS.get(0).toString();
                        // for different roman numbers.
                        for (String s2 : romanNumbers)
                        {
                            // reform the key with different roman numbers.
                            processedCMD = s + s2;
                            try
                            {
                                results = CheckConnectedComponents(processedCMD);
                                /*
                                int index = dataFilter.getSymbolGraph().index(processedCMD);
                                for (int point : graph.adj(index))
                                {
                                    StdOut.println("   " + dataFilter.getSymbolGraph().name(point));
                                }
                                // if reached an out put, drop the error flag.

                                 */
                                errorFlag = false;
                            } catch (Exception e)
                            {
                                // Doing nothing to avoid the java.lang.NullPointerException ERROR in the ST.java.
                                // the source code of SymbolGraph class and Graph class are serious horrible in handling error.
                            }
                        }
                    }
                    else if (processedCMDS.size() > 1)
                    {
                        // reset error flag (just in case)
                        errorFlag = true;
                        // results empty flag for first round
                        boolean emptyFlag = true;
                        // search result
                        for (String s : processedCMDS)
                        {
                            // reset error flag for a new name
                            errorFlag = true;
                            for (String s2 : romanNumbers)
                            {
                                resultBuffer02 = new ArrayList<String>();
                                processedCMD = s + s2;
                                try
                                {
                                    resultBuffer02 = CheckConnectedComponents(processedCMD);
                                    if (!resultBuffer02.isEmpty())
                                    {
                                        resultsBuffer = new ArrayList<String>(resultBuffer02);
                                    }
                                    errorFlag = false;
                                } catch (Exception e)
                                {
                                    // Doing nothing to avoid the java.lang.NullPointerException ERROR in the ST.java.
                                    // the source code of SymbolGraph class and Graph class are serious horrible in handling error.
                                }
                            }
                            // when there are no results in results
                            if (emptyFlag)
                            {
                                // direct clone from resultBuffer
                                results = new ArrayList<String>(resultsBuffer);
                                emptyFlag = false;
                            }
                            else
                            {
                                // search in the results Buffer
                                for (int i = 0; i < resultsBuffer.size(); i++)
                                {
                                    // if one of the result are not in the results,
                                    if (!results.contains(resultsBuffer.get(i)))
                                    {
                                        String t = resultsBuffer.remove(i);
                                        // resultsBuffer size - 1 due to remove()
                                        i -= 1;
                                    }
                                }
                                results = new ArrayList<String>(resultsBuffer);
                            }
                        }
                    }
                }
                if (errorFlag)
                {
                    System.out.println("A Invalid Input.");
                }
                for (String s : results)
                {
                    System.out.println(s);
                }
            }
        }
        System.out.println("Process Finished.");
    }

    /**
     * Check connected components in symbol graph.
     */
    private static ArrayList<String> CheckConnectedComponents (String item)
    {
        ArrayList<String> resultsBuffer = new ArrayList<String>();
        int index = dataFilter.getSymbolGraph().index(item);
        for (int point : graph.adj(index))
        {
            resultsBuffer.add(dataFilter.getSymbolGraph().name(point));
        }
        return resultsBuffer;
    }

    /**
     * Task 03
     */
    public static void Task03 ()
    {
        System.out.println("Task #3 Depth First Order to find all connected components Processing...");
        ConnectedComponents();
        System.out.println("Task #3 Depth First Order to find all connected components Finished.");
    }

    /**
     * Finding all connected components
     */
    public static void ConnectedComponents ()
    {
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        startTime = System.nanoTime();
        cc = new CC(graph);
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println(time + "Depth First Order to find all connected components time nanosecond");
    }

    /**
     * Task 02
     */
    public static void Task02 ()
    {
        System.out.println("Task #2 Shortest Path Processing...");
        ShortestPath();
        System.out.println("Task #2 Shortest Path Finished.");
        System.out.println("Task #2 Minimum Spanning Trees Processing...");
        MinimumSpanningTrees();
        System.out.println("Task #2 Minimum Spanning Trees Finished.");
    }

    /**
     * Finding the Shortest Path for EWG using DijkstraSP
     */
    public static void ShortestPath ()
    {
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        startTime = System.nanoTime();
        // find the shortest path to other vertex
        dijkstraSP = new DijkstraSP(edgeWeightedDigraph, 0);
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println(time + " Dijkstra Shortest Path time nanosecond");
    }

    /**
     * Finding the Minimum Spanning Trees for EWG using DijkstraSP
     */
    public static void MinimumSpanningTrees ()
    {
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        startTime = System.nanoTime();
        kruskalMST = new KruskalMST(edgeWeightedGraph);
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println(time + " kruskal Minimum Spanning Trees time nanosecond");
    }

    /**
     * Task 01
     */
    public static void Task01 ()
    {
        System.out.println("Task #1 Processing...");
        PreOrderDFS();
        System.out.println("Task #1 Finished.");
    }

    /**
     * Pre-order Depth First Search
     */
    public static void PreOrderDFS ()
    {
        long startTime = 0;
        long endTime = 0;
        long time = 0;
        startTime = System.nanoTime();
        depthFirstOrder = new DepthFirstOrder(digraph);
        endTime = System.nanoTime();
        time = endTime - startTime;
        System.out.println(time + " DepthFirstOrder time nanosecond");
    }

    /**
     * Useless loop to reduce the CPU predictor and OS optimizer
     */
    public static void ComputingOptimizer ()
    {
        System.out.println("Optimizing CPU & OS...");
        int a = 0;
        for (int i = 0; i < 10000; i++)
        {
            a++;
        }
        System.out.println("CPU & OS Optimize Finished.");
    }

}
