# Harvey Mudd
## CS121 - Software Development
### Spring 2015

#### Team: Andrew Fishberg, Hannah Young, Hannah Rose
#### App: Book Exchange

##### Contact us: afishberg@hmc.edu, hyoung@hmc.edu, hrose@hmc.edu

#### APP DESCRIPTION
Our app, Book Exchange, seeks to improve friendly book-borrowing between students by providing a platform to locate books that users want to borrow and to loan out books they have from previous semesters. The Book Exchange system achieves our goal of connecting book-sharing users through a Android application frontend and Java server backend. Users only directly interact with the app while the server supplies the app with all relevant data on users, books, and exchanges. 

The Book Exchange system refers the process of a user loaning or borrowing a specific book as an exchange. Each exchange has a lifecycle from its creation as an offer to loan or a request to borrow a book, to the physical exchange of the book between users, to its safe return (see Architecture for the technical details of the lifecycle). This allows the application to track the exchange process and to whom the user needs to return the book at the end of the exchange. An open exchange is an unaccepted offer or request, whereas a current exchange is one where the book is currently being borrowed by someone. A complete exchange is one where the book has been borrowed and returned to its owner. An exchange advances whenever the status of the exchange changes from INITIAL (unaccepted offer or request) to RESPONSE (current exchange pending user confirmation) to ACCEPTED (current, active exchange between two users) to COMPLETE (the loaner marks the exchange as finished). 

#### INSTALLING AND LAUNCHING/EDITING
To run and test the code, Android Studio builds the project into a .apk file using Gradle, which can then be downloaded and run on an Android device. The minimum APK level required to run this app is 15. Our app uses Gradle version 1.1.2. If working in Android Studio, then the Gradle plugin will be automatically downloaded. We would recommend developing in this environment for consistency's sake. However, if this is not preferable, information on how to configure Gradle can be found at https://developer.android.com/tools/building/configuring-gradle.html. 

Also, our app is dependent on data stored on our server on Knuth, hosted by Harvey Mudd College's CS department. This server will not be left running, so if access is desired, please contact one of the authors listed above. Otherwise, the dummy credentials to log into the app are username = "foo@example.com" and password = "hello". Set debug=true in client so that the app does not attempt to access the server, and so that dummy data can show up. However, without server access, there may be some activities on which the app still crashes. 
