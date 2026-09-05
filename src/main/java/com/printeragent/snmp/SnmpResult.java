package com.printeragent.snmp;

public class SnmpResult {


    private String oid;
    private String value;
    private boolean successful;
    private String error;

    public SnmpResult() {
    }

    public SnmpResult(
            String oid,
            String value,
            boolean successful,
            String error) {

        this.oid = oid;
        this.value = value;
        this.successful = successful;
        this.error = error;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}


