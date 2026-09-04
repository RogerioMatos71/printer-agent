\# ADR 001: Use SNMP4J for SNMP Communication



\## Status



Accepted



\## Context



PrinterAgent needs to communicate with network printers using SNMP.



During the technical prototype, SNMP communication was successfully validated using Java and SNMP4J.



The prototype demonstrated that SNMP4J can perform the operations required by the project, including:



\- SNMP GET;

\- SNMP GETNEXT;

\- controlled SNMP Walk;

\- communication with SNMP v2c devices;

\- communication over UDP;

\- timeout and retry configuration.



The implementation was validated both with an SNMP simulator and with a real Brother printer.



Because SNMP communication is a core infrastructure responsibility of PrinterAgent, the project needs a stable Java library instead of implementing the SNMP protocol manually.



\---



\## Decision



PrinterAgent will use SNMP4J as its SNMP communication library.



SNMP4J will be encapsulated behind the application-level `SnmpClient` component.



Other application components should not directly depend on SNMP4J classes unless there is a specific infrastructure reason.



Conceptually:



Application

&#x20;   |

&#x20;   v

PrinterCollectionService

&#x20;   |

&#x20;   v

SnmpClient

&#x20;   |

&#x20;   v

SNMP4J

&#x20;   |

&#x20;   v

Printer



\---



\## Rationale



SNMP4J was selected because:



\- it is a Java-native SNMP library;

\- it supports SNMP v2c;

\- it supports GET and GETNEXT operations;

\- it supports configurable timeout and retries;

\- it worked with the SNMP simulator used during development;

\- it successfully communicated with the real printer used during validation;

\- it avoids implementing SNMP packet handling manually.



Most importantly, the choice is supported by direct prototype validation rather than being only a theoretical technology selection.



\---



\## Consequences



\### Positive consequences



PrinterAgent can rely on a tested SNMP implementation.



The application can focus on:



\- printer data collection;

\- device profiles;

\- scheduling;

\- persistence;

\- API integration;

\- failure handling.



SNMP protocol details remain isolated in the infrastructure layer.



\### Negative consequences



PrinterAgent becomes dependent on SNMP4J for SNMP communication.



Library upgrades may introduce compatibility changes.



SNMP4J-specific objects must not spread across the application architecture, otherwise replacing or adapting the library later would become more difficult.



\---



\## Implementation Guidance



SNMP4J usage should remain concentrated primarily inside the SNMP infrastructure package.



For example:



```text

com.printeragent.snmp

