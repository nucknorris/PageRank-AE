package com.snuck.htwk.algoengin.projekt.test;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.snuck.htwk.algoengin.projekt.PageRanking;
import com.snuck.htwk.algoengin.projekt.SampleGraphGenerator;
import com.snuck.htwk.algoengin.projekt.SimplePageRanking;

@SuppressWarnings("deprecation")
@AxisRange(min = 0, max = .1)
@BenchmarkMethodChart(filePrefix = "complete-benchmark")
public class SimplePageRankTest {

	@Rule
	public MethodRule	benchmarkRun	= new BenchmarkRule();

	@BeforeClass
	public static void prepare() {
	}

	@BenchmarkOptions(benchmarkRounds = 1, warmupRounds = 0)
	@Test
	public void startTest() {
		SampleGraphGenerator generator;
		generator = new SampleGraphGenerator();
		SimplePageRanking ranking = new SimplePageRanking(generator);
		ranking.rank("A");
	}

	public static void main(String[] args) {
		SampleGraphGenerator generator = new SampleGraphGenerator();
		generator.visualizeGraph();
	}
}
