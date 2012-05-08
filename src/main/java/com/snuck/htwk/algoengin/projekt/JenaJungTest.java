package com.snuck.htwk.algoengin.projekt;

import java.awt.Dimension;

import javax.swing.JFrame;

import net.rootdev.jenajung.JenaJungGraph;
import net.rootdev.jenajung.Transformers;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.RenderContext;

public class JenaJungTest {
	public static void main(String[] args) {
		Model model = FileManager.get().loadModel("file:///Users/sebastian/Downloads/page.rdf");
		// Model model = FileManager.get().loadModel(
		// "http://de.dbpedia.org/data/Abl%C3%B6se.rdf");

		// vertices: RDFNodes, edges: Statements
		Graph<RDFNode, Statement> g = new JenaJungGraph(model);

		PageRank<RDFNode, Statement> rank = new PageRank<RDFNode, Statement>(g, 0.5);
		rank.evaluate();
		if (rank.done()) {
			System.out.println("fertig");
		}
		// System.out.println(g.getVertexCount());

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
}
