<html lang="en-US">
    <head>
        <meta charset="UTF-8">
        <title>Blah</title>
        <link rel="stylesheet" href="css/foundation.css">
        <script src="js/vendor/custom.modernizr.js"></script>        
    </head>
    <body>
<%@page import="java.sql.*"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.*"%>

<%

	String fb_id = request.getParameter("acc");

	//recipe names adj and nouns to mix and match
	String [] recNamesA = {"tasty","fast","electrifying","delicious","magnificent","smelly","disgusting", "bland",
						   "stingy","spooky","remarkable","rare","used","cultured","even", "feeble"};
	String [] recNamesN = {" chicken"," cranberry"," eggs"," taco"," beef"," cow brain"," crepe", " omelette",
						   " beer"," cod"," cocoa"," pound cake"," toast"," pasta"," wine", " duck"}; 
	String [] recNamesN2 = {" and broccoli", " and horseradish", " and apples", " and grapes", " and curry", " and syrup",
						   " and soup", " and pie"};

	String [] ingredients = {"oyster", "banana", "rice", "protein powder", "broccoli", "steak", "flour", "lime juice",
							"cheese", "beans", "peaches", "parsley", "lentils", "ham", "scallion", "mango"};
	String [] qty = {"3 oz", "4 lbs", "1 cup", "3 tablespoon", "8 dozen", "2 liters", "1 pint", "5 fluid oz"};
   String [] lastStep = {"serve and enjoy and remember to always look both ways before crossing the street.",
                         "take your knife and chop the onions or you're banished forever. enjoy!",
                         "go eat and please do not feed the dogs chocolate.",
                         "do not underestimate the power of food. you are now hungry. go eat.",
                         "enjoy your meal and have a cookalicious time!"};
	//random fields 
	int randomIngredientCountMax = (int)((Math.random()*4)+1);
	int l = 16;
	String recNameBuilder;
	String randomIngredient;
	String randomQty;
	ArrayList<String> ingredient = new ArrayList<String>(4);
	ArrayList<String> ingredientQty = new ArrayList<String>(4);
	ArrayList<String> instructionTitle = new ArrayList<String>(3);
	ArrayList<String> instructionDesc = new ArrayList<String>(3);
	recNameBuilder = recNamesA[(int) (Math.random() * l)];
	recNameBuilder += recNamesN[(int) (Math.random() * l)];
	recNameBuilder += recNamesN2[(int) (Math.random() * 8)];
	%>
		<h1>Generated recipe:</h1> 
		<h3><%=recNameBuilder%></h3>
		<h2>your ingredients</h2>
	<%	
	//generate a random number of recipes with random names and qty
	for(int i = 0; i < randomIngredientCountMax; i++) {
		int ci = (int)(Math.random()*l);
		String ring = ingredients[ci];
		String rqty = qty[ci/2];
		ingredient.add(ring);
		ingredientQty.add(rqty);
		%>
		<h3><%=ingredientQty.get(i)%> of <%=ingredient.get(i)%></h3>
		<%
	}
   %><h2>your steps</h2><%
	int ci2 = (int)(Math.random()*8);
   int last = (int)(Math.random()*5);
	String rins = ingredients[ci2];
	instructionTitle.add("prep the " + rins);
	instructionDesc.add("lay out the " + rins + ". Don't die");
	instructionTitle.add("cook the " + rins);
	instructionDesc.add("shove " + rins + " in the oven. Be happy");
	instructionTitle.add("serve");
	instructionDesc.add(lastStep[last]);
   for(int i = 0; i < instructionDesc.size(); i++) {
      %><h3><%=instructionDesc.get(i)%></h3><%
   }
	
	Connection conn = null;
	PreparedStatement pstmt = null;

	try {
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/CookPal", "root", "");
		Statement stmt = conn.createStatement();
		
		String query = "INSERT INTO recipe (name, account_id, cookbook_type, image_url) VALUES (?, ?, ?, ?)";
		pstmt = conn.prepareStatement(query);     

		conn.setAutoCommit(false);       
		pstmt.setString(1, recNameBuilder);
		pstmt.setString(2, fb_id);
		pstmt.setString(3, "private");
		pstmt.setString(4, "");
		pstmt.executeUpdate(); 
		conn.commit();
		conn.setAutoCommit(true);      
		for(int i = 0; i < ingredient.size(); i++) {
			query = "INSERT INTO recipe_ingredient (recipe_name, account_id, name, quantity) VALUES (?, ?, ?, ?)";
			pstmt = conn.prepareStatement(query);     

			conn.setAutoCommit(false);       
			pstmt.setString(1, recNameBuilder);
			pstmt.setString(2, fb_id);
			pstmt.setString(3, ingredient.get(i));
			pstmt.setString(4, ingredientQty.get(i));
			pstmt.executeUpdate(); 
			conn.commit();
			conn.setAutoCommit(true);     
		}	

		for(int i = 0; i < 3; i++) {
			query = "INSERT INTO recipe_instruction (recipe_name, account_id, instruction, hrs, mins, step_no, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
			int randTimeHr = (int) (Math.random()*24);
			int randTimeMin = (int) (Math.random()*60);
			pstmt = conn.prepareStatement(query);     
			conn.setAutoCommit(false);       
			pstmt.setString(1, recNameBuilder);
			pstmt.setString(2, fb_id);
			pstmt.setString(3, instructionTitle.get(i));
			pstmt.setInt(4, randTimeHr);
			pstmt.setInt(5, randTimeMin);
			pstmt.setInt(6, i+1);         
			pstmt.setString(7, instructionDesc.get(i));
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
      <a href="index.html" class="button small">Go back!</a>

   </body>
   <script>
        document.write('<script src=/js/vendor/'
        + ('__proto__' in {} ? 'zepto' : 'jquery')
        + '.js><\/script>');
    </script>
    <script src="js/foundation.min.js"></script>
    <script>$(document).foundation();</script>
</html>