package com.UBC513.A3.Helpers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

	public static void Process() 
	{
		for (Entity e: GetFreeSeatsTask()) {
			try {
				Queue checkpoint = QueueFactory.getQueue("checkpoint");
		        checkpoint.add(TaskOptions.Builder.withUrl("/FreeSeats").method(Method.GET));
				
				// Message logs if checkpoint is triggered and operation is successful
				System.out.println("Checkpoint item has been re-executed");
				
				DeleteFreeSeatsTask();
				
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
		}
	}
	
	// Get all flights in the datastore
	public static Iterable<Entity> GetFreeSeatsTask() {
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("FreeSeats");
		return ds.prepare(q).asIterable();
	}
	
	// Create Free Seats task queue
	public static Entity CreateFreeSeatsTask() throws Exception
	{
		Entity e = new Entity("FreeSeats");
		e.setProperty("date", new Date());
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(e);
		
		System.out.println("FreeSeats is checkpointed!");
		return e;
	}
	
	// Remove reservation from the datastore
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
	
}
