package com.printeragent.snmp;

public class BrotherPrinterProfile implements PrinterProfile{

     private static final String SERIAL_NUMBER_OID =
        "1.3.6.1.2.1.43.5.1.1.17.1";

    private static final String TOTAL_PAGE_COUNT_OID =
        "1.3.6.1.2.1.43.10.2.1.4.1.1";

    private static final SupplyDefinition TONER =
        new SupplyDefinition(
            "1.3.6.1.2.1.43.11.1.1.6.1.1",
            "1.3.6.1.2.1.43.11.1.1.8.1.1",
            "1.3.6.1.2.1.43.11.1.1.9.1.1"
        );

    private static final SupplyDefinition DRUM =
        new SupplyDefinition(
            "1.3.6.1.2.1.43.11.1.1.6.1.2",
            "1.3.6.1.2.1.43.11.1.1.8.1.2",
            "1.3.6.1.2.1.43.11.1.1.9.1.2"
        );

    @Override
    public String getSerialNumberOid() {
        return SERIAL_NUMBER_OID;
    }

    @Override
    public String getTotalPageCountOid() {
        return TOTAL_PAGE_COUNT_OID;
    }

    @Override
    public SupplyDefinition getTonerDefinition() {
        return TONER;
    }

    @Override
    public SupplyDefinition getDrumDefinition() {
        return DRUM;
    }



}
