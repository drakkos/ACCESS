package accessframework;

public interface InvokeablePlugin {
    // Called when a user attempts to invoke it.
    abstract void invokePlugin();
    abstract boolean displayButton();
}