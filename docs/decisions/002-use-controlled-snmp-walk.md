\# ADR 002: Use Direct SNMP GET for Collection and Controlled GETNEXT for Diagnostic Walks



\## Status



Accepted



\## Context



PrinterAgent needs to retrieve printer information through SNMP.



Two different SNMP use cases were identified during the prototype:



1\. normal production collection of known printer values;

2\. diagnostic discovery of unknown OIDs or printer behavior.



These use cases have different requirements.



For normal collection, PrinterAgent usually knows the exact OIDs required for values such as:



\- serial number;

\- total page counter;

\- toner information;

\- drum information.



For diagnostics and support of new printer models, the application may need to inspect an SNMP subtree and discover available OIDs.



During the prototype, both direct SNMP GET operations and SNMP Walk approaches were evaluated.



\---



\## Decision



PrinterAgent will use:



\- direct SNMP GET operations for normal production collection when the OID is already known;

\- controlled sequential SNMP GETNEXT operations for diagnostic subtree walking.



SNMP Walk will not be used as the default mechanism for regular scheduled collection.



\---



\## Rationale



\### Direct GET for known values



When the application already knows the required OID, a direct GET is simpler and more predictable.



Conceptually:



known OID

&#x20;   |

&#x20;   v

SNMP GET

&#x20;   |

&#x20;   v

value



Advantages include:



\- fewer SNMP requests;

\- reduced network traffic;

\- simpler error handling;

\- faster collection;

\- less dependency on printer-specific subtree behavior.



For example, the total page counter of the Brother printer validated during the prototype was available through a known Printer-MIB OID.



Therefore, walking the entire Printer-MIB tree during every scheduled collection would be unnecessary.



\---



\## Controlled GETNEXT Walk



A diagnostic SNMP Walk is implemented as a controlled sequence of GETNEXT requests.



Conceptually:



root OID

&#x20;   |

&#x20;   v

GETNEXT

&#x20;   |

&#x20;   v

next OID/value

&#x20;   |

&#x20;   v

GETNEXT

&#x20;   |

&#x20;   v

next OID/value

&#x20;   |

&#x20;   v

repeat while inside subtree



The walk must stop when the returned OID no longer belongs to the requested subtree.



\---



\## Prototype Finding: GET vs GETNEXT



During the prototype, a PDU was initially configured as GETNEXT but sent using:



```java

snmp.get(...)

