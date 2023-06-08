package dhbw.fileconverter;

import org.junit.Test;

public class JsonToXMLTest {

    @Test
    public void testJsonToXMLConversion() {

        // this tests whether all needed components for a successful conversion function properly
        // aside from testing the actual JSON to XML converter pipeline it is mandatory to test
        // the module and argument loading first
        new ModuleUtilTest().testModuleLoading();
        new ArgumentUtilTest().testArgumentLoading();

        new JSONFromTest().testJSONConverter();
        new XMLToTest().testXMLConverter();

        System.out.println("\n[Info] The JSON to XML converter pipeline has been tested successfully");
    }
}
