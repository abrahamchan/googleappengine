package com.UBC513.A3;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.UBC513.A3.Data.Seat;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class SelectSeatServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String flight1 = req.getParameter("Flight1");
		String flight2 = req.getParameter("Flight2");
		String flight3 = req.getParameter("Flight3");
		String flight4 = req.getParameter("Flight4");
		
		Iterable<Entity> flightSeats1 = Seat.GetFreeSeats(flight1);
		Iterable<Entity> flightSeats2 = Seat.GetFreeSeats(flight2);
		Iterable<Entity> flightSeats3 = Seat.GetFreeSeats(flight3);
		Iterable<Entity> flightSeats4 = Seat.GetFreeSeats(flight4);
		
		req.setAttribute("flightSeats1", flightSeats1);
		req.setAttribute("flightSeats2", flightSeats2);
		req.setAttribute("flightSeats3", flightSeats3);
		req.setAttribute("flightSeats4", flightSeats4);
		
		//redirect to index.jsp
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/selectSeats.jsp");
		rd.forward(req, resp);
	}

}
