package dhbw.fileconverter;

public class ProcessStep {
    private final IModule<?> module;
    private final String[] parameters;

    public ProcessStep(IModule<?> module, String[] parameters) {
        this.module = module;
        this.parameters = parameters;
    }

    public IModule<?> getModule() {
        return module;
    }

    public String[] getParameters() {
        return parameters;
    }
}
