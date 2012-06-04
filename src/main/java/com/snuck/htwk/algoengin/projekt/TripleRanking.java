package com.snuck.htwk.algoengin.projekt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.rootdev.jenajung.JenaJungGraph;
import Jama.Matrix;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.util.FileManager;

import edu.uci.ics.jung.graph.Graph;

public class TripleRanking {
	private final double	                 DAMPING_FACTOR	= 0.5;
	private List<String>	                 parameters	    = new ArrayList<String>();
	private static SampleGraphGenerator	     generator;
	private static Graph<RDFNode, Statement>	graph;

	public static void main(String[] args) {
		TripleRanking ranking = new TripleRanking();
		String resource = "file:///Users/sebastian/Downloads/page_yo.rdf";
		Model model = FileManager.get().loadModel(resource);

		// vertices: RDFNodes, edges: Statements
		graph = new JenaJungGraph(model);

		System.out.println(graph.getVertexCount());
		ranking.rank("http://dbpedia.org/resource/Internet_Society");

	}

	/*
	 * Solve the equation of ax=b, which : a is the generated matrix based on
	 * the parameter constants. x is the page ranks matrix. b is a n*1 matrix
	 * which all the values are equal to the damping factor.
	 */
	public double rank(String pageId) {
		generateParamList(pageId);
		Matrix a = new Matrix(generateMatrix());
		double[][] arrB = new double[parameters.size()][1];
		for (int i = 0; i < parameters.size(); i++) {
			arrB[i][0] = 1 - DAMPING_FACTOR;
		}

		Matrix b = new Matrix(arrB);
		// Solve the equation and get the page ranks
		Matrix x = a.solve(b);
		int indicator = 0;
		int count = 0;
		for (Iterator<String> iterator = parameters.iterator(); iterator.hasNext();) {
			String currentPage = (String) iterator.next();
			if (currentPage.equals(pageId)) {
				indicator = count;
			}
			count++;
		}
		return x.getArray()[indicator][0];
	}

	/*
	 * This method generates the matrix of the linear equations. The generated
	 * matrix is a n*n matrix where n is number of the related pages.
	 */
	private double[][] generateMatrix() {
		double[][] arr = new double[parameters.size()][parameters.size()];
		for (int i = 0; i < parameters.size(); i++) {
			for (int j = 0; j < parameters.size(); j++) {
				arr[i][j] = getMultiFactor((String) parameters.get(i),
				        (String) parameters.get(j));
			}
		}
		return arr;
	}

	/*
	 * This method returns the constant of the given variable in the linear
	 * equation.
	 */
	private double getMultiFactor(String sourceId, String linkId) {
		if (sourceId.equals(linkId))
			return 1;
		else {
			String[] inboundLinks = generator.getIncomingPageLinks(sourceId);
			for (int i = 0; i < inboundLinks.length; i++) {
				if (inboundLinks[i].equals(linkId)) {
					return -1
					        * (DAMPING_FACTOR / generator.getOutgoingPageLinks(linkId).length);
				}
			}
		}
		return 0;
	}

	/*
	 * This method returns list of the related pages. This list is also the
	 * parameters in the linear equation.
	 */
	private void generateParamList(String pageId) {

		// Add the starting page.
		if (!parameters.contains(pageId)) {
			parameters.add(pageId);
		}

		// Get list of the inbound pages
		String[] inboundPages = generator.getIncomingPageLinks(pageId);

		// Add the inbound links to the params list and do same for inbound
		// links
		for (int i = 0; i < inboundPages.length; i++) {
			if (!parameters.contains(inboundPages[i])) {
				generateParamList(inboundPages[i]);
			}
		}
	}

	public String[] getInboundLinks(RDFNode vertice) {
		List<String> inboundLinks = new ArrayList<String>();

		Collection<Statement> listOfInboundEdges = graph.getInEdges(vertice);
		for (Statement inboundEdge : listOfInboundEdges) {
			Collection<RDFNode> c = graph.getIncidentVertices(inboundEdge);
			inboundLinks.add(c.toString());
		}
		String[] l = new String[inboundLinks.size()];
		return inboundLinks.toArray(l);
	}

	public String[] getOutboundLinks(RDFNode vertice) {
		List<String> outboundLinks = new ArrayList<String>();

		Collection<Statement> listOfOutboundEdges = graph.getOutEdges(vertice);
		for (Statement outboundEdge : listOfOutboundEdges) {
			Collection<RDFNode> c = graph.getIncidentVertices(outboundEdge);
			outboundLinks.add(c.toString());
		}
		String[] l = new String[outboundLinks.size()];
		return outboundLinks.toArray(l);
	}
}
