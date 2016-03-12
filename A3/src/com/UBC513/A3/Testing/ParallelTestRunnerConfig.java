package com.UBC513.A3.Testing;

import junit.framework.TestSuite;

import com.google.appengine.testing.cloudcover.harness.junit3.JUnit3Config;
import com.google.appengine.testing.cloudcover.harness.junit3.JUnit3TestRun;
import com.google.appengine.testing.cloudcover.spi.TestRun;

public class ParallelTestRunnerConfig extends JUnit3Config
{

	public TestRun newTestRun(String suiteID)
	{
		TestSuite suite = new TestSuite();
		suite.addTestSuite(TestSuccessReservation.class);
		suite.addTestSuite(TestFailureReservation.class);
		suite.addTestSuite(TestSuccessWaitingList.class);
		suite.addTestSuite(TestFailureWaitingList.class);
		return new JUnit3TestRun(suite);
	}
}