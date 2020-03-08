package com.company;

import graphs.*;

import java.io.*;
// import java.nio.charset.Charset; // Useless
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Maxi Agrippa
 */
public class DataFilter
{
    // source file info
    private static final Path LargeDGPath = Paths.get("./src/Data/largeDG.txt");
    private static final Path LargeEWGPath = Paths.get("./src/Data/largeEWG.txt");
    private static final Path MoviesPath = Paths.get("./src/Data/movies.txt");
    private static final Path ChIP_seqPath = Paths.get("./src/Data/ChIP-seq-reads-1M.dat");
    private static final Path DataPath = Paths.get("./src/Data/");

    // private Charset charset = Charset.forName("US-ASCII"); // Useless

    // source files
    private File dgFile = null;
    private File ewgFile = null;
    private File moviesFile = null;
    private File ChIP_seqFile = null;

    // vertex count of direct graph
    private static int DGVertexCount = 0;
    // edge count of direct graph
    private static int DGEdgeCount = 0;
    // vertex count of edge weighted graph
    private static int EWGVertexCount = 0;
    // edge count of edge weighted graph
    private static int EWGEdgeCount = 0;
    // vertex count of Indirect graph
    private static int IDGVertexCount = 0;
    // edge count of indirect graph
    private static int IDGEdgeCount = 0;
    // vertex count of Indirect Symbol graph
    private static int IDSGVertexCount = 0;
    // edge count of indirect Symbol graph
    private static int IDSGEdgeCount = 0;


    // direct graph
    private Digraph digraph = null;
    // edge weighted direct graph
    private EdgeWeightedDigraph edgeWeightedDigraph = null;
    // edge weighted indirect graph
    private EdgeWeightedGraph edgeWeightedGraph = null;
    // indirect graph
    private Graph graph = null;
    // indirect Symbol graph
    private SymbolGraph symbolGraph = null;

    // ChIP_seq Data
    private ArrayList<String> ChIP_seq = null;

    /**
     * constructor that initial the two graphs
     */
    public DataFilter ()
    {
        // edge start vertex
        int vertex01 = 0;
        // edge end vertex
        int vertex02 = 0;
        // edge weight
        double weight = 0;
        // Scanner to read int in file
        Scanner scanner = null;

        // Initialize directed graph
        try
        {
            // get directed graph file
            dgFile = new File(LargeDGPath.toAbsolutePath().toString());
            // initial Scanner of directed graph file
            scanner = new Scanner(dgFile);
            // get vertex count
            DGVertexCount = scanner.nextInt();
            // get edge count
            DGEdgeCount = scanner.nextInt();
            // initial directed graph
            digraph = new Digraph(DGVertexCount);
            // fill the graph
            while (scanner.hasNextInt())
            {
                vertex01 = scanner.nextInt();
                vertex02 = scanner.nextInt();
                digraph.addEdge(vertex01, vertex02);
            }
            // Check if edge count is correct
            if (digraph.E() != DGEdgeCount)
            {
                System.err.println("digraph.E() != DGEdgeCount");
            }
        } catch (IOException e)
        {
            System.err.format("IOException: %s%n", e);
        }

        vertex01 = 0;
        vertex02 = 0;
        scanner = null;

        // initialize directed and indirect edge weighted graph
        try
        {
            // get directed graph file
            ewgFile = new File(LargeEWGPath.toAbsolutePath().toString());
            // initial Scanner of directed graph file
            scanner = new Scanner(ewgFile);
            // get vertex count
            EWGVertexCount = scanner.nextInt();
            // get edge count
            EWGEdgeCount = scanner.nextInt();
            // initial edge weighted directed graph
            edgeWeightedDigraph = new EdgeWeightedDigraph(EWGVertexCount);
            // initial edge weighted indirect graph
            edgeWeightedGraph = new EdgeWeightedGraph(EWGVertexCount);
            // fill the graph
            while (scanner.hasNextDouble())
            {
                vertex01 = scanner.nextInt();
                vertex02 = scanner.nextInt();
                weight = scanner.nextDouble();
                edgeWeightedDigraph.addEdge(new DirectedEdge(vertex01, vertex02, weight));
                edgeWeightedGraph.addEdge(new Edge(vertex01, vertex02, weight));
            }
            // Check if edge count is correct
            if (edgeWeightedDigraph.E() != EWGEdgeCount)
            {
                System.err.println("edgeWeightedDigraph.E() != EWGEdgeCount");
                // FIXME: Need a way to check the edgeWeightedGraph
            }
        } catch (IOException e)
        {
            System.err.format("IOException: %s%n", e);
        }


        vertex01 = 0;
        vertex02 = 0;
        scanner = null;

        // initialize movie symbol graph
        try
        {
            // get movies indirected graph file
            moviesFile = new File(MoviesPath.toAbsolutePath().toString());
            // Using symbolGraph to get connected compounds graph
            symbolGraph = new SymbolGraph(MoviesPath.toAbsolutePath().toString(), "/");
            // Using symbol graph to initialize indirect graph
            graph = new Graph(symbolGraph.G());
            // get vertex count
            IDGVertexCount = graph.V();
            // get edge count
            IDGEdgeCount = graph.E();
        } catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }

        scanner = null;

        try
        {
            // get ChIP_seq file
            ChIP_seqFile = new File(ChIP_seqPath.toAbsolutePath().toString());
            // initial Scanner of directed graph file
            scanner = new Scanner(ChIP_seqFile);
            // initial Chip_seq
            ChIP_seq = new ArrayList<String>();
            String s = "";
            while (scanner.hasNextLine())
            {
                s = scanner.nextLine();
                s.replace(" ", "");
                ChIP_seq.add(s);
            }
        } catch (Exception e)
        {
            System.err.format("IOException: %s%n", e);
        }
    }


    /**
     * Get Direct Graph
     *
     * @return
     */
    public Digraph getDigraph ()
    {
        return digraph;
    }

    /**
     * Get Edge Weighted Direct Graph
     *
     * @return
     */
    public EdgeWeightedDigraph getEdgeWeightedDigraph ()
    {
        return edgeWeightedDigraph;
    }

    /**
     * Get Edge Weighted Indirect Graph
     *
     * @return
     */
    public EdgeWeightedGraph getEdgeWeightedGraph ()
    {
        return edgeWeightedGraph;
    }

    /**
     * Get Indirect Graph
     *
     * @return
     */
    public Graph getGraph ()
    {
        return graph;
    }

    /**
     * Get Indirect Symbol Graph
     *
     * @return
     */
    public SymbolGraph getSymbolGraph ()
    {
        return symbolGraph;
    }

    /**
     * Get ChIP_seq File
     * @return
     */
    public File getChIP_seqFile ()
    {
        return ChIP_seqFile;
    }

    /**
     * Get ChIP_seq ArrayList<String>
     * @return
     */
    public ArrayList<String> getChIP_seq ()
    {
        return ChIP_seq;
    }

    /**
     * Get Data Path
     * @return
     */
    public Path getDataPath ()
    {
        return DataPath;
    }
}
