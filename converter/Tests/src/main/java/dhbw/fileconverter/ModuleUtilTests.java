package dhbw.fileconverter;

import dhbw.fileconverter.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ModuleUtilTests {
    @Test
    public void testModuleLoading() {
        // create whitebox tests for the module loading
        ModuleUtil moduleUtil = ModuleUtil.GetInstance();
        moduleUtil.converters = new HashMap<>();
        moduleUtil.formatters = new HashMap<>();
        moduleUtil.converters.put("json", new JsonConverter());
        moduleUtil.converters.put("xml", new XMLConverter());

        assertEquals(2, moduleUtil.converters.size());
        assertEquals(0, moduleUtil.formatters.size());
        assertEquals(JsonConverter.class, moduleUtil.converters.get("json").getClass());
        assertEquals(XMLConverter.class, moduleUtil.converters.get("xml").getClass());

        System.out.println("Test successful.");
    }
}
