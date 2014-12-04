CookPal Server/DB: 
<-------------------------->
index.html
dbaccess.jsp

A simple page used to query (select and delete) from the DB and return the results. Used for debugging app/server interactions.

login_handler.jsp
Processes login from AccountActivity (first time and returning)

request_handler.jsp
Processes inbound and outbound interaction between the server and app through HttpPost/Get. Returns a JSON object for grab requests.

Additional Packages Used:
<-------------------------->
MySQL JDBC Connector (5.1.33)
JSONSimple 1.1.1

EC2 Info:
<-------------------------->
Host/DNS:
ec2-54-69-39-93.us-west-2.compute.amazonaws.com

Use Port 8080
User: ec2-user

SQLDB: TCP/IP over SSH
DBname: CookPal
SQL user: root
password: none
Hostname: 127.0.0.1
Port: 3306