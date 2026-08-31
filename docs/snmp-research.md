\# SNMP Research and Prototype Findings



\## 1. Purpose



Before starting the structured implementation of PrinterAgent, a technical prototype was developed to validate SNMP communication with network printers.



The prototype was not intended to represent the final application architecture.



Its purpose was to answer technical questions such as:



\- whether printers could be queried through SNMP;

\- which SNMP operations were required;

\- which OIDs contained the required information;

\- how SNMP Walk behaved with real printers;

\- how the application should handle different SNMP responses.



The knowledge obtained from this prototype will be used as input for the new PrinterAgent architecture.



\---



\## 2. Technologies Used in the Prototype



The prototype used:



\- Java;

\- Maven;

\- SNMP4J;

\- SNMP v2c;

\- UDP;

\- SNMP Simulator;

\- Python tooling for the simulator.



The default SNMP port used with real printers was:



`161`



A different port was used during local simulator tests.



\---



\## 3. SNMP Simulator



An SNMP simulator was configured to allow development and testing without depending on a physical printer.



The simulator was used to create controlled OIDs and known values.



Example simulated values included:



\- printer identification;

\- total page counter;

\- Printer-MIB test entries.



This allowed the Java SNMP client to be tested independently from a real printer.



\---



\## 4. SNMP GET



A direct SNMP GET operation was successfully validated.



GET is appropriate when the application already knows the exact OID containing the required information.



Conceptually:



`known OID -> GET -> value`



This is the preferred approach for normal PrinterAgent data collection because it avoids walking an entire SNMP tree when the required OIDs are already known.



\---



\## 5. SNMP GETNEXT



During the prototype, an important implementation issue was identified.



The PDU was configured as GETNEXT, but the request was initially sent using:



`snmp.get(...)`



This caused unexpected behavior, including responses such as:



`noSuchInstance`



The correct SNMP4J operation for an individual GETNEXT request was validated as:



`snmp.getNext(...)`



After this correction, the real printer correctly returned the next OID.



For example, starting from:



`1.3.6.1.2.1.1.5.0`



the printer returned:



`1.3.6.1.2.1.1.6.0`



This confirmed that GETNEXT was working correctly.



\---



\## 6. SNMP Walk



Two approaches were evaluated for walking an SNMP subtree.



\### 6.1 TreeUtils



SNMP4J `TreeUtils` was tested as a higher-level mechanism for retrieving an SNMP subtree.



It worked correctly with the SNMP simulator.



However, when tested with a real Brother printer, the operation resulted in:



`Request timed out`



Increasing timeout and retry values did not solve the problem.



\### 6.2 Controlled GETNEXT Walk



A manual walk was then implemented using sequential calls to:



`snmp.getNext(...)`



The process follows this logic:



1\. start from a base OID;

2\. request the next OID;

3\. process the returned value;

4\. use the returned OID as the next starting point;

5\. repeat while the returned OID remains inside the requested subtree.



This approach worked correctly with the real printer.



The Printer-MIB subtree successfully tested was:



`1.3.6.1.2.1.43`



\---



\## 7. Printer-MIB



The standard Printer-MIB was successfully queried on a real Brother printer.



The prototype confirmed that useful printer information could be obtained from the subtree:



`1.3.6.1.2.1.43`



The following information was successfully identified.



\---



\## 8. Printer Serial Number



The printer serial number was obtained using:



`1.3.6.1.2.1.43.5.1.1.17.1`



The value returned by the test printer matched its actual serial number.



This OID corresponds to the printer serial number in the tested Printer-MIB implementation.



\---



\## 9. Printer Name



The following OID was also identified:



`1.3.6.1.2.1.43.5.1.1.16.1`



It returned a printer name/identifier.



This value must not be confused with the printer serial number.



The serial number used by PrinterAgent is obtained from the `.17.1` entry.



\---



\## 10. Total Page Counter



The total page counter was identified at:



`1.3.6.1.2.1.43.10.2.1.4.1.1`



The value returned by SNMP was manually compared with the total counter displayed by the physical printer.



The values matched.



Therefore, the prototype confirmed this OID as a valid total page counter for the tested Brother printer.



The total page counter is the primary measurement required by PrinterAgent.



\---



\## 11. Toner Information



The Printer-MIB supplies table identified an entry named:



`Black Toner Cartridge`



The tested printer exposed toner capacity and level information through the supplies table.



However, the returned values were special SNMP values rather than useful numeric levels:



\- maximum capacity: `-2`;

\- current level: `-3`.



The physical printer displayed the toner as nearly full.



Therefore, toner information shall be treated as informational and may not provide an accurate percentage on every printer.



This is also important because some consumables may be reset without being physically replaced.



\---



\## 12. Drum Information



The Printer-MIB supplies table also identified:



`Drum Unit`



The tested printer returned numeric values for the drum.



During the test:



\- maximum capacity: `50000`;

\- current level: `37033`.



These values were compared with the physical printer information and were considered consistent.



The relevant entries identified in the tested printer were:



\- drum maximum capacity:

&#x20; `1.3.6.1.2.1.43.11.1.1.8.1.2`



\- drum current level:

&#x20; `1.3.6.1.2.1.43.11.1.1.9.1.2`



\---



\## 13. Toner OIDs



For the tested printer, the toner was represented by supplies table index `1`.



The identified entries were:



\- toner maximum capacity:

&#x20; `1.3.6.1.2.1.43.11.1.1.8.1.1`



\- toner current level:

&#x20; `1.3.6.1.2.1.43.11.1.1.9.1.1`



These OIDs should not be assumed to have the same indexes on every printer.



The supplies table must be interpreted according to the device implementation.



\---



\## 14. Important Findings



The prototype produced the following conclusions:



\### Direct GET



Direct GET requests work correctly and should be preferred for normal collection when the OID is already known.



\### GETNEXT



GETNEXT works correctly when sent through the appropriate SNMP4J operation.



\### SNMP Walk



A controlled GETNEXT walk proved more reliable with the tested real printer than the SNMP4J TreeUtils implementation.



\### Printer-MIB



The standard Printer-MIB provides several of the values required by PrinterAgent.



\### Total page counter



The total page counter was successfully validated against the physical printer.



\### Serial number



The serial number was successfully obtained and validated.



\### Supplies



Drum information was useful on the tested printer.



Toner information was available but should be considered informational.



\---



\## 15. Architectural Impact



The prototype revealed several architectural requirements for the new PrinterAgent.



\### OIDs must not be stored in Main



SNMP OIDs are infrastructure/device-specific information and should not be scattered throughout the application.



\### SNMP communication must have a dedicated responsibility



The SNMP client should be responsible for protocol communication and should return results to higher-level services.



It should not be responsible for presenting information to the customer.



\### Diagnostic Walk and Production Collection are different operations



SNMP Walk is useful for:



\- diagnostics;

\- new model investigation;

\- OID discovery.



Normal scheduled collection should prefer direct GET operations for known OIDs.



\### Manufacturer differences must be expected



Different manufacturers and models may expose different OIDs, indexes or supplies information.



The architecture should allow device-specific behavior without modifying the application core.



\### SNMP responses must be validated



Responses such as:



\- `noSuchObject`;

\- `noSuchInstance`;

\- timeout;

\- special supplies values;



must not cause the entire agent to terminate.



\---



\## 16. Prototype Status



The prototype successfully demonstrated that PrinterAgent can communicate with a real network printer and retrieve the primary information required by the project.



The prototype will remain a technical reference only.



The new PrinterAgent implementation will be developed from the requirements and architecture defined in this repository.

