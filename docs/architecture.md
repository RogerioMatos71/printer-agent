\# PrinterAgent — Initial Architecture



\## 1. Purpose



This document defines the initial architecture of PrinterAgent based on the approved requirements and the technical findings obtained during the SNMP prototype.



The objective is to establish clear responsibilities before implementation starts.



The architecture should support:



\- multiple printers per customer;

\- scheduled collections;

\- local persistence;

\- SNMP communication;

\- central API integration;

\- monthly measurement reporting;

\- failure tracking;

\- alert generation;

\- future support for multiple printer manufacturers.



\---



\## 2. Architectural Principles



The project shall follow these principles:



\### Separation of responsibilities



Each component should have a clear and limited responsibility.



\### Dependency direction



Higher-level business logic should not depend directly on implementation details whenever avoidable.



\### External configuration



Customer-specific and operational parameters must remain outside the source code.



\### Extensibility



Manufacturer-specific SNMP behavior should be isolated from the application core.



\### Fault tolerance



Failure while collecting one printer must not interrupt the collection of other printers.



\### Background execution



PrinterAgent is intended to run continuously without customer interaction.



\---



\## 3. High-Level Architecture



The initial architecture is divided into the following areas:



Application

&#x20;   |

&#x20;   +-- Configuration

&#x20;   |

&#x20;   +-- Scheduling

&#x20;   |

&#x20;   +-- Collection

&#x20;   |      |

&#x20;   |      +-- Printer profile

&#x20;   |      +-- SNMP communication

&#x20;   |

&#x20;   +-- Persistence

&#x20;   |

&#x20;   +-- Central API integration

&#x20;   |

&#x20;   +-- Notifications

&#x20;   |

&#x20;   +-- Failure tracking

&#x20;   |

&#x20;   +-- Logging



\---



\## 4. Main Components



\### PrinterAgentApplication



Responsible for application startup.



Main responsibilities:



\- initialize application configuration;

\- initialize services;

\- start scheduled processing;

\- keep the application running.



This component must not contain printer-specific OIDs or collection rules.



\---



\### ApplicationConfig



Responsible for providing internal application configuration.



Examples:



\- collection frequency;

\- monthly collection day;

\- SNMP timeout;

\- SNMP retry count;

\- central API endpoint;

\- alert threshold;

\- notification configuration.



The customer must not directly manage these operational settings.



\---



\### Printer



Represents a configured printer.



Initial information may include:



\- id;

\- name;

\- IP address;

\- SNMP port;

\- SNMP community;

\- manufacturer;

\- model;

\- serial number.



The Printer entity represents the device itself.



It must not be responsible for SNMP communication.



\---



\### PrinterReading



Represents the result of a collection performed at a specific point in time.



Possible information:



\- printer identifier;

\- serial number;

\- total page count;

\- toner information;

\- drum information;

\- collection date and time;

\- collection status;

\- failure information when applicable;

\- collection type.



A PrinterReading is historical data.



It must not overwrite previous readings.



\---



\### CollectionType



Represents the purpose of a collection.



Initial values:



\- REGULAR;

\- MONTHLY.



REGULAR represents operational daily or weekly collection.



MONTHLY represents the measurement used for monthly validation and reporting.



\---



\### SnmpClient



Responsible exclusively for SNMP protocol communication.



Expected operations may include:



\- read a known OID;

\- perform a controlled GETNEXT operation;

\- perform diagnostic subtree walking.



Examples of conceptual operations:



readOid(...)

getNext(...)

walkSubtree(...)



SnmpClient must not:



\- decide which OIDs represent a total page counter;

\- save data to the database;

\- send e-mails;

\- send data to the central API;

\- present results to the customer.



\---



\### PrinterProfile



Responsible for representing SNMP knowledge required to collect information from a printer type.



A profile may define:



\- serial number OID;

\- total page count OID;

\- toner information;

\- drum information;

\- manufacturer-specific behavior.



The purpose of PrinterProfile is to prevent OIDs from being scattered throughout the application.



Conceptually:



BrotherPrinterProfile

HPPrinterProfile

KyoceraPrinterProfile



The exact implementation strategy will be decided during detailed design.



\---



\### PrinterCollectionService



Responsible for coordinating the collection process for a printer.



Expected flow:



1\. receive the printer to collect;

2\. determine the appropriate PrinterProfile;

3\. request values through SnmpClient;

4\. validate SNMP responses;

5\. create a PrinterReading;

6\. return the collection result.



PrinterCollectionService coordinates the use case.



It should not implement low-level SNMP communication.



\---



\### CollectionScheduler



Responsible for deciding when collections should occur.



Responsibilities:



\- trigger regular collections;

\- trigger monthly collections;

\- use internally configured schedules.



The scheduler must not contain SNMP logic.



\---



\### ReadingRepository



Responsible for persistence of PrinterReading records.



Expected responsibilities:



\- save successful readings;

\- save failed collection records when appropriate;

\- query collection history;

\- support monthly reporting and synchronization.



The specific database technology will be decided later.



\---



\### CentralApiClient



Responsible for communication between PrinterAgent and the central system.



Possible responsibilities:



\- send collected readings;

\- send monthly measurements;

\- report agent status;

\- report communication failures.



The API contract will be defined in a later project phase.



\---



\### FailureTracker



Responsible for tracking consecutive collection failures.



Possible responsibilities:



\- register successful collection;

\- register failed collection;

\- reset failure count after recovery;

\- determine when the alert threshold has been reached.



Failure tracking should be independent from SNMP communication.



\---



\### NotificationService



Responsible for sending application notifications.



Initial use cases include:



\- consecutive printer communication failure alerts;

\- operational alerts.



The notification channel may initially use e-mail.



The architecture should allow other notification mechanisms in the future.



\---



\### EmailService



Responsible specifically for e-mail delivery.



Possible responsibilities:



\- monthly measurement e-mail;

\- failure alerts;

\- operational notifications.



NotificationService may use EmailService as one notification channel.



\---



\## 5. Regular Collection Flow



The expected regular collection flow is:



CollectionScheduler

&#x20;   |

&#x20;   v

PrinterCollectionService

&#x20;   |

&#x20;   +--> PrinterProfile

&#x20;   |

&#x20;   +--> SnmpClient

&#x20;   |

&#x20;   v

PrinterReading

&#x20;   |

&#x20;   +--> ReadingRepository

&#x20;   |

&#x20;   +--> CentralApiClient

&#x20;   |

&#x20;   v

FailureTracker



If collection succeeds:



\- reading is stored;

\- failure state is reset;

\- data may be synchronized with the central API.



If collection fails:



\- failure is recorded;

\- other printers continue processing;

\- FailureTracker increments the consecutive failure count;

\- an alert may be generated when the configured threshold is reached.



\---



\## 6. Monthly Collection Flow



The expected monthly flow is:



CollectionScheduler

&#x20;   |

&#x20;   v

PrinterCollectionService

&#x20;   |

&#x20;   v

PrinterReading (MONTHLY)

&#x20;   |

&#x20;   +--> ReadingRepository

&#x20;   |

&#x20;   +--> CentralApiClient

&#x20;   |

&#x20;   v

NotificationService

&#x20;   |

&#x20;   v

EmailService



The monthly reading shall include the collection timestamp and must be distinguishable from regular operational readings.



\---



\## 7. SNMP Collection Strategy



Normal production collection should use direct SNMP GET requests whenever the required OIDs are already known.



Example:



known total page counter OID

&#x20;   |

&#x20;   v

SNMP GET

&#x20;   |

&#x20;   v

counter value



SNMP Walk should primarily be used for:



\- diagnostics;

\- OID discovery;

\- support for new printer models.



The controlled GETNEXT walk validated during the prototype should be preferred over TreeUtils for devices where TreeUtils produces timeouts.



\---



\## 8. Printer Profiles



Printer-specific OID information must not be placed in:



\- PrinterAgentApplication;

\- Main;

\- scheduling components;

\- persistence components.



This information should be isolated through printer profiles.



Example conceptual structure:



PrinterProfile

&#x20;   |

&#x20;   +-- BrotherPrinterProfile

&#x20;   +-- HpPrinterProfile

&#x20;   +-- KyoceraPrinterProfile



A future profile resolver may select the appropriate profile based on:



\- manufacturer;

\- model;

\- SNMP identification;

\- configuration.



\---



\## 9. Failure Handling



PrinterAgent must distinguish between:



\- successful SNMP response;

\- timeout;

\- noSuchObject;

\- noSuchInstance;

\- invalid returned value;

\- unexpected protocol error.



A failure must not terminate the application.



Each printer must be processed independently.



Repeated failures should be tracked and may generate an alert after the configured threshold is reached.



\---



\## 10. Logging



Production code should use structured logging.



Logging should cover at least:



\- application startup;

\- application shutdown;

\- scheduled collection start;

\- printer collection success;

\- printer collection failure;

\- SNMP timeout;

\- API synchronization;

\- notification delivery;

\- unexpected errors.



System.out.println() may be used during development, but it should not be the primary production logging mechanism.



\---



\## 11. Package Direction



An initial package structure may evolve toward:



com.printeragent

|

+-- application

+-- config

+-- domain

+-- service

+-- snmp

+-- persistence

+-- integration

+-- notification

+-- scheduling



The final package structure will be validated after the class model is defined.



\---



\## 12. Initial Domain Relationships



Conceptually:



Customer

&#x20;   |

&#x20;   +-- multiple Printer



Printer

&#x20;   |

&#x20;   +-- multiple PrinterReading



PrinterReading

&#x20;   |

&#x20;   +-- CollectionType



PrinterCollectionService

&#x20;   |

&#x20;   +-- SnmpClient

&#x20;   +-- PrinterProfile



CollectionScheduler

&#x20;   |

&#x20;   +-- PrinterCollectionService



PrinterCollectionService

&#x20;   |

&#x20;   +-- ReadingRepository

&#x20;   +-- FailureTracker



CentralApiClient

&#x20;   |

&#x20;   +-- sends readings to central system



NotificationService

&#x20;   |

&#x20;   +-- EmailService



\---



\## 13. Open Architectural Decisions



The following decisions are intentionally not finalized yet:



\- database technology;

\- scheduling framework;

\- dependency injection framework;

\- API protocol details;

\- e-mail provider;

\- printer profile implementation strategy;

\- Windows background service strategy;

\- configuration storage strategy;

\- synchronization retry strategy.



These decisions should be made only when enough information is available.



Important decisions should be documented through Architecture Decision Records (ADR).



\---



\## 14. Next Step



The next design activity is to create the initial class diagram based on the responsibilities defined in this document.



The diagram will be reviewed before Java implementation begins.

