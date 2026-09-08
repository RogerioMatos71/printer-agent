package com.printeragent.snmp;

import com.printeragent.domain.Printer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SnmpClientIT {


    @Test
    void shouldReadOidFromSnmpSimulator() {

        Printer printer = new Printer();

        printer.setName("Impressora Simulada");
        printer.setIpAddress("127.0.0.1");
        printer.setSnmpPort(1161);
        printer.setCommunity("public");

        SnmpClient snmpClient =
            new SnmpClient();

        SnmpResult result =
            snmpClient.readOid(
                printer,
                "1.3.6.1.4.1.9999.1.1.0"
            );

        assertTrue(
            result.isSuccessful()
        );

        assertEquals(
            "15025",
            result.getValue()
        );
    }

    @Test
    void shouldReturnFailureForUnknownOid() {

    Printer printer = new Printer();

    printer.setName("Impressora Simulada");
    printer.setIpAddress("127.0.0.1");
    printer.setSnmpPort(1161);
    printer.setCommunity("public");

    SnmpClient snmpClient =
        new SnmpClient();

    SnmpResult result =
        snmpClient.readOid(
            printer,
            "1.3.6.1.4.1.9999.99.99.99"
        );

    assertFalse(
         result.isSuccessful()
    );
}

    @Test
    void shouldReturnFailureWhenPrinterDoesNotRespond() {

    Printer printer = new Printer();

    printer.setName("Impressora Inexistente");
    printer.setIpAddress("127.0.0.1");
    printer.setSnmpPort(1162);
    printer.setCommunity("public");

    SnmpClient snmpClient =
        new SnmpClient();

    SnmpResult result =
        snmpClient.readOid(
            printer,
            "1.3.6.1.4.1.9999.1.1.0"
        );

    assertFalse(
        result.isSuccessful()
    );
}

}
