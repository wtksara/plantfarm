# PlantFarm
### System architecture
The system includes a remote water pump control system and a plantation control system, an MQTT broker, a web application and data storage methods.The main element in the system is the MQTT broker running on the Raspberry PI, responsible for the exchange of information between the web application, the plantation control system and the water pump control system.

<img src="/Architecture.png" width="720" height="324">

The system assumes two possibilities of use, the first is the operation of the plantation, the second is the operation of the water pump. Both control systems are managed by microcontrollers, which are responsible for the operation of appropriate sensors and devices depending on the intended purpose.
In addition, microcontrollers are also responsible for communication with the MQTT broker in order to transfer the obtained measurements or perform an action initiated by the web application.

The discussed system requires interaction with the user, which can be provided through a web application. This application is the main recipient of messages transferred under the MQTT protocol. The interface is designed to visualize the collected data and enable the user to operate and manage the existing database.

Data storage is a separate component of the system, however, its management is available only from the level of the web application. A database managed by PostgresSQL was used to store the data, but also the Amazon S3 storage service, due to the type of some data. Access to the application is authenticated and authorized so that an unauthorized person cannot modify the database.

### Backend for web application
The following libraries were used in the project: Spring Web, Spring Boot DevTools, Lombok, Spring Data JPA, PostgresSQL Driver, AWS SDK for Java, Spring Security, JJWT, Spring Integration.


#### Database structure and relations
<img src="/schemat.png" width="666" height="474">

#### Amazon S3
The project uses the paid Amazon S3 mass storage service offered by Amazon Web Service to store and download plant images. 

The process of adding a new plant with its picture is presented. The client, i.e. the React application, sends the graphics and data about the plant to the server using the form. Then the class of the controller responsible for handling the given entity, processes the input parameters and calls the appropriate services. A service responsible for saving plant data to the database and a service supporting image transfer to the cloud are respectively launched. The created image file path is saved to the appropriate plant in the database.

<img src="/amazon.png" width="250" height="333">

#### Security
The project decided to secure access to certain resources, due to the potential interference with the database and forcing undesirable actions. For this purpose, it was decided to use two tools, i.e. Spring Security and JSON Web Token.

### Hardware part
In the project, it is possible to monitor the condition of the plantation on the basis of measurements made by the microcontroller. The photos show the actual operation of the system.

<img src="/1.png">

<img src="/2.png">
