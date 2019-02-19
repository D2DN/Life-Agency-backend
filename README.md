# LifeAgencyBackEnd's README


In order to make this backend runable, the following steps must be followed.

 - Download the project into a machine (git clone)
 - Configure the project's properties, in "./src/main/resources/config.properties" and for the Unit Tests "./src/test/resources/config.properties"
 - Compile the project: maven clean install; so that, the war of the project will be created at: ./target/SiiLifeAgency.war
 - This war can be deployed in any Web Container Server, as in Tomcat in the folder "webapp"
 - Once deployed, access this address: http://<ip>:<port>/SiiLifeAgency/test. 
 - if everything is ok, you will get this json answer: {"data":"Welcome"}. If so, the project is up and running
    
This project is built using Java 8 and Spring Framework (MVC, Rest). The whole system works as a RESTfull. The frontend is responsible for consuming these rests and show the results to the final user.