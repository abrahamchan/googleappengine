package com.UBC513.A3.Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.UBC513.A3.Data.Seat;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class Checkpoint {

	// Main Checkpoint Resuming Process
	public static void Process() 
	{
		ResumeFreeSeats();
		ResumeReserveSeat();
	}
	
	private static void ResumeFreeSeats() {
		for (Entity e: GetFreeSeatsTask()) {
			try {
				Queue checkpoint = QueueFactory.getQueue("checkpoint");
		        checkpoint.add(TaskOptions.Builder.withUrl("/FreeSeats").method(Method.GET));
				
				// Message logs if checkpoint is triggered
				System.out.println("Checkpoint FreeSeats has been re-executed");
				
				DeleteFreeSeatsTask();
				
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}
	
	private static void ResumeReserveSeat() {
		for (Entity e: GetReserveSeatsTask()) {
			
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
					
					// Message logs if checkpoint is triggered
					System.out.println("Checkpoint ReserveSeat has been re-executed");
					
					DeleteReserveSeatTask();
				}
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
		}
	}
	
	// Get all outstanding FreeSeats tasks in the datastore
	public static Iterable<Entity> GetFreeSeatsTask() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("FreeSeats");
		return ds.prepare(q).asIterable();
	}
	
	// Create FreeSeat in datastore
	public static Entity CreateFreeSeatsTask() throws Exception
	{
		Entity e = new Entity("FreeSeats");
		e.setProperty("date", new Date());
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(e);
		
		System.out.println("FreeSeats is checkpointed!");
		return e;
	}
	
	// Remove FreeSeat from the datastore
	public static void DeleteFreeSeatsTask() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//Deletes all flights in datastore
		Query q = new Query("FreeSeats");
		Iterable<Entity> tasks = ds.prepare(q).asIterable();

		List<Key> keys = new ArrayList<Key>();
		for( Entity e : tasks)
			keys.add(e.getKey());
		
		ds.delete(keys);
	}
	
	// Get all outstanding ReserveSeat tasks in the datastore
	public static Iterable<Entity> GetReserveSeatsTask() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("ReserveSeat");
		return ds.prepare(q).asIterable();
	}
	
	// Create ReserveSeat in datastore
	public static Entity CreateReserveSeatTask(String Flight1, String Flight1Seat, String Flight2, String Flight2Seat, String Flight3,
			String Flight3Seat, String Flight4, String Flight4Seat, String FirstName, String LastName) throws Exception
	{
		Entity e = new Entity("ReserveSeat");
		e.setProperty("Flight1", Flight1);
		e.setProperty("Flight2", Flight2);
		e.setProperty("Flight3", Flight3);
		e.setProperty("Flight4", Flight4);
		
		e.setProperty("Flight1Seat", Flight1Seat);
		e.setProperty("Flight2Seat", Flight2Seat);
		e.setProperty("Flight3Seat", Flight3Seat);
		e.setProperty("Flight4Seat", Flight4Seat);
		
		e.setProperty("FirstName", FirstName);
		e.setProperty("LastName", LastName);
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(e);
		
		System.out.println("ReserveSeat is checkpointed!");
		return e;
	}
	
	// Remove ReserveSeat from the task datastore
	public static void DeleteReserveSeatTask() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		
		//Deletes all flights in datastore
		Query q = new Query("ReserveSeat");
		Iterable<Entity> tasks = ds.prepare(q).asIterable();

		List<Key> keys = new ArrayList<Key>();
		for( Entity e : tasks)
			keys.add(e.getKey());
		
		ds.delete(keys);
	}
	
}
