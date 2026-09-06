package com.printeragent.snmp;

import com.printeragent.domain.Printer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PrinterProfileResolverTest {

    @Test
    void shouldResolveBrotherPrinterProfile() {

        Printer printer = new Printer();
        printer.setManufacturer("Brother");

        PrinterProfileResolver resolver =
            new PrinterProfileResolver();

        PrinterProfile profile =
            resolver.resolve(printer);

        assertInstanceOf(
            BrotherPrinterProfile.class,
            profile
        );
    }

    @Test
    void shouldIgnoreManufacturerCase() {

    Printer printer = new Printer();
    printer.setManufacturer("BROTHER");

    PrinterProfileResolver resolver =
        new PrinterProfileResolver();

    PrinterProfile profile =
        resolver.resolve(printer);

    assertInstanceOf(
        BrotherPrinterProfile.class,
        profile
    );
}

    @Test
    void shouldIgnoreManufacturerSurroundingSpaces() {

    Printer printer = new Printer();
    printer.setManufacturer("   Brother   ");

    PrinterProfileResolver resolver =
        new PrinterProfileResolver();

    PrinterProfile profile =
        resolver.resolve(printer);

    assertInstanceOf(
        BrotherPrinterProfile.class,
        profile
    );
}

    @Test
    void shouldRejectPrinterWithoutManufacturer() {

    Printer printer = new Printer();

    PrinterProfileResolver resolver =
        new PrinterProfileResolver();

    assertThrows(
        IllegalArgumentException.class,
        () -> resolver.resolve(printer)
    );
}

    @Test
    void shouldRejectUnsupportedManufacturer() {

    Printer printer = new Printer();
    printer.setManufacturer("HP");

    PrinterProfileResolver resolver =
        new PrinterProfileResolver();

    assertThrows(
        IllegalArgumentException.class,
        () -> resolver.resolve(printer)
    );
}

}
