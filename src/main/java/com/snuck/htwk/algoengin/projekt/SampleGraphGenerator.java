package com.snuck.htwk.algoengin.projekt;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * @author Sebastian Nuck
 * 
 */
public class SampleGraphGenerator {
	private Graph<String, String>	graph;

	public SampleGraphGenerator() {
		graph = new DirectedSparseMultigraph<String, String>();
		// graph.addVertex((String) "A");
		// graph.addVertex((String) "B");
		// graph.addVertex((String) "C");
		//
		// graph.addEdge("A-B", "A", "B");
		// graph.addEdge("A-C", "A", "C");
		// graph.addEdge("B-C", "B", "C");
		// graph.addEdge("C-A", "C", "A");

		graph.addVertex((String) "A");
		graph.addVertex((String) "B");
		graph.addVertex((String) "C");
		graph.addVertex((String) "D");
		graph.addVertex((String) "E");
		graph.addVertex((String) "F");
		graph.addVertex((String) "G");

		graph.addVertex((String) "H");
		graph.addVertex((String) "I");
		graph.addVertex((String) "J");
		graph.addVertex((String) "K");
		graph.addVertex((String) "L");
		graph.addVertex((String) "M");
		graph.addVertex((String) "N");

		graph.addEdge("A-B", "A", "B");
		graph.addEdge("A-E", "A", "E");
		graph.addEdge("A-G", "A", "G");
		graph.addEdge("B-A", "B", "A");
		graph.addEdge("B-K", "B", "K");
		graph.addEdge("B-F", "B", "F");
		graph.addEdge("B-C", "B", "C");
		graph.addEdge("E-L", "E", "L");
		graph.addEdge("E-A", "E", "A");
		graph.addEdge("C-B", "C", "B");
		graph.addEdge("D-M", "D", "M");
		graph.addEdge("D-A", "D", "A");
		graph.addEdge("D-B", "D", "B");

		graph.addEdge("H-B", "H", "B");
		graph.addEdge("H-A", "H", "A");
		graph.addEdge("I-H", "I", "H");
		graph.addEdge("I-A", "I", "A");
		graph.addEdge("K-G", "K", "G");
		graph.addEdge("K-F", "K", "F");
		graph.addEdge("K-C", "K", "C");
		graph.addEdge("L-N", "L", "N");
		graph.addEdge("M-A", "M", "A");
		graph.addEdge("C-A", "C", "A");
		graph.addEdge("M-N", "M", "N");
		graph.addEdge("M-A", "M", "A");
		graph.addEdge("N-J", "N", "J");

	}

	public void visualizeGraph() {
		Layout<String, String> layout = new CircleLayout<String, String>(graph);
		layout.setSize(new Dimension(300, 300));
		BasicVisualizationServer<String, String> server =
		        new BasicVisualizationServer<String, String>(layout);
		server.setPreferredSize(new Dimension(350, 350));
		server.getRenderContext().setVertexLabelTransformer(new
		        ToStringLabeller<String>());
		server.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

		JFrame frame = new JFrame("Simple Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(server);
		frame.pack();
		frame.setVisible(true);
	}

	public String[] getIncomingPageLinks(String vertice) {
		List<String> incomingPageLinks = new ArrayList<String>();

		Collection<String> listOfIncomingEdges = graph.getInEdges(vertice);
		for (String incomingEdge : listOfIncomingEdges) {
			Collection<String> c = graph.getIncidentVertices(incomingEdge);
			incomingPageLinks.add(cleanString(c.toString(), vertice));
		}
		String[] l = new String[incomingPageLinks.size()];
		return incomingPageLinks.toArray(l);
	}

	public String[] getOutgoingPageLinks(String vertice) {
		List<String> outgoingPageLinks = new ArrayList<String>();

		Collection<String> listOfOutgoingEdges = graph.getOutEdges(vertice);
		for (String outgoingEdge : listOfOutgoingEdges) {
			Collection<String> c = graph.getIncidentVertices(outgoingEdge);
			outgoingPageLinks.add(cleanString(c.toString(), vertice));
		}
		String[] l = new String[outgoingPageLinks.size()];
		return outgoingPageLinks.toArray(l);
	}

	private String cleanString(String string, String vertice) {
		return string.replace("[", "").replace("]", "").replace(",", "")
		        .replace(vertice, "").trim();
	}

	public Graph<String, String> getGraph() {
		return this.graph;
	}
}
