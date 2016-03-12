<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.*"  %>
    <%@ page import="com.UBC513.A3.Data.Flight" %>
    <%@ page import="com.google.appengine.api.datastore.Entity" %>
    <%@ page import="com.UBC513.A3.Data.Seat" %>
    <%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home</title>
<link href="Site.css" rel="Stylesheet" type="text/css"/>
</head>
<body>
	
	
	<div class="wrapper">
		<div id="Content">
			<div id="Header"><H1>Flights - V4</H1></div>
			<hr/>
			<div id="MainContent">

				<% Iterable<Entity> list = (Iterable<Entity>)request.getAttribute("flights"); %>
				
				<form action="SelectSeat" method="post">
				<table>
					<tr>
						<td>&nbsp;</td>
						<td><strong>Trip</strong></td>
					</tr>
					<tr>
						<td>Flight</td>
						<td>
							<select name="Flight">
								<option value="">Please select a flight.</option>
							<% for( Entity e : list ) { %>
								<option value="<%=e.getKey().getName()%>"><%=e.getKey().getName() %></option>
							<%} %>
							</select>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Select Seats"></td>
					</tr>
				</table>
				</form>
				
			</div><!-- end MainContent -->
		</div><!-- end Content -->
	</div><!-- end wrapper -->
</body>
</html>