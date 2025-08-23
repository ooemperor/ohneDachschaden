package ch.ohne.dachschaden.client;

/**
 * Severity levels for dangers, each with an associated CSS color.
 *
 * Colors (muted, more neutral):
 * - NONE   = white (#ffffff)
 * - LOW    = soft yellow (#fff3b0)
 * - MEDIUM = soft orange (#ffe0b2)
 * - HIGH   = soft red (#ffcdd2)
 */
public enum Severity {
    NONE("#ffffff"),
    LOW("#fff3b0"),
    MEDIUM("#ffe0b2"),
    HIGH("#ffcdd2");

    private final String color;

    Severity(String color) {
        this.color = color;
    }

    /**
     * Returns the CSS color (hex) associated with this severity level.
     */
    public String getColor() {
        return color;
    }
}
