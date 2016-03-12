package com.UBC513.A3.Helpers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.UBC513.A3.Data.Flight;
import com.UBC513.A3.Data.Seat;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class PrepServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Entity> l = new ArrayList<Entity>();

		Flight.CreateFlight("F2491", true);
		Flight.CreateFlight("F2539", true);
		Flight.CreateFlight("F3519", true);

		// Create seats for flight
		for (int i = 1; i < 5; i++) {
			for (int c = 'A'; c <= 'D'; c += 1) {
				l.add(Seat.CreateSeat(String.format("%d%c", i, c), "F2491", false));
				l.add(Seat.CreateSeat(String.format("%d%c", i, c), "F2539", false));
				l.add(Seat.CreateSeat(String.format("%d%c", i, c), "F3519", false));
			}
		}

		// put all seats into datastore
		ds.put(l);

		// redirect to prepDone.jsp
		ServletContext sc = getServletContext();
		RequestDispatcher rd = sc.getRequestDispatcher("/prepDone.jsp");
		rd.forward(req, resp);
	}
}
