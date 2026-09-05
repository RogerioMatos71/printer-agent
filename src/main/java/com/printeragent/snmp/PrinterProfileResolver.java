package com.printeragent.snmp;

import com.printeragent.domain.Printer;

public class PrinterProfileResolver {

    public PrinterProfile resolve(Printer printer) {

        if (printer.getManufacturer() == null) {
            throw new IllegalArgumentException(
                "Fabricante da impressora não informado."
            );
        }

        String manufacturer =
            printer.getManufacturer()
                   .trim()
                   .toLowerCase();

        if (manufacturer.equals("brother")) {
            return new BrotherPrinterProfile();
        }

        throw new IllegalArgumentException(
            "Fabricante não suportado: "
            + printer.getManufacturer()
        );
    }

}
