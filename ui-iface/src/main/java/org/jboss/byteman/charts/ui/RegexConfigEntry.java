package org.jboss.byteman.charts.ui;

import java.util.regex.Pattern;

/**
 * User: alexkasko
 * Date: 10/7/15
 */
public class RegexConfigEntry extends ConfigEntryBase<String> {
    private Pattern pattern;

    public RegexConfigEntry() {
    }

    public RegexConfigEntry(String label, String defaultValue) {
        super("org.jboss.byteman.charts.ui.swing.controls.RegexFieldControl", label, defaultValue);
        pattern = Pattern.compile(defaultValue);
    }

    @Override
    public void setValue(String value) {
        super.setValue(value);
        pattern = Pattern.compile(value);
    }

    public Pattern getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("{name='").append(name).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", label='").append(label).append('\'');
        sb.append(", layoutOptions='").append(layoutOptions).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", defaultValue='").append(defaultValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
