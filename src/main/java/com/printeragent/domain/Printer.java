package com.printeragent.domain;

public class Printer {

    private String id;
    private String name;
    private String ipAddress;
    private int snmpPort;
    private String community;
    private String manufacturer;
    private String model;
    private String serialNumber;

    public Printer() {
    }

    public Printer(
            String id,
            String name,
            String ipAddress,
            int snmpPort,
            String community,
            String manufacturer,
            String model,
            String serialNumber) {

        this.id = id;
        this.name = name;
        this.ipAddress = ipAddress;
        this.snmpPort = snmpPort;
        this.community = community;
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serialNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getSnmpPort() {
        return snmpPort;
    }

    public void setSnmpPort(int snmpPort) {
        this.snmpPort = snmpPort;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}



