import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features/book.feature",
                 glue = "com/example/stepdefinitions")
public class RunCucumberTest {
}
