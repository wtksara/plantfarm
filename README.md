# PlantFarm
### System architecture
The system includes a remote water pump control system and a plantation control system, an MQTT broker, a web application and data storage methods.The main element in the system is the MQTT broker running on the Raspberry PI, responsible for the exchange of information between the web application, the plantation control system and the water pump control system.

![Alt text](/rys1png.png)

The system assumes two possibilities of use, the first is the operation of the plantation, the second is the operation of the water pump. Both control systems are managed by microcontrollers, which are responsible for the operation of appropriate sensors and devices depending on the intended purpose.
In addition, microcontrollers are also responsible for communication with the MQTT broker in order to transfer the obtained measurements or perform an action initiated by the web application.

The discussed system requires interaction with the user, which can be provided through a web application. This application is the main recipient of messages transferred under the MQTT protocol. The interface is designed to visualize the collected data and enable the user to operate and manage the existing database.

Data storage is a separate component of the system, however, its management is available only from the level of the web application. A database managed by PostgresSQL was used to store the data, but also the Amazon S3 storage service, due to the type of some data. Access to the application is authenticated and authorized so that an unauthorized person cannot modify the database.

### Backend for web application
The following libraries were used in the project: Spring Web, Spring Boot DevTools, Lombok, Spring Data JPA, PostgresSQL Driver, AWS SDK for Java, Spring Security, JJWT, Spring Integration.


#### Database structure and relations
