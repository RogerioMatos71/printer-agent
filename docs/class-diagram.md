\# PrinterAgent — Initial Class Diagram



\## 1. Purpose



This document represents the initial class model of PrinterAgent.



The diagram is based on the approved requirements, the initial architecture, and the technical knowledge obtained from the SNMP prototype.



This model is intentionally independent from specific frameworks and database technologies.



\---



\## 2. Initial Class Diagram



```mermaid

classDiagram



&#x20;   class PrinterAgentApplication {

&#x20;       +start()

&#x20;       +shutdown()

&#x20;   }



&#x20;   class ApplicationConfig {

&#x20;       +String customerId

&#x20;       +Duration collectionInterval

&#x20;       +int monthlyCollectionDay

&#x20;       +int snmpTimeout

&#x20;       +int snmpRetries

&#x20;       +int failureAlertThreshold

&#x20;   }



&#x20;   class Printer {

&#x20;       +String id

&#x20;       +String name

&#x20;       +String ipAddress

&#x20;       +int snmpPort

&#x20;       +String community

&#x20;       +String manufacturer

&#x20;       +String model

&#x20;       +String serialNumber

&#x20;   }



&#x20;   class PrinterReading {

&#x20;       +String id

&#x20;       +String printerId

&#x20;       +String serialNumber

&#x20;       +long totalPageCount

&#x20;       +Integer tonerCapacity

&#x20;       +Integer tonerLevel

&#x20;       +Integer drumCapacity

&#x20;       +Integer drumLevel

&#x20;       +LocalDateTime collectedAt

&#x20;       +CollectionType collectionType

&#x20;       +CollectionStatus status

&#x20;       +String failureMessage

&#x20;   }



&#x20;   class CollectionType {

&#x20;       <<enumeration>>

&#x20;       REGULAR

&#x20;       MONTHLY

&#x20;   }



&#x20;   class CollectionStatus {

&#x20;       <<enumeration>>

&#x20;       SUCCESS

&#x20;       FAILURE

&#x20;   }



&#x20;   class SnmpClient {

&#x20;       +String readOid(Printer printer, String oid)

&#x20;       +SnmpResult getNext(Printer printer, String oid)

&#x20;       +List\~SnmpResult\~ walkSubtree(Printer printer, String rootOid)

&#x20;   }



&#x20;   class SnmpResult {

&#x20;       +String oid

&#x20;       +String value

&#x20;       +boolean successful

&#x20;       +String error

&#x20;   }



&#x20;   class PrinterProfile {

&#x20;       <<interface>>

&#x20;       +String getSerialNumberOid()

&#x20;       +String getTotalPageCountOid()

&#x20;       +SupplyDefinition getTonerDefinition()

&#x20;       +SupplyDefinition getDrumDefinition()

&#x20;   }



&#x20;   class BrotherPrinterProfile {

&#x20;       +String getSerialNumberOid()

&#x20;       +String getTotalPageCountOid()

&#x20;       +SupplyDefinition getTonerDefinition()

&#x20;       +SupplyDefinition getDrumDefinition()

&#x20;   }



&#x20;   class SupplyDefinition {

&#x20;       +String descriptionOid

&#x20;       +String capacityOid

&#x20;       +String levelOid

&#x20;   }



&#x20;   class PrinterProfileResolver {

&#x20;       +PrinterProfile resolve(Printer printer)

&#x20;   }



&#x20;   class PrinterCollectionService {

&#x20;       +PrinterReading collect(Printer printer, CollectionType type)

&#x20;   }



&#x20;   class CollectionScheduler {

&#x20;       +start()

&#x20;       +runRegularCollection()

&#x20;       +runMonthlyCollection()

&#x20;   }



&#x20;   class ReadingRepository {

&#x20;       <<interface>>

&#x20;       +void save(PrinterReading reading)

&#x20;       +List\~PrinterReading\~ findByPrinter(String printerId)

&#x20;   }



&#x20;   class CentralApiClient {

&#x20;       <<interface>>

&#x20;       +void sendReading(PrinterReading reading)

&#x20;       +void sendMonthlyReading(PrinterReading reading)

&#x20;   }



&#x20;   class FailureTracker {

&#x20;       +void registerSuccess(String printerId)

&#x20;       +void registerFailure(String printerId)

&#x20;       +int getConsecutiveFailures(String printerId)

&#x20;       +boolean shouldAlert(String printerId)

&#x20;   }



&#x20;   class NotificationService {

&#x20;       <<interface>>

&#x20;       +void sendFailureAlert(Printer printer, int failureCount)

&#x20;       +void sendMonthlyReading(Printer printer, PrinterReading reading)

&#x20;   }



&#x20;   class EmailService {

&#x20;       +void send(String recipient, String subject, String message)

&#x20;   }



&#x20;   PrinterAgentApplication --> ApplicationConfig

&#x20;   PrinterAgentApplication --> CollectionScheduler



&#x20;   CollectionScheduler --> PrinterCollectionService



&#x20;   PrinterCollectionService --> SnmpClient

&#x20;   PrinterCollectionService --> PrinterProfileResolver

&#x20;   PrinterCollectionService --> ReadingRepository

&#x20;   PrinterCollectionService --> FailureTracker

&#x20;   PrinterCollectionService --> CentralApiClient

&#x20;   PrinterCollectionService --> NotificationService



&#x20;   PrinterProfileResolver --> PrinterProfile

&#x20;   PrinterProfile <|.. BrotherPrinterProfile



&#x20;   PrinterProfile --> SupplyDefinition



&#x20;   PrinterCollectionService --> Printer

&#x20;   PrinterCollectionService --> PrinterReading



&#x20;   PrinterReading --> CollectionType

&#x20;   PrinterReading --> CollectionStatus



&#x20;   SnmpClient --> SnmpResult



&#x20;   NotificationService --> EmailService



&#x20;   Printer "1" --> "\*" PrinterReading

