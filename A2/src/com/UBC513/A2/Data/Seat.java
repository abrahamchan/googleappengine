package com.UBC513.A2.Data;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;

import java.io.UnsupportedEncodingException;
import javax.xml.bind.DatatypeConverter;


//Helper class for flight seats.
public class Seat {
	
	// Create a seat on a specific flight,
	// @store = true, when you want to commit entity to the datastore
	// = false, when you want to commit entity later, like in a batch operation
	public static Entity CreateSeat(String SeatID, String FlightKey, boolean store) {
		Entity e = new Entity("Seat", getSeatFlightStringKey(SeatID, FlightKey));
		e.setProperty("PersonSitting", null);
		e.setProperty("FlightKey", FlightKey);

		if (store) {
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			ds.put(e);
		}

		return e;
	}

	// Frees specific seat(SeatID) on flight(FlightKey)
	public static void FreeSeat(String SeatID, String FlightKey) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity e = ds.get(KeyFactory.createKey("Seat", getSeatFlightStringKey(SeatID, FlightKey)));

			e.setProperty("PersonSitting", null);

			ds.put(e);
		} catch (EntityNotFoundException e) {
		}
	}

	//Returns all free seats on a specific flight(FlightKey)
	public static Iterable<Entity> GetFreeSeats(Key FlightKey) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Query.Filter flightFilter = new Query.FilterPredicate(
				"FlightKey", FilterOperator.EQUAL, FlightKey.getName());
		
		Query.Filter personFilter = new Query.FilterPredicate(
				"PersonSitting", FilterOperator.EQUAL, null);
		
		Query.Filter validFilter = Query.CompositeFilterOperator.and(flightFilter, personFilter);
		
		Query q = new Query("Seat").setFilter(validFilter);
		
		return ds.prepare(q).asIterable();
	}

	//Reserves a specific seat(SeatID) on a specific flight(FlightKey)
	public static boolean ReserveSeat(String FlightKey, String SeatID,
			String FirstName, String LastName) throws EntityNotFoundException, UnsupportedEncodingException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity e;

		Transaction tx = ds.beginTransaction();

		try {
			if (!SeatID.contains("-")) {
				e = ds.get(tx, KeyFactory.createKey("Seat", getSeatFlightStringKey(SeatID, getFlightName(FlightKey))));
			}
			else {
				e = ds.get(tx, KeyFactory.createKey("Seat", SeatID));
			}
			
			if (e.getProperty("PersonSitting") != null)
				return false;

			e.setProperty("PersonSitting", FirstName + " " + LastName);
			
			ds.put(tx, e);

			return true;
		} finally {
			tx.commit();
		}
	}
	
	private static String getSeatFlightStringKey(String SeatID, String FlightKey) {
		return SeatID + "-" + FlightKey;
	}
	
	private static String getFlightName(String FlightKey) {
	    String flightdecoded = decode(FlightKey);
	    return flightdecoded.substring(flightdecoded.length()-6, flightdecoded.length()-1);
	}
	
	private static String decode(String s) {
	    return new String(DatatypeConverter.parseBase64Binary(s));
	}
	
}
