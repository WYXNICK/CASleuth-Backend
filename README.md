# CASleuth-Backend
This is the backend part of CASleuth software.

## Installation

### Software-Backend

#### Requirements

Before you proceed with setting up and running this project, ensure that you meet the following requirements:

- **Java Version:** You must have Java 17 installed on your system. You can download it from the official Oracle website or use an OpenJDK distribution.
- **Build Tool:** Maven is required for building and managing project dependencies. You can download and install Maven from [Maven's official website](https://maven.apache.org/).
- **Spring Boot Version:** This project is designed to work with Spring Boot version 2.7.13. Ensure that you have the specified version of Spring Boot set up in your development environment.

Meeting these requirements is essential to ensure a smooth setup and execution of the project.

#### Steps

Follow these steps to set up and run the project:

1. **Clone the Project:** Use the following command to clone the project from GitHub:

   ```
   git clone git@github.com:WYXNICK/CASleuth-Backend.git
   ```

2. **Navigate to Project Directory:** Change your current working directory to the root of the cloned project:

   ```
   cd CASleuth-Backend
   ```

3. **Build the Project:** Build the project using Maven by executing the following command:

   ```
   mvn clean install
   ```

4. **Configure the Database:** If your project interacts with a database, edit the `application.properties` file to configure the database connection information:

   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/casleuth?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

   Make sure to modify the above information according to your specific database configuration.

5. **Run the Application:** Start the Spring Boot application using the following command:

   ```
   mvn spring-boot:run
   ```

   Your application will run on the port 5555.

Or you can perform the steps mentioned above using [IntelliJ IDEA](https://www.jetbrains.com/idea/), which is a popular IDE for Java development:

1. **Clone the Project:**

   - Open IntelliJ IDEA.

   - Go to `File > New > Project from Version Control > Git`.

   - In the "URL" field, enter the GitHub repository URL:

     ```
     https://github.com/WYXNICK/CASleuth-Backend.git
     ```

   - Click the "Clone" button to clone the project to your local machine.

2. **Navigate to Project Directory:**

   - After the project is cloned, IntelliJ IDEA will typically open it automatically.
   - If not, you can navigate to the project directory by clicking `File > Open...` and selecting the project's root directory.

3. **Build the Project:**

   - Once the project is open in IntelliJ IDEA, Maven will be automatically detected and used for the project. You can monitor the build progress in the bottom status bar.

4. **Configure the Database:**

   - locate the `application.properties` file within the project. You can typically find it under `src/main/resources`.
   - Right-click the file and select "Edit" to modify the database connection properties as needed.

5. **Run the Application:**

   Click the green "Run" button in the top-right corner of IntelliJ IDEA.

IntelliJ IDEA automatically detects and integrates with Maven, so you don't need to manually execute Maven build commands.

Following these steps will help you successfully install, configure, and run the Spring Boot backend project.

make sure your Spring Boot project is up and running. If you see a message in the console similar to "Tomcat started on port(s): 5555 (http)", it means this project is up and running and you can access http://localhost:5555/swagger-ui/index.html to open Swagger UI of our project and test the interfaces.

#### server deployment

Of course, we have deployed our backend project on the Tencent Cloud server. You can see all the interfaces implemented in our backend section on this website and test them:http://tongji-software-igem2023.com:5555/swagger-ui/index.html

##### Prerequisites

Before deploying your project on the server, make sure the following prerequisites are met:

1. **Local Testing:** Ensure that your local project can run successfully on `localhost`, and that the API endpoints work as expected.
2. **Tools:** You will need two tools for deployment:
   - **[Xshell](https://www.xshell.com/zh/xshell/):** A terminal emulator used for server control.
   - **[Xftp](https://www.xshell.com/zh/xftp/):** A file transfer tool used to upload local files to the server.

##### Deployment Steps

Follow these steps to deploy your project on the server using IntelliJ IDEA:

1. **Build the Project:**

   - First, ensure that your local project is in working condition on `localhost`.

   - Test the API endpoints to confirm that they are functioning as expected.

   - Next, use Maven for building the project:

     - Clean the project by executing `clean` or simply click the `clean` button(in IDEA):

       ```
       mvn clean
       ```

     - Package the project into a JAR executable file or simply click the `package` button(in IDEA):

       ```
       mvn package
       ```

2. **Locate the JAR File:**

   After packaging, you'll find the JAR file in the `target` directory within your project folder.

3. **Check Java on the Server:**

   In the Xshell terminal, check if Java is installed on the server by running:

   ```
   java -version
   ```

4. **Install Java (if needed):**

   If Java is not installed, use a package manager to install Java. For example, with `yum`:

   ```
   sudo yum install java-17-openjdk-devel
   ```

   After installation, verify the Java version again:

   ```
   java -version
   ```

5. **Transfer JAR File to Server:**

   Utilize Xftp to transfer the JAR file generated in the previous step to the server.

6. **Navigate to the Project Directory:**

   Use the `cd` command to move to the directory where you uploaded the JAR file.

7. **Run the JAR File:**

   Run the JAR file with the following command (Note: this method will stop if you close the connection):

   ```
   java -jar yourprojectname.jar
   ```

8. **Open Necessary Ports:**

   Before using the API, ensure that the corresponding ports are open. This can be done both on the server-side and through your cloud service control panel.

9. **Monitor Running Java Processes:**

   To view the Java processes running on the server, use the following command:

   ```
   ps -ef | grep java
   ```

10. **Terminate a Process (if needed):**

    To terminate a specific Java process, use the `kill` command with the process ID (PID):

    ```
    bashCopy code
    kill -9 PID
    ```

11. **Run JAR in Background (Optional):**

    If you want the JAR file to continue running on the server even after closing the connection, you can use the following command:

    ```
    nohup java -jar yourprojectname.jar >output 2>&1 &
    ```

This will allow the JAR file to run persistently on the server.

Note: 

1. The SQL file containing the database structure is located in the 'related resources' folder.
2. The relevant model files and execution code used by the software are all located in the 'related resources' folder.
