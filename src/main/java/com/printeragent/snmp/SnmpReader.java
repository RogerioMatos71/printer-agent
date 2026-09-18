package com.printeragent.snmp;

import com.printeragent.domain.Printer;

public interface SnmpReader {

    SnmpResult readOid(
        Printer printer,
        String oid
    );

}
