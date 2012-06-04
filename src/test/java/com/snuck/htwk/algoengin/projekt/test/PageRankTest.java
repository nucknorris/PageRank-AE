package com.snuck.htwk.algoengin.projekt.test;

import java.util.Date;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.snuck.htwk.algoengin.projekt.GraphGenerator;
import com.snuck.htwk.algoengin.projekt.PageRanking;

import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.Graph;

public class PageRankTest {
	public static void main(String[] args) {
		System.out.println("start: " + new Date());
		GraphGenerator generator = new GraphGenerator();
		String testString = "http://dbpedia.org/resource/Wikipedia";
		// String testString = "http://dbpedia.org/resource/Jimmy_Wales";

		PageRanking ranking = new PageRanking(generator);
		System.out.println("own ranking: " + ranking.rank(testString));
		System.out.println("rank: " + rank(generator.getGraph(), testString));

		String[] foo =
		        generator.getIncomingPageLinks(testString);
		System.out.println("number of incomingPageLinks: " + foo.length);
		// for (String string : foo) {
		// System.out.println(string);
		// }

		String[] bar =
		        generator.getOutgoingPageLinks(testString);
		System.out.println("number of outgoingPageLinks: " + bar.length);
		// for (String string : bar) {
		// System.out.println(string);
		// }
		System.out.println("end: " + new Date());

	}

	public static double rank(Graph<RDFNode, Statement> g, String testString) {
		PageRank<RDFNode, Statement> rank = new PageRank<RDFNode, Statement>(g, 0.85);
		rank.evaluate();
		Model model = ModelFactory.createDefaultModel();
		Resource resource = model.createResource(testString);
		Node node = resource.asNode();
		RDFNode rdfnode = model.asRDFNode(node);
		if (rank.done()) {
			System.out.println("fertig: ");
		}
		return (rank.getVertexScore(rdfnode));
	}
}
