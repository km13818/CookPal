<!--[if IE 8]>         <html class="no-js lt-ie9" lang="en" > <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js" lang="en" > <!--<![endif]-->
<body>
    <head>
        <meta charset="UTF-8">
        <title>DB Access</title>
        <link rel="stylesheet" href="css/foundation.css">
        <script src="js/vendor/custom.modernizr.js"></script>
    </head>
    <body>
<%@page import="java.sql.*"%>
<%@page import="java.util.*"%>

<%
    String username = request.getParameter("username");
    String fb_id = request.getParameter("fb_id");

    %>
    <div class="row">
      <h5><%=request.getParameter("username")%></h5>
      <h5><%=request.getParameter("fb_id")%></h5>    
    </div>
    <%
    
    Connection conn = null;
	 PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    
    boolean isInDB = false;
    
    if(username == null || fb_id == null) {
      %>
      <!-- if no input is passed in (should never happen) -->
      <div class="row">
            <div class="large-12 large-centered columns text-center">
                <div data-alert class="alert-box alert">
                    <a href="index.html">An error occured. Go back.</a>
                </div>
            </div>      
        </div>
      <%
    }
    else {
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
         //if there is a match for fb id,
         if(rs.getString("fb_id").equals(fb_id))  {
            %><h1>Hello <%=rs.getString("username")%>! Welcome Back!</h1><%
            isInDB = true;
            break;
         }
      }
      
      if(!isInDB) {
         String newAcc = "INSERT INTO account (username, fb_id) VALUES (?, ?)";
         pstmt2 = conn.prepareStatement(newAcc);
         conn.setAutoCommit(false); 
         pstmt2.setString(1, username);
         pstmt2.setString(2, fb_id);
         pstmt2.executeUpdate();
         
         conn.commit();
         conn.setAutoCommit(true);
         
            %><h1>Hello new user!</h1><%
      }
    }
    catch (NullPointerException e) {
		%><h1>oops! Something went wrong!</h1><%
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
    }
%>	
	
    </body>
    <script>
        document.write('<script src=/js/vendor/'
        + ('__proto__' in {} ? 'zepto' : 'jquery')
        + '.js><\/script>');
    </script>
    <script src="js/foundation.min.js"></script>
    <script>$(document).foundation();</script>    
</html>