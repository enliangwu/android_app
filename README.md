# Android Application

### Project Topics: Mobile to Cloud application

This project has 2 tasks:

- Task 1: design and build a simple mobile application that will communicate with a RESTful web service in the cloud.
- Task 2: add an operations logging and analysis function to your Task 1 web service.

### Task 1: Mobile to Cloud Application
Design and build a distributed application that works between a mobile phone and the cloud. Specifically, develop a native Android application that communicates with a web service that you deployed to Heroku.

The following is a diagram of the components in Task 1.  
![Task 2 Diagram](docs/Project4-Diagram-Task1.png)

The web service deployed to Heroku should be a simple RESTful API.

Users will access your application via a native Android application. **You do not need to have a browser-based interface for your application** (only for the Task 2 dashboard). The Android application should communicate with your web service deployed to Heroku. Your web service is where the business logic for your application should be implemented (including fetching information from the 3rd party API).

In detail, your application should satisfy the following requirements:

#### 1.	Implement a native Android application
a.	Has at least three different kinds of Views in your Layout (TextView, EditText, ImageView, or anything that extends android.view.View). **In order to figure out if something is a View, find its API.  If it extends android.view.View then it is a View.**  
b.	Requires input from the user  
c.	Makes an HTTP request (using an appropriate HTTP method) to your web service  
d.	Receives and parses an XML or JSON formatted reply from your web service  
e.	Displays new information to the user  
f.	Is repeatable (I.e. the user can repeatedly reuse the application without restarting it.)

#### 2.	Implement a web service, deployed to Heroku
a.	Implement a simple (can be a single path) API.  
b.	Receives an HTTP request from the native Android application  
c.	Executes business logic appropriate to your application.  This includes fetching XML or JSON information from some 3rd party API and processing the response.
- -10 if you use a banned API
- -10 if screen scrape instead of fetching XML or JSON via a published API

**Use Servlets, not JAX-RS, for your web services.** Students have had issues deploying web applications built with JAX-RS to Docker Containers and a solution has not yet been found.

d.	Replies to the Android application with an XML or JSON formatted response. The schema of the response can be of your own design.  
-	-5 if more information is returned to the Android app that is needed, forcing the mobile app to do more computing than is necessary. The web service should select and pass on only the information that the mobile app needs.

Refer back to Lab 3 for instructions on pushing a web service to Heroku.

#### 3. Handle error conditions
Your application should test for and handle gracefully:
 - Invalid mobile app input
 - Invalid server-side input (regardless of mobile app input validation)
 - Mobile app network failure, unable to reach server
 - Third-party API unavailable
 - Third-party API invalid data

#### Writeup
Because each student's mobile/cloud application will be different, you are responsible for making it clear to the TAs how you have met these requirements, and it is in your best interest to do so. You will lose points if you don't make it clear how you have met the requirements. Therefore, you must create a document describing how you have met each of the requirements (1a – 2d) above.  (You do not need to document _3. Handle error conditions_.) Your writeup will guide the TAs in grading your application.  See the provided example ([Project4Task1Writeup.pdf](https://github.com/CMU-Heinz-95702/Project4/blob/master/docs/Project4-WriteUp.pdf)) for the content and style of this document.

Alternatively, instead of a document, you may submit a narrated screencast that includes the same information that would be in the writeup.

### Task 2: Web Service Logging and Analysis Dashboard

For Task 2, you are to embellish your web service to add logging, analysis, and reporting capabilities.  In other words, you are to create a web-based dashboard to your web service that will display information about how your service is being used. This will be web-page interface designed for laptop or desktop browser, not for mobile. In order to display logging and analytical data, you will have to first store it somewhere.  For this task, you are required to store your data in a noSQL database, or more specifically a MongoDB, database hosted in the cloud.

The following is a diagram of the components in Task 2.
![Task 2 Diagram](docs/Project4-Diagram.png)

**Note:**  Task 2 builds on Task 1, but for your own safety, you should not overwrite Task 1.  Rather, once you have Task 1 working, you should create a separate Task 2 project. In this way you will never lose the working Task 1 that you are required to submit. When deploying to Heroku, you should deploy Task 1 and Task 2 separately.  Heroku allows you do have multiple applications.  In this way, if Task 2 does not work for some reason, we still have Task 1 to grade.
#### Logging data
Your web service should keep track (i.e. log) data regarding its use.  You can decide what information would be useful to track for your web application, but you should track at least 6 pieces of information that would be useful for including in a dashboard for your application. It should include information about the request from the mobile phone, information about the request and reply to the 3rd party API, and information about the reply to the mobile phone.  Information can include such parameters as what kind of model of phone has made the request, parameters included in the request specific to your application, timestamps for when requests are received, requests sent to the 3rd party API, and the data sent in the reply back to the phone.
You should NOT log data from interaction with the operations dashboard, only from the mobile phone.
#### Database
You should log your data persistently so that it is available across restarts of the application. For this task you should use MongoDB to store your logging data.   MongoDB is a noSQL database that is easy to use.  By incorporating it into your web service you will gain experience using a noSQL database, and experience doing CRUD operations programmatically from a Java program to a database.

The main MongoDB web site is https://www.mongodb.com. The site provides documentation, a [downloadable version of the database manager application](https://www.mongodb.com/try/download/enterprise) (*mongod*) that you can run on your laptop, and MongoDB drivers for many languages, including Java.

*Mongod* is the MongoDB database server. It listens by default on port 27017. Requests and responses with the database are made via a MongoDB protocol.

*Mongo* (without the DB) is a command line shell application for interacting with a MongoDB database.  It is useful for doing simple operations on the database such as finding all the current contents or deleting them.

Because your web service will be running in the Heroku PaaS (or more specifically, Container-as-a-Service), you can’t run your database on your laptop.  Rather, you should use a MongoDB-as-a-Service to host your database in the cloud. Atlas (https://www.mongodb.com/cloud/atlas) is required because it has a free level of service that is adequate for your project.  

#### Setting up MongoDB Atlas
In this project, you are going to us nosql-database-as-a-service with MongoDB Atlas. Information about MongoDB can be found here: https://www.mongodb.com/what-is-mongodb

Getting started:
1. Create your account. Go to https://www.mongodb.com/cloud/atlas and create your own free account.
2. Choose Java as the preferred language.
3. Choose the FREE shared cluster.
4. Accept the default cloud provider and region and Create Cluster.
5. In the Security Quickstart:
 - *How would you like to authenticate your connection?*  
 Authenticate using Username and Password.  Create a MongoDB user name and password (**only use letters and numbers to save yourself some hassle for encoding it later**) -  don't forget these.  The cluster takes a few minutes to provision, so be patient.
 - *Where would you like to connect from?*  
 Choose My Local Environment and add the IP address `0.0.0.0/0`. This means your DB will be open to the world, which is needed for the grading purposes. (You can check this later on the Security tab, IP Whitelist. If it doesn't have that IP address, click on Edit.)
 - You can then Finish and Close
6. Connect to the cluster.  
  a) Click on the 'Connect' button in the Sandbox section.  
	b) For *Choose a connection method*; choose 'Connect with your application'. Then choose the Driver as 'Java', use version 4.3.  
  c) Click on the Full Driver Example. Click Copy to copy that code stub. For now, save that code in a file; later, you'll edit and paste into your application to connect to your MongoDB instance, but don't forget to replace your <password> with your database user‘s credentials (Note that when entering your password, any special characters are URL encoded; that's why a simple password is better here).  
  d) You will access this database in two ways:  
	**For Task 0:** Create a simple Java application to demonstrate reading and writing to the database as described in Task 0 above.  
  **For Task 2:** Create your dashboard.  

The sample code in the Quick Start guide shows how to access the database. You can access this cloud-based MongoDB database from your laptop as well as from Heroku.

Info about the MongoDB Java driver and sample code can be found here:  
https://docs.mongodb.com/drivers/java/sync/v4.3/quick-start/


You can easily add the MongoDB Java Drivers to a project with Maven:
```
<dependency>
   <groupId>org.mongodb</groupId>
   <artifactId>mongodb-driver-sync</artifactId>
   <version>4.3.3</version>
</dependency>
```

#### Hints for connecting to MongoDB Atlas

Use a password that uses only letters and numbers so you don't have to deal with encoding it.

When Heroku communicates with MongoDB Atlas, it requires TLSv1.2.  To enable this, edit your Dockerfile and add the following lines near the top of the file with the other ENV commands. (But not the first line in the file.)
```
# Use TLSv1.2 for communication between Heroku and MongoDB
ENV JAVA_OPTS="-Djdk.tls.client.protocols=TLSv1.2"
```

The MongoDB connection string that Atlas provides is of the form:
```
mongodb+srv://USER:PASSWD@CLUSTER.mongodb.net/mydb?retryWrites=true&w=majority
```

But the `+srv` will not work with a number of DNS servers, and TLS and an authentication mechanism needs to be defined. Therefore:
1. On the MongoDB Atlas dashboard where you created the database, click on *Database* and then on *Cluster0*  You will see three shard servers listed.  Click on the name of each to display the full server URL, and copy the full URL.  It will look something like  
 `cluster0-shard-00-02.cbkkm.mongodb.net:27017`
2. Find the URLs for the other two shard servers and copy them also.
3. Create your own connection string:  
`mongodb://USER:PASSWD@SERVER1,SERVER2,SERVER3/test?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1`  
Be sure to substitute your own values for USER, PASSWD, and SERVER1-3

The resulting connection string should look similar to:  
```
mongodb://myuser:mysecretpassword@cluster0-shard-00-02.cbkkm.mongodb.net:27017,cluster0-shard-00-01.cbkkm.mongodb.net:27017,cluster0-shard-00-00.cbkkm.mongodb.net:27017/myFirstDatabase?w=majority&retryWrites=true&tls=true&authMechanism=SCRAM-SHA-1


```

When running your application, you will see the following warning:  
`WARNING: SLF4J not found on the classpath.  Logging is disabled for the 'org.mongodb.driver' component`

If you would like to see the messages logged from the MongoDB driver, add the following dependencies to the pom.xml file:
```
<!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-api -->
 <dependency>
		 <groupId>org.slf4j</groupId>
		 <artifactId>slf4j-api</artifactId>
		 <version>1.7.36</version>
 </dependency>
 <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
 <dependency>
		 <groupId>org.slf4j</groupId>
		 <artifactId>slf4j-simple</artifactId>
		 <version>1.7.36</version>
 </dependency>
```

Bson info is available at:  
http://mongodb.github.io/mongo-java-driver/4.1/bson/documents/

#### Dashboard
The purpose of logging data to the database is to be able to create an operations dashboard for your web service.  This dashboard should be web page interface for use from a desktop or laptop browser (not a mobile device).

The dashboard should display two types of data:
1. Operations analytics – display at least 3 interesting operations analytics from your web service.  You should choose analytics that are relevant to your specific web service. Examples for InterestingPicture might be top 10 picture search terms, average Flickr search latency, or the top 5 Android phone models making requests.
2. Logs – display the data logs being stored for each mobile phone user interaction with your web service. The display of each log entry can be simply formatted and should be easily readable. **(Three points will be lost if they are displayed as JSON nor XML.)**  


In detail, your solution should satisfy the following requirements:
#### 1.	Log useful information
At least 6 pieces of information is logged for each request/reply with the mobile phone.  It should include information about the request from the mobile phone, information about the request and reply to the 3rd party API, and information about the reply to the mobile phone. (You should NOT log data from interactions from the operations dashboard.)
#### 2.	Store the log information in a database
The web service can connect, store, and retrieve information from a MongoDB database in the cloud.
#### 3.	Display operations analytics and full logs on a web-based dashboard
a. A unique URL addresses a web interface dashboard for the web service.  
b. The dashboard displays at least 3 interesting operations analytics.  
c. The dashboard displays **formatted** full logs.  

#### 4. Deploy the web service to Heroku  
This web service should have all the functionality of Task 1 but with the additional logging, database, and dashboard analytics functions.  

In your Task 2 writeup be sure to include the dashboard URL!

#### Task 2 Writeup
In the same style as Task 1, but in a separate document, describe how you have met these 4 requirements.

Alternatively, similar to Task 1, you may submit a narrated screencast that includes the same information that would be in the writeup.


