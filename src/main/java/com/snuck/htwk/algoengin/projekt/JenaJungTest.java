package com.snuck.htwk.algoengin.projekt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.rootdev.jenajung.JenaJungGraph;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;

public class JenaJungTest {
	public static void main(String[] args) {
		String resource = "file:///Users/sebastian/Downloads/page_yo8.rdf";
		Model model = FileManager.get().loadModel(resource);

		// vertices: RDFNodes, edges: Statements
		Graph<RDFNode, Statement> g = new JenaJungGraph(model);
		System.out.println(g.getEdgeCount());

		List<RDFNode> listOfBla = new ArrayList<RDFNode>();
		Collection<RDFNode> collectionOfVertices = g.getVertices();
		System.out.println("number of vertices:" + collectionOfVertices.size());
		for (RDFNode node : collectionOfVertices) {
			listOfBla.add(node);
		}
		System.out.println(listOfBla.get(0));

		// Layout<RDFNode, Statement> layout = new FRLayout<RDFNode,
		// Statement>(g);
		// layout.setSize(new Dimension(800, 800));
		// BasicVisualizationServer<RDFNode, Statement> viz =
		// new BasicVisualizationServer<RDFNode, Statement>(layout);
		// RenderContext<RDFNode, Statement> context = viz.getRenderContext();
		// context.setEdgeLabelTransformer(Transformers.EDGE); // property label
		// context.setVertexLabelTransformer(Transformers.NODE); // node label
		// viz.setPreferredSize(new Dimension(850, 850));
		// JFrame frame = new JFrame("Jena Graph");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.getContentPane().add(viz);
		// frame.pack();
		// frame.setVisible(true);
	}

	public static void rank(Graph<RDFNode, Statement> g) {
		PageRank<RDFNode, Statement> rank = new PageRank<RDFNode, Statement>(g, 0.85);
		rank.evaluate();
		Model model = ModelFactory.createDefaultModel();
		Resource resource = model.createResource("http://dbpedia.org/resource/Bahrain");
		Node node = resource.asNode();
		RDFNode rdfnode = model.asRDFNode(node);
		if (rank.done()) {
			System.out.println("fertig");
		}
		System.out.println(rank.getVertexScore(rdfnode));
	}
}
