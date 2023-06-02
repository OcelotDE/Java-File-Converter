package dhbw.fileconverter;

import org.junit.Test;

public class JsonToXMLTest {

    @Test
    public void testJsonToXMLConversion() {

        new ModuleUtilTest().testModuleLoading();
        new ArgumentUtilTest().testArgumentLoading();
        new JSONFromTest().testJSONConverter();
        new XMLToTest().testXMLConverter();

        System.out.println("\n[Info] The JSON to XML converter pipeline has been tested successfully");
    }
}
