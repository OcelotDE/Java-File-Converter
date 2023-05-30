package dhbw.fileconverter;

/**
 * Represents a step in the file conversion process
 */
public class ProcessStep {
    private final IModule<?> module;
    private final String[] parameters;

    /**
     * Constructs a ProcessStep object with the specified module and parameters
     *
     * @param module     The module responsible for executing the conversion step
     * @param parameters The parameters required for the conversion step
     */
    public ProcessStep(IModule<?> module, String[] parameters) {
        this.module = module;
        this.parameters = parameters;
    }

    /**
     * Retrieves the module associated with this process step.
     *
     * @return The module responsible for executing the conversion step.
     */
    public IModule<?> getModule() {
        return module;
    }

    /**
     * Retrieves the parameters associated with this process step.
     *
     * @return The parameters required for the conversion step.
     */
    public String[] getParameters() {
        return parameters;
    }
}
