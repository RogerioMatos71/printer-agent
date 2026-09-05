package com.printeragent.snmp;

public interface PrinterProfile {

      String getSerialNumberOid();

    String getTotalPageCountOid();

    SupplyDefinition getTonerDefinition();

    SupplyDefinition getDrumDefinition();
}


