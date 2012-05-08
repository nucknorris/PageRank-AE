package com.snuck.htwk.algoengin.projekt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jama.Matrix;

public class Ranking {
	private final double	           DAMPING_FACTOR	= 0.5;
	private List<String>	           parameters	  = new ArrayList<String>();
	private static SampleDataGenerator	generator;

	public static void main(String[] args) {
		generator = new SampleDataGenerator();
		Ranking ranking = new Ranking();
		System.out.print("PR(A)= " + ranking.rank("A") + ", PR(B)=" + ranking.rank("B")
		        + ", PR(C)=" + ranking.rank("C"));
		generator.visualizeGraph();
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
			String[] inboundLinks = generator.getInboundLinks(sourceId);
			for (int i = 0; i < inboundLinks.length; i++) {
				if (inboundLinks[i].equals(linkId)) {
					return -1
					        * (DAMPING_FACTOR / generator.getOutboundLinks(linkId).length);
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
		String[] inboundPages = generator.getInboundLinks(pageId);

		// Add the inbound links to the params list and do same for inbound
		// links
		for (int i = 0; i < inboundPages.length; i++) {
			if (!parameters.contains(inboundPages[i])) {
				generateParamList(inboundPages[i]);
			}
		}
	}

	// /*
	// * Return list of the inbound links to a given page.
	// */
	// private String[] getInboundLinks(String pageId) {
	//
	// // This simulates a simple page collection
	// Map<String, String[]> map = new HashMap<String, String[]>();
	// map.put("A", new String[] { "C" });
	// map.put("B", new String[] { "A" });
	// map.put("C", new String[] { "A", "B" });
	// return (String[]) map.get(pageId);
	// }
	//
	// /*
	// * Returns list of the outbound links from a page.
	// */
	// private String[] getOutboundLinks(String pageId) {
	//
	// // This simulates a simple page collection
	// Map<String, String[]> map = new HashMap<String, String[]>();
	// map.put("A", new String[] { "B", "C" });
	// map.put("B", new String[] { "C" });
	// map.put("C", new String[] { "A", "D", "E", "F" });
	// return (String[]) map.get(pageId);
	// }
}
