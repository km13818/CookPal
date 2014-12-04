<!-- login handler -->
<!-- processes first time users into the DB -->
<%@page import="java.sql.*"%>
<%@page import="java.io.*"%>
<%@page import="org.json.simple.*" %>
<%
   String fb_id = request.getParameter("fb_id");
   String userName = request.getParameter("username");
   
   boolean isInDB = false;
    
   Connection conn = null;
   PreparedStatement pstmt = null;
   ResultSet rs = null;  		
	try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookPal", "root", "");
      Statement stmt = conn.createStatement();

      //check if the account is already in the database

      String findQuery = "SELECT * FROM account WHERE fb_id = ?";
      pstmt = conn.prepareStatement(findQuery);
      pstmt.setString(1, fb_id);
      rs = pstmt.executeQuery();

      while(rs.next()) { 
         if(rs.getString("fb_id").equals(fb_id)) {
            isInDB = true;
            break;
         }
      }
      if(isInDB == false) {
         query = "INSERT INTO account (name, account_id) VALUES (?, ?)";
         pstmt = conn.prepareStatement(query);     

         conn.setAutoCommit(false);       
         pstmt.setString(1, userName);
         pstmt.setString(2, fb_id);
         pstmt.executeUpdate(); 
         conn.commit();
         conn.setAutoCommit(true);  
      }        	 
	}
	catch (SQLException e) {
      e.printStackTrace();
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