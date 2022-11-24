import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * The test class TestRunner.
 *
 * @author David Wang (k19005122), Savraj Bassi (k19029720), Edis Emin (k1921655), Sean McGrory (k1922054)
 * @version 1.6
 */
public class TestRunner {
   public static void main(String[] args) {
      // Run tests for statistics controller
      Result result = JUnitCore.runClasses(StatisticsPaneControllerTest.class);
        
      // Output failures to console
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }

      System.out.println(result.wasSuccessful());
   }
}
