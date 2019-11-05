package com6441.team7.risc.controller;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * a test suite to test run all test cases
 */
@RunWith(Suite.class)
@SuiteClasses({MapLoaderControllerTest.class,
				StartupGameControllerTest.class,
				ReinforceGameControllerTest.class,
				//AttackGameControllerTest.class,
				FortifyGameControllerTest.class})
public class TestSuiteRISC {

}
