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

        // load three modules: firstly json, then xml, then json again
        String[] args = {"Examples/adresse.json", "-json", "-xml", "-json"};

        // create list of process steps to fill
        List<ProcessStep> steps = null;
        try {
            // parse arguments to list of process steps
            steps = ArgumentUtil.parseArguments(args);
        } catch (ArgumentException e) {
            throw new RuntimeException(e);
        }

        // check if the list of process steps is correct
        assertEquals(3, steps.size());
        assertEquals(XMLConverter.class, steps.get(0).getModule().getClass());
        assertEquals(JsonConverter.class, steps.get(1).getModule().getClass());

        System.out.println("Test successful.");
    }
}
