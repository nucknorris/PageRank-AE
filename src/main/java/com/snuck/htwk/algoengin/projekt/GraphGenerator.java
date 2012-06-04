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

import edu.uci.ics.jung.graph.Graph;

public class GraphGenerator {
	private Graph<RDFNode, Statement>	graph;

	public GraphGenerator() {
		String resource = "file:///Users/sebastian/Downloads/page.rdf";
		Model model = FileManager.get().loadModel(resource);

		// vertices: RDFNodes, edges: Statements
		graph = new JenaJungGraph(model);
		System.out.println("Number of edges: " + graph.getEdgeCount());
	}

	public String[] getOutgoingPageLinks(String vertice) {
		List<String> outgoingPageLinks = new ArrayList<String>();

		Model model = ModelFactory.createDefaultModel();
		Resource resource = model.createResource(vertice);
		Node node = resource.asNode();
		RDFNode rdfnode = model.asRDFNode(node);

		Collection<Statement> listOfOutgoingEdges = graph.getOutEdges(rdfnode);
		for (Statement statement : listOfOutgoingEdges) {
			Collection<RDFNode> c = graph.getIncidentVertices(statement);
			outgoingPageLinks.add(c.toString());
		}
		String[] l = new String[outgoingPageLinks.size()];
		return outgoingPageLinks.toArray(l);
	}

	public String[] getIncomingPageLinks(String vertice) {
		List<String> incomingPageLinks = new ArrayList<String>();

		Model model = ModelFactory.createDefaultModel();
		Resource resource = model.createResource(vertice);
		Node node = resource.asNode();
		RDFNode rdfnode = model.asRDFNode(node);

		Collection<Statement> listOfIncomingEdges = graph.getInEdges(rdfnode);
		for (Statement incomingEdge : listOfIncomingEdges) {
			Collection<RDFNode> c = graph.getIncidentVertices(incomingEdge);
			incomingPageLinks.add(cleanString(c.toString(), vertice));
		}
		String[] l = new String[incomingPageLinks.size()];
		return incomingPageLinks.toArray(l);
	}

	private String cleanString(String string, String vertice) {
		return string.replace("[", "").replace("]", "").replace(",", "")
		        .replace(vertice, "").trim();
	}

	public Graph<RDFNode, Statement> getGraph() {
		return this.graph;
	}
}
