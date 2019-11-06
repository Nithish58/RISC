package com6441.team7.risc.controller;

import javax.naming.spi.DirStateFactory.Result;

import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

public class TestSuiteControllerRunner {

	public static void main(String[] args) {
		
		org.junit.runner.Result result=JUnitCore.runClasses(TestSuiteController.class);
		
		for(Failure failure:result.getFailures()) {
			System.out.println(failure.toString());
		}
		
		System.out.println(result.wasSuccessful());
		
	}
	
}
