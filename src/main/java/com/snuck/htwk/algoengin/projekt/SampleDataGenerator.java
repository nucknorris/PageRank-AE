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
public class SampleDataGenerator {
	private static Graph<String, String>	graph;

	public SampleDataGenerator() {
		graph = new DirectedSparseMultigraph<String, String>();
		graph.addVertex((String) "A");
		graph.addVertex((String) "B");
		graph.addVertex((String) "C");

		graph.addEdge("A-B", "A", "B");
		graph.addEdge("A-C", "A", "C");
		graph.addEdge("B-C", "B", "C");
		graph.addEdge("C-A", "C", "A");
	}

	public void visualizeGraph(Graph<String, String> g) {
		Layout<String, String> layout = new CircleLayout<String, String>(g);
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

	public String[] getInboundLinks(String vertice) {
		List<String> inboundLinks = new ArrayList<String>();

		Collection<String> listOfInboundEdges = graph.getInEdges(vertice);
		for (String inboundEdge : listOfInboundEdges) {
			Collection<String> c = graph.getIncidentVertices(inboundEdge);
			inboundLinks.add(cleanString(c.toString(), vertice));
		}
		String[] l = new String[inboundLinks.size()];
		return inboundLinks.toArray(l);
	}

	public String[] getOutboundLinks(String vertice) {
		List<String> outboundLinks = new ArrayList<String>();

		Collection<String> listOfOutboundEdges = graph.getOutEdges(vertice);
		for (String outboundEdge : listOfOutboundEdges) {
			Collection<String> c = graph.getIncidentVertices(outboundEdge);
			outboundLinks.add(cleanString(c.toString(), vertice));
		}
		String[] l = new String[outboundLinks.size()];
		return outboundLinks.toArray(l);
	}

	private String cleanString(String string, String vertice) {
		return string.replace("[", "").replace("]", "").replace(",", "")
		        .replace(vertice, "").trim();
	}
}
