package ch.ohne.dachschaden.client.gvb;

import ch.ohne.dachschaden.client.Severity;

public class Danger {
    private final String name;
    private final Severity severity;
    private final String details;

    public Danger(String name, Severity severity, String details) {
        this.name = name;
        this.severity = severity;
        this.details = details;
    }
    public String getName() { return name; }
    public Severity getSeverity() { return severity; }
    public String getDetails() { return details; }
    @Override public String toString() {
        return name;
    }
}
