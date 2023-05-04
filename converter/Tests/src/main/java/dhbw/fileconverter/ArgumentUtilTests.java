package dhbw.fileconverter;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ArgumentUtilTests {
    @Test
    public void testArgumentLoading() {
        // load two modules to parse into
        ModuleUtil moduleUtil = ModuleUtil.GetInstance();
        moduleUtil.converters = new HashMap<>();
        moduleUtil.formatters = new HashMap<>();
        moduleUtil.converters.put("json", new JsonConverter());
        moduleUtil.converters.put("xml", new XMLConverter());


        // create whitebox tests for the argument loading
        String[] args = {"Examples/adresse.json", "-xml", "-json"};

        try {
            List<ProcessStep> steps = ArgumentUtil.parseArguments(args);
            assertEquals(2, steps.size());
            assertEquals(XMLConverter.class, steps.get(0).getModule().getClass());
            assertEquals(JsonConverter.class, steps.get(1).getModule().getClass());
        } catch (ArgumentException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Test successful.");
    }
}
