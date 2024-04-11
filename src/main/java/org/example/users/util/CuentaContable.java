package org.example.users.util;

public enum CuentaContable {
    CC50201("502.01"),
    CC15201("152.01"),
    CC15501("155.01"),
    CC15401("154.01"),
    CC15601("156.01"),
    CC16401("164.01"),
    CC15701("157.01"),
    CC16501("165.01"),
    CC16901("169.01"),
    CC60183("601.83"),
    CC60185("601.85"),
    CC50301("503.01");

    private String value;

    CuentaContable(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
