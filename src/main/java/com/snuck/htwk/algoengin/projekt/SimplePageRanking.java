package com.snuck.htwk.algoengin.projekt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Jama.Matrix;

public class SimplePageRanking {
	private final double	     DAMPING_FACTOR	= 0.85;
	private List<String>	     pages	        = new ArrayList<String>();

	private SampleGraphGenerator	generator;

	public SimplePageRanking(SampleGraphGenerator generator) {
		this.generator = generator;
	}

	/*
	 * Solve the equation of ax=b, which : a is the generated matrix based on
	 * the parameter constants. x is the page ranks matrix. b is a n*1 matrix
	 * which all the values are equal to the damping factor.
	 */
	public double rank(String pageId) {
		createPageList(pageId);
		System.out.println(pages);
		Matrix a = new Matrix(generateMatrix());
		double[][] arrB = new double[pages.size()][1];
		for (int i = 0; i < pages.size(); i++) {
			arrB[i][0] = 1 - DAMPING_FACTOR;
		}

		Matrix b = new Matrix(arrB);
		// Solve the equation and get the page ranks
		Matrix x = a.solve(b);
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
			String[] inboundLinks = generator.getIncomingPageLinks(sourcePage);
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

		if (!pages.contains(page)) {
			pages.add(page);
		}

		String[] incomingPages = generator.getIncomingPageLinks(page);
		for (int i = 0; i < incomingPages.length; i++) {
			if (!pages.contains(incomingPages[i])) {
				createPageList(incomingPages[i]);
			}
		}
	}
}
