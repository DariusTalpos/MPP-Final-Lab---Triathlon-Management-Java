# MPP-Final-Lab---Triathlon-Management-Java

C# version: https://github.com/DariusTalpos/MPP-Final-Lab---Triathlon-Management-C-Sharp

EN: This application is the final version of the homework received at the "Projecting and Programming Methods" course at Babeș-Bolyai University.
It is a client-server application that stores data using a PostgreSQL database, and has multiple functionalities:
1. Log In - by starting the client side of the application, and having the server running beforehand, the app user can log in into the application by using a valid username and the associated password in the log in window.
2. Viewing participants, rounds, results - the main menu of the application presents the user a list of participants (names and points) and a list of rounds (names). By choosing a round, the user can view the paricipants of that round and the amount of points they obtained.
3. Adding new rounds and results - by pressing the "Add New Round/Score Data" button, the user is sent to a new window where they can insert a round name, an amount of points and select a participant. If they choose to submit this score, the data will be saved (if the round name is not in the database, a new round with this name will be created). The user can also choose to cancel this action by pressing the "Cancel button".
4. Log Out - by pressing the "Log out" button in the main menu, the user will log out of the application.

This application was built using the Java programming language with the GUI being created by using JavaFX. It is also compatible with the C# version of the application, allowing the C# client to connect to the Java server using Protobuf. One of the repositories also has a version written using Hibernate. Using the Spring Framework, the application can start a server which allows REST services to be utilised.
Javascript and React were used to create a webpage which allows the use of the REST services.
While the application uses an RPC connection to communicate between client and server, there is implemented code allows for asynchronous messaging using ActiveMQ.

RO: Această aplicație este versiunea finală a temei primite în cadrul materiei "Metode de Programare și Proiectare" la Universitatea Babeș-Bolyai.
Este o aplicație client-server care stochează date utilizând o bază de date PostgreSQL și are multiple funcționalități:
1. Log In - prin pornirea părții de client a aplicației, server-ul fiind activ în prealabil, utilizatorul poate să se conecteze la aplicație prin introducerea unui nume de utilizator și a parolei aferente în fereastra de log in.
2. Vizualizarea participanților, a rundelor și a rezultatelor - meniul principal al aplicației prezintă utilizatorului o listă cu participanții (nume și punctaj) și o listă de runde (nume). La alegerea unei runde, utilizatorul poate vizualiza participanții acelei runde și punctajul obținut de fiecare.
3. Adăugarea de runde și de rezultate noi - prin apăsarea butonului "Add New Round/Score Data", utilizatorul accesează o nouă fereastră unde se poate insera numele unei runde, un punctaj și să selecteze un participant. Dacă se alege înregistrarea acesui scor, datele vor fi salvate (dacă numele rundei nu se regăsește în baza de date, o nouă rundă având acest nume va fi creată). Utilizatorul poate să aleagă să anuleze această acțiune prin apăsarea butonului "Cancel".
4. Log Out - prin apăsarea butonului "Log out" în meniul principal, utilizatorul se poate deconecta de la aplicație.

Această aplicație a fost construită utilizând limbajul de programare Java iar GUI-ul a fost creat utilizând JavaFX. Este de asemenea compatibilă cu versiunea în limbaj C#, permițând clientului C# să se conecteze la server-ul Java utilizând Protobuf. Unul dintre repository-ul are de asemenea o versiune scrisă folosind Hibernate. Folosind framework-ul Spring, aplicația poate porni un server care permite utilizarea de servicii REST.
Javascript și React au fost folosite pentru a crea o pagină web care permite utilizarea de servicii REST.
Deși aplicația folosește o conexiune RPC pentru comunicarea dintre client și server, este implementat cod care permite comunicare asincronă folosind ActiveMQ.
