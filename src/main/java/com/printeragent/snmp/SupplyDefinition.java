package com.printeragent.snmp;

public class SupplyDefinition {

    private String descriptionOid;
    private String capacityOid;
    private String levelOid;

    public SupplyDefinition(
            String descriptionOid,
            String capacityOid,
            String levelOid) {

        this.descriptionOid = descriptionOid;
        this.capacityOid = capacityOid;
        this.levelOid = levelOid;
    }

    public String getDescriptionOid() {
        return descriptionOid;
    }

    public String getCapacityOid() {
        return capacityOid;
    }

    public String getLevelOid() {
        return levelOid;
    }
}



