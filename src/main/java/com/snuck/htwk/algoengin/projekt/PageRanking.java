package com.snuck.htwk.algoengin.projekt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jama.Matrix;

public class PageRanking {
	private final double	DAMPING_FACTOR	= 0.85;
	private List<String>	pages	       = new ArrayList<String>();

	private GraphGenerator	generator;

	public PageRanking(GraphGenerator generator) {
		this.generator = generator;
	}

	/*
	 * Solve the equation of ax=b, which : a is the generated matrix based on
	 * the parameter constants. x is the page ranks matrix. b is a n*1 matrix
	 * which all the values are equal to the damping factor.
	 */
	public double rank(String pageId) {
		createPageList(pageId);

		// System.out.println("pages:");
		// for (String page : pages) {
		// System.out.println(page);
		// }

		Matrix a = new Matrix(generateMatrix());
		System.out.println("\nmatrix a: ");
		printMatrix(a);
		double[][] arrB = new double[pages.size()][1];
		for (int i = 0; i < pages.size(); i++) {
			arrB[i][0] = 1 - DAMPING_FACTOR;
		}

		Matrix b = new Matrix(arrB);

		System.out.println("\nmatrix b: ");
		printMatrix(b);
		// Solve the equation and get the page ranks
		System.out.println("\nsolving matrix ...");
		Matrix x = a.solve(b);

		System.out.println("\nmatrix x: ");
		printMatrix(x);

		int indicator = 0;
		int count = 0;
		for (Iterator<String> iterator = pages.iterator(); iterator.hasNext();) {
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
		double[][] matrix = new double[pages.size()][pages.size()];
		for (int i = 0; i < pages.size(); i++) {
			for (int j = 0; j < pages.size(); j++) {
				matrix[i][j] = getConstantMultiplicatorFactor((String) pages.get(i),
				        (String) pages.get(j));
			}
		}
		return matrix;
	}

	/*
	 * Now we need to calculate the constant factors of the equations. The PR of
	 * the source page itself is 1. If there are no incoming links, the PR is 0.
	 * If there are links, they are calculated by DUMPING_FACTOR / Number of
	 * outgoing links of the linked page. To get it on the left side of the
	 * matrix, it will be multiplicated with -1
	 */
	private double getConstantMultiplicatorFactor(String sourcePage, String linkPage) {
		if (sourcePage.equals(linkPage))
			return 1;
		else {
			// System.out.println(String.format("\n*******\nsourcePage: %s, \nlinkPage: %s",
			// sourcePage,
			// linkPage));
			String[] inboundLinks = generator.getIncomingPageLinks(sourcePage);
			// System.out.println("inbound links of source page: ");
			// for (String string : inboundLinks) {
			// System.out.println(string);
			// }
			for (int i = 0; i < inboundLinks.length; i++) {
				if (inboundLinks[i].equals(linkPage)) {
					return -1 * (DAMPING_FACTOR / generator.getOutgoingPageLinks(linkPage).length);
				}
			}
		}
		return 0;
	}

	/**
	 * This method creates a list of incoming pages. Additionally, all the
	 * incoming pages of the imcoming pages are detected recursively too.
	 * 
	 * @param page
	 */
	private void createPageList(String page) {
		// System.out.println("begin createPageList for " + page);

		if (!pages.contains(page)) {
			pages.add(page);
		}

		String[] incomingPages = generator.getIncomingPageLinks(page);

		// System.out.println("incoming pages of " + page + " (" +
		// incomingPages.length + "):");
		// for (String string : incomingPages) {
		// System.out.println(string);
		// }
		for (int i = 0; i < incomingPages.length; i++) {
			if (!pages.contains(incomingPages[i])) {
				createPageList(incomingPages[i]);
			}
		}
		// System.out.println("end createPageList for " + page);
	}

	private void printMatrix(Matrix matrix) {
		for (int i = 0; i < matrix.getRowDimension(); i++) {
			for (int j = 0; j < matrix.getColumnDimension(); j++) {
				System.out.println(String.format("matrix[%s][%s]: %s", i, j, matrix.get(i, j)));
			}
		}
	}
}
