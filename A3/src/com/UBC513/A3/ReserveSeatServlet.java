package com.UBC513.A3;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.UBC513.A3.Data.Seat;
import com.UBC513.A3.Data.SeatReservation;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class ReserveSeatServlet extends HttpServlet {
	

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		// Get parameters
		String Flight1 = req.getParameter("Flight1");		
		String Flight1Seat = req.getParameter("SeatID1");
		String Flight2 = req.getParameter("Flight2");		
		String Flight2Seat = req.getParameter("SeatID2");
		String Flight3 = req.getParameter("Flight3");		
		String Flight3Seat = req.getParameter("SeatID3");
		String Flight4 = req.getParameter("Flight4");		
		String Flight4Seat = req.getParameter("SeatID4");
		
		String FirstName = req.getParameter("FirstName");
		String LastName = req.getParameter("LastName");
		
		String forwardTo = "/seatConfirmation.jsp";
		try {
			if (!Seat.ReserveSeats(Flight1, Flight1Seat,
					Flight2, Flight2Seat, Flight3,
					Flight3Seat, Flight4, Flight4Seat,
					FirstName, LastName)) {
				// seat not reserved, show error page
				forwardTo = "/reserveSeatWaiting.jsp";
			}
		} catch (EntityNotFoundException e) {
			// seat not found, show error page
			forwardTo = "/reserveSeatError.jsp";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// redirect to final page
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher(forwardTo);
		rd.forward(req, resp);
	}
}
