package com6441.team7.risc.api.model;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

import com6441.team7.risc.controller.TestSuiteController;

public class TestSuiteModelRunner {

	public static void main(String[] args) {
		
		org.junit.runner.Result result=JUnitCore.runClasses(TestSuiteModel.class);
		
		for(Failure failure:result.getFailures()) {
			System.out.println(failure.toString());
		}
		
		System.out.println(result.wasSuccessful());
		
	}
	
	
}
