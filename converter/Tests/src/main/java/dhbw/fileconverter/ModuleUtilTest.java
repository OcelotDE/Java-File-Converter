package dhbw.fileconverter;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ModuleUtilTest {
    @Test
    public void testModuleLoading() {
        // create whitebox test for the module loading
        ModuleUtil moduleUtil = ModuleUtil.GetInstance();

        // Without manually setting the converters and formatters, they should be null, since there is no modules folder
        assertNull(moduleUtil.converters);
        assertNull(moduleUtil.formatters);

        // Add two modules manually
        moduleUtil.converters = new HashMap<>();
        moduleUtil.formatters = new HashMap<>();
        moduleUtil.converters.put("json", new JsonConverter());
        moduleUtil.converters.put("xml", new XMLConverter());

        // Check if the modules were added correctly
        assertEquals(2, moduleUtil.converters.size());
        assertEquals(0, moduleUtil.formatters.size());
        assertEquals(JsonConverter.class, moduleUtil.converters.get("json").getClass());
        assertEquals(XMLConverter.class, moduleUtil.converters.get("xml").getClass());

        System.out.println("Module Util Test successful.");
    }
}
