# OrderGenerator

STS 4.13.1.RELEASE
JavaSE - 1.8

Spring Boot Command Line runner to generate orders and send them to PizzaJoint via REST

<h2>How it works:</h2>

Takes 1 command line argument i.e. the number of orders to be generated <br>
Note: Before executing in STS go to Run -> Run Configurations -> OrderGenerator -> Spring Boot -> Disable ANSI console output

Step 1: Authentication
Obtain JWT token on successful othentication and store it in service for further requests

Step 2: Send randomly generated orders to PizzaJoint
