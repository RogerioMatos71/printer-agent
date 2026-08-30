\# PrinterAgent — Requirements



\## 1. Purpose



PrinterAgent is a background application responsible for periodically collecting information from network printers using SNMP.



The agent will run in the customer's environment and communicate with a central system responsible for consolidating collected data.



The primary information collected by the system is the printer total page counter.



\---



\## 2. Functional Requirements



\### FR-001 — Multiple printers



The system shall support multiple printers within the same customer environment.



Each printer shall contain at least:



\- name;

\- IP address;

\- SNMP port;

\- SNMP community.



\### FR-002 — Background execution



The PrinterAgent shall start automatically with the operating system and remain running in the background.



The application shall not require interaction from the customer.



\### FR-003 — Scheduled collection



The system shall perform periodic printer data collection.



The collection frequency shall be internally configurable.



Initially supported collection frequencies may include:



\- daily;

\- weekly.



This configuration shall not be exposed to the customer.



\### FR-004 — Collection history



Each collection shall be stored in a database.



Each record shall contain at least:



\- printer identification;

\- serial number;

\- total page count;

\- toner information;

\- drum information;

\- collection date and time;

\- collection status.



\### FR-005 — Monthly collection



The system shall perform a monthly measurement collection on a configurable day.



The monthly collection shall be used for measurement and billing purposes.



\### FR-006 — Monthly measurement notification



The monthly measurement shall be sent to the person responsible for validating printer readings.



Initially, the notification channel shall be e-mail.



\### FR-007 — Serial number collection



The system shall collect the printer serial number through SNMP whenever supported by the device.



\### FR-008 — Total page count collection



The system shall collect the printer total page count through SNMP.



The total page counter is considered the primary measurement collected by PrinterAgent.



\### FR-009 — Toner information



The system shall collect toner information whenever available.



Toner information shall be treated as informational because some printers may not provide accurate numeric values.



\### FR-010 — Drum information



The system shall collect drum information whenever available.



\### FR-011 — Communication failure registration



When a printer does not respond, the system shall record the communication failure.



A failure in one printer shall not interrupt the collection of other printers.



\### FR-012 — Retry on future collections



When a collection fails, the system shall retry the printer on subsequent scheduled collection cycles.



\### FR-013 — Consecutive failure alert



The system shall keep track of consecutive collection failures.



After a configurable number of consecutive failures, an alert shall be generated.



\### FR-014 — SNMP communication



The initial SNMP implementation shall support:



\- SNMP v2c;

\- UDP;

\- configurable SNMP port;

\- configurable community.



The default SNMP port is 161.



\### FR-015 — Direct OID read



The system shall provide an internal operation for reading a known OID directly.



Example conceptual operation:



`readOid(...)`



\### FR-016 — SNMP Walk diagnostic



The system shall provide a diagnostic operation capable of walking an SNMP subtree.



This functionality shall primarily be used for:



\- diagnostics;

\- new printer model analysis;

\- OID discovery.



SNMP Walk shall not be required for normal periodic data collection.



\### FR-017 — Central API integration



The PrinterAgent shall send collected information to a central system through an API.



\### FR-018 — Collection timestamp



Every collection shall record the date and time when it was performed.



\---



\## 3. Non-Functional Requirements



\### NFR-001 — No customer interaction



The application shall operate without requiring customer interaction.



\### NFR-002 — Protected configuration



Operational parameters shall not be exposed to the customer.



Examples include:



\- collection frequency;

\- retry limits;

\- monthly measurement day;

\- API configuration.



\### NFR-003 — Separation of responsibilities



Application components shall have clearly defined responsibilities.



Expected responsibilities include:



\- application initialization;

\- configuration;

\- scheduling;

\- SNMP communication;

\- printer data collection;

\- persistence;

\- API communication;

\- notifications;

\- logging.



\### NFR-004 — English code conventions



All internal code identifiers shall use English.



This includes:



\- classes;

\- methods;

\- variables;

\- packages;

\- constants;

\- internal properties.



Only customer-facing messages may use Portuguese.



\### NFR-005 — Fault tolerance



Failure while collecting data from one printer shall not interrupt the processing of other printers.



\### NFR-006 — Persistent collection history



Collection records shall survive application and operating system restarts.



\### NFR-007 — Structured logging



The production application shall use a logging framework.



`System.out.println()` shall not be used as the primary production logging mechanism.



\### NFR-008 — Maintainability



Printer OIDs and manufacturer-specific behavior shall not be scattered throughout the application source code.



\### NFR-009 — Extensibility



The architecture shall allow support for multiple printer manufacturers and models without rewriting the application core.



\### NFR-010 — Configuration security



Sensitive information shall not be hardcoded into the source code or committed to the repository.



\---



\## 4. Initial Scope



The initial implementation will focus on:



\- application configuration;

\- printer domain model;

\- SNMP communication;

\- serial number collection;

\- total page counter collection;

\- toner information;

\- drum information;

\- collection scheduling;

\- local persistence;

\- failure handling;

\- central API integration.



\---



\## 5. Out of Initial Scope



The following items are currently outside the first implementation scope:



\- graphical user interface;

\- automatic network printer discovery;

\- SNMP v3;

\- automatic application updates;

\- Windows installer;

\- dashboard.



These features may be evaluated in future versions.

