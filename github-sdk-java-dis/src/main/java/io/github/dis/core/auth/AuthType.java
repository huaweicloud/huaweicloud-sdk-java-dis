package io.github.dis.core.auth;

public enum AuthType {
    AKSK("aksk",1),

    AUTHTOKEN("authtoken", 2);

    private String AuthType;
    private int value;


    private AuthType(String authType, int value) {
        this.AuthType = authType;
        this.value = value;
    }


    public String getAuthType() {
        return AuthType;
    }

    public int getValue() {
        return value;
    }


    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return AuthType;
    }
}
