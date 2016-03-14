package com.UBC513.A3.Helpers;

import com.UBC513.A3.Data.Seat;
import com.UBC513.A3.Data.SeatReservation;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;

public class Worker {

	public static void Process() 
	{
		for (Entity e: SeatReservation.GetReservations()) {
			
			String Flight1 = (String) e.getProperty("Flight1");
			String Flight1Seat = (String) e.getProperty("Flight1Seat");
			String Flight2 = (String) e.getProperty("Flight2");		
			String Flight2Seat = (String) e.getProperty("Flight2Seat");
			String Flight3 = (String) e.getProperty("Flight3");		
			String Flight3Seat = (String) e.getProperty("Flight3Seat");
			String Flight4 = (String) e.getProperty("Flight4");			
			String Flight4Seat = (String) e.getProperty("Flight4Seat");
			
			String FirstName = (String) e.getProperty("FirstName");
			String LastName = (String) e.getProperty("LastName");
			
			try {
				if (Seat.ReserveSeats(Flight1, Flight1Seat,
						Flight2, Flight2Seat, Flight3,
						Flight3Seat, Flight4, Flight4Seat,
						FirstName, LastName)) {
					// waitlist seat reserved
					
					SeatReservation.DeleteReservation(e);
				}
			} catch (EntityNotFoundException ex) {
				// seat not found, show error page
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
		}
	}
}
