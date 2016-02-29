package com.UBC513.A2.Data;

import java.util.ConcurrentModificationException;
import java.util.Random;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Transaction;

//Helper class for flight seats.
public class Seat {
	
	private static final int NUM_SHARDS = 1000;
	private static final Random generator = new Random();

	// Create a seat on a specific flight,
	// @store = true, when you want to commit entity to the datastore
	// = false, when you want to commit entity later, like in a batch operation
	public static Entity CreateSeat(String SeatID, Key FlightKey, boolean store) {
		Entity e = new Entity("Seat", SeatID, FlightKey);
		e.setProperty("PersonSitting", null);

		if (store) {
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			ds.put(e);
		}

		return e;
	}

	// Frees specific seat(SeatID) on flight(FlightKey)
	public static void FreeSeat(String SeatID, Key FlightKey) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		try {
			Entity e = ds.get(KeyFactory.createKey(FlightKey, "Seat", SeatID));

			e.setProperty("PersonSitting", null);
			ds.put(e);
		} catch (EntityNotFoundException e) {
		}
	}

	//Returns all free seats on a specific flight(FlightKey)
	public static Iterable<Entity> GetFreeSeats(Key FlightKey) {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
//		Query q = new Query("Seat").setAncestor(FlightKey).addFilter(
//				"PersonSitting", FilterOperator.EQUAL, null);
		Query q = new Query("Seat").setAncestor(FlightKey).setFilter(new Query.FilterPredicate(
				"PersonSitting", FilterOperator.EQUAL, null));
		return ds.prepare(q).asIterable();
	}

	//Reserves a specific seat(SeatID) on a specific flight(FlightKey)
	public static boolean ReserveSeat(Key FlightKey, String SeatID,
			String FirstName, String LastName) throws EntityNotFoundException {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		int shardNum = generator.nextInt(NUM_SHARDS);
		
		Transaction tx = ds.beginTransaction();
		
		try {
			Key shardKey = new KeyFactory.Builder(FlightKey.toString(), Integer.toString(shardNum)).addChild("Seat",SeatID).getKey();
			Entity shard;
			
			try { //If the sharding scheme exist, then put the value into it;
				shard = ds.get(tx, shardKey);
				if (shard.getProperty("PersonSitting") != null)
					return false;
				shard.setProperty("PersonSitting", FirstName + " " + LastName); 
			} catch (EntityNotFoundException e) {
				shard = new Entity(shardKey);
                if (shard.getProperty("PersonSitting") != null)
					return false;
				shard.setProperty("PersonSitting", FirstName + " " + LastName);
            }
			ds.put(tx, shard);
			tx.commit();
			return true;
			
		} catch (ConcurrentModificationException e) {
			System.out.println("You may need more shards. Consider adding more shards.");
			throw new ConcurrentModificationException();
        } finally {
        	//restart to commit again if failed for transaction
            if (tx.isActive()) {
                tx.rollback();  
            }
        }
		
	}
}
