# technical-assignment-silicon-waha
Employee logging timesheet system:
    Employee has to register to the system then will  redirect to the login page . 
  Employee login to system and log his timesheet login/logout times. Also can view all his historical time logs order descending .
  Employee can see his newest log time then older.
  On the logging process , System will not accept logging interval conflict/intersect and
  also will not accept log in/out after current time, all logging should be before current time.


Technical Doc. :
  - Persistence layer : using spring data for all databases Query handle transaction and commit
  - Service layer : for handle business logic and inject persistence layer to persist data
  - Restful API link service with front end  needed to implement system
  - logging using lo4j log all used functions 
  - Add interceptor for logging any request/response and request handler 


Used Tech. :
 - spring boot(Security,Data,MVC,) 
 - angular JS
 - twitter bootstrap styles
 - Mysql Database


System setup:
     Should maintain database username , password, port ,host and schema to connect to database(mysql). 
   The default database properties placed in file ’application.yml’ are :
     - Username : root
     - Password : root
     - Port: 3306
     - Host : localhost 
     - Schema : silicon_waha

Run the system: 
   Use the link : http://localhost:8588/login  then register and login then start timesheet login / logout times.
   be care not conflict logging interval ,


