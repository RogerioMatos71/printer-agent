package com.printeragent.persistence;

import com.printeragent.domain.PrinterReading;

public interface ReadingRepository {

    void save(PrinterReading reading);
}