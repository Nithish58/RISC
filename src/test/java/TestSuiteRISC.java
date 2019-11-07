import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com6441.team7.risc.api.model.TestSuiteModel;
import com6441.team7.risc.controller.AttackGameControllerTest;
import com6441.team7.risc.controller.FortifyGameControllerTest;
import com6441.team7.risc.controller.MapLoaderControllerTest;
import com6441.team7.risc.controller.ReinforceGameControllerTest;
import com6441.team7.risc.controller.StartupGameControllerTest;
import com6441.team7.risc.controller.TestSuiteController;
/**
 * Test suite to run all other test suites
 * @author Keshav
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TestSuiteModel.class, TestSuiteController.class })
public class TestSuiteRISC {

}
