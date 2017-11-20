
Cake Manager Micro Service (fictitious)

1.	Using Eclipse IDE with tomcat server
2.	Needed to recreate the package com.waracle.cakemgr and move the java files to the package.
3.	The url http://localhost:8080/cake-manager/ returned index.jsp - Hello World!
4.	The url http://localhost:8080/cake-manager/cakes/ returned 404, the requested resource is not available.
5.	The url http://localhost:8080/cake-manager/cakes return the data in json format.
6.	Resubmission of the url caused the server to timeout.
7.	Made the following changes to CakeEntity

  a.	renamed table Employee to Cake
  b.	changed the uniqueConstraint for TITLE
  c.	renamed 	Integer employeeId to cakeId
              column EMAIL to TITLE
              column FIRST_NAME to DESCRIPTION
              column LAST_NAME to IMAG
		

8.	the unique the cakes from the json are now being shown without any errors.
9.	On re submission of the url the server still hangs.
10.	Made the following changes to CakeServlet
   a.	In the doGet method  the session is not being closed.
   b.	Closing the session stopped the server from hanging.
   c.	Improved the json parsing in the init method by using the ObjectMapper
   d.	This caused a problem as the json had an attribute of desc rather then description.
   e.	Changed the description attribute in the CakeEntity to desc.
   f.	Refactored the doGet method to to use ObjectMapper to write the json to the response.
   g. Added a validBrowser method so any clients not recognised by the method would return the data as json.

11. add a doPost method 
   a.  validates that a title, description and image url exists in the request.
   b. Stores any validation errors in an array list that is set in a response attribute.
   c. if valid add a new cake to the database.
   d. If the cake exist write a errror message to the response

12. add a JSP 
   a. which displays the cakes from the database.
   b Allows the addition of a new cake.
   c. Shows any error messages.




	
