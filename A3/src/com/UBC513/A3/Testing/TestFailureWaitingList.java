package com.UBC513.A3.Testing;

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;


import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.UBC513.A3.Data.Flight;
import com.UBC513.A3.Data.Seat;
import com.UBC513.A3.Data.SeatReservation;
import com.UBC513.A3.Helpers.Worker;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

public class TestFailureWaitingList extends TestCase {

	private static boolean flag = false;
	@Override
	protected void setUp() throws Exception {

	}

	@Override
	protected void tearDown() throws Exception {
		
	}

	public void testSuccessfulReservation() throws Exception
	{
		//=================================================================
		//Prep
		//=================================================================
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		List<Entity> l = new ArrayList<Entity>();

		Flight.CreateFlight("F2491", true);

		// Create seats for flight
		for (int i = 1; i < 5; i++) {
			for (int c = 'A'; c <= 'D'; c += 1) {
				l.add(Seat.CreateSeat(String.format("%d%c", i, c), "F2491", false));
			}
		}

		// put all seats into datastore
		ds.put(l);
		
		Seat.ReserveSeat("F2491", "1A", "Another", "Person");
		SeatReservation.CreateReservation("F2491", "1A", "F2491", "2A", "F2491", "3A", "F2491", "4A", "John", "Doe", true );
		
		//let datastore catch up
		Thread.sleep(10000);
		
		int sum = 0;
		Iterable<Entity> seats = Seat.GetFreeSeats("F2491");
		for(Entity e : seats )
		{
			//check to make sure all are empty
			assertNull( e.getProperty("PersonSitting") );
			sum++;
		}
		assertEquals(15, sum );
		
		//=================================================================
		// Perform Reservation
		//=================================================================
		
		Worker.Process();
		
		//=================================================================
		// Check Results
		//=================================================================
		
		//let datastore catch up
		Thread.sleep(10000);
		
		sum = 0;
		seats = Seat.GetFreeSeats("F2491");
		for(Entity e : seats )
		{
			//check to make sure all are empty
			assertNull( e.getProperty("PersonSitting") );
			sum++;
		}
		assertEquals(15, sum );
		
	}
}
