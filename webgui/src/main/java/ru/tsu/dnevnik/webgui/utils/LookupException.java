package ru.tsu.dnevnik.webgui.utils;

public class LookupException
    extends RuntimeException {

    private String jndiName;

    public LookupException(String message, String jndiName) {
        super(message);
        this.jndiName = jndiName;
    }

    public LookupException(String message, Throwable cause) {
        super(message, cause);
        this.jndiName = null;
    }

    public LookupException(String message, Throwable cause, String jndiName) {
        super(message, cause);
        this.jndiName = jndiName;
    }

    public String getJNDIName() {
        return jndiName;
    }

    @Override
    public String getMessage() {
        if(jndiName == null)
            return super.getMessage();
        else
            return String.format("%s\nJNDI name: \"%s\"",
                    super.getMessage(), jndiName);
    }
}
