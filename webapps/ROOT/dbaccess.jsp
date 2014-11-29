<html lang="en-US">
    <head>
        <meta charset="UTF-8">
        <title>Team Cookpal DB Tables!</title>
        <link rel="stylesheet" href="css/foundation.css">
        <script src="js/vendor/custom.modernizr.js"></script>        
    </head>
    <body>
    
    <div class="row">
      <h3>Here's your table...</h3>
      <div class="large 8-columns">
<%@page import="java.sql.*"%>
<%@page import="java.io.*"%>
<%
   String op = request.getParameter("operation");
   String tbl = request.getParameter("table");
   String c1c = request.getParameter("col1");
   String c2c = request.getParameter("col2");
   String c1v = request.getParameter("col1v");
   String c2v = request.getParameter("col2v");
   
   String query = ""; 
   int condCount = 2;
   Connection conn = null;
   PreparedStatement pstmt = null;
   ResultSet rs = null;
    		
	try {
		Class.forName("com.mysql.jdbc.Driver");
		  conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookPal", "root", "");
		  Statement stmt = conn.createStatement();
        //remove the where clause
        if(c1c.equals("") || c1v.equals("")) {
         condCount--;
        }
        if(c2c.equals("") || c2v.equals("")) {
         condCount--;
        }
        
        if(condCount == 0) {
           query = op + " * FROM " + tbl; 
        	  pstmt = conn.prepareStatement(query);   
        }
        else if(condCount == 1) {
           query = op + " * FROM " + tbl + " WHERE " + c1c + " = ?";          
           pstmt = conn.prepareStatement(query);
           pstmt.setString(1, c1v); 
        }
        else {
           query = op + " * FROM " + tbl + " WHERE " + c1c + " = ? AND " + c2c + " = ?";
           pstmt = conn.prepareStatement(query);
           pstmt.setString(1, c1v);
           pstmt.setString(2, c2v);              
        }
		  rs = pstmt.executeQuery();
        ResultSetMetaData metadata = rs.getMetaData();
        int columnCount = metadata.getColumnCount();
        %>
          <table>
            <tr>
            <%
            for (int i = 1; i <= columnCount; i++) {
              %>
                 <th><%=metadata.getColumnName(i)%></th>
            <%
            }
            %></tr>
        <%
        while(rs.next()) {
            %><tr><%
            for (int i = 1; i <= columnCount; i++) {
              %>
                 <td><%=rs.getString(i)%></td>
            <%
            }
            %></tr><%

        }
        %></table><%
	}
	catch (SQLException e) {
      %><div class="row">
            Something Went Wrong!
         </div><%
	}
	finally {
		// Release resources in a finally block in reverse-order of
		// their creation
		
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) { } // Ignore
			rs = null;
		}        
		
		if (pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) { } // Ignore
			pstmt = null;
		}
		
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) { } // Ignore
			conn = null;
		}
	} 
%>	
      </div>
       <a href="index.html" class="button small">Go back!</a>
    </div>  

    </body>
    <script>
        document.write('<script src=/js/vendor/'
        + ('__proto__' in {} ? 'zepto' : 'jquery')
        + '.js><\/script>');
    </script>
    <script src="js/foundation.min.js"></script>
    <script>$(document).foundation();</script>
</html>