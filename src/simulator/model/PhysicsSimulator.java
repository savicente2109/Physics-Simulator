package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PhysicsSimulator {
	
	private List<Body> arrayBodies;  
	private List<Body> arrayBodies_RO;
	private double time; 
	private double dt; 
	private ForceLaws forceLaws; 
	private List<SimulatorObserver> obsList; 
	
	public PhysicsSimulator(){
		dt = 2500.0; 
		forceLaws = new NewtonUniversalGravitation(); 
		time = 0.0; 
		arrayBodies = new ArrayList<Body>();
		arrayBodies_RO = Collections.unmodifiableList(arrayBodies);
		obsList = new ArrayList<SimulatorObserver>();
	}
	
	public PhysicsSimulator(double dtime, ForceLaws fl) throws IllegalArgumentException{
		if (fl == null) throw new IllegalArgumentException("Unable to create the Physics Simulator. There's no force law. ");
		if (dtime < 0) throw new IllegalArgumentException("Unable to create the Physics Simulator. Delta-Time must be a positive value. ");
		dt = dtime;
		forceLaws = fl;
		time = 0.0; 
		arrayBodies = new ArrayList<Body>();
		arrayBodies_RO = Collections.unmodifiableList(arrayBodies);
		obsList = new ArrayList<SimulatorObserver>();
	}
	
	public void setDeltaTime(double dt) throws IllegalArgumentException {
		if(dt<0) throw new IllegalArgumentException("Invalid arguments for the Physics Simulator. Delta-Time must be a positive value. "); 
		this.dt = dt; 
		for(SimulatorObserver s: obsList) {
			s.onDeltaTimeChanged(dt); 
		}
	}
	
	public void setForceLaws(ForceLaws forceLaws)  throws IllegalArgumentException {
		if(forceLaws == null) throw new IllegalArgumentException("Invalid arguments for the Physics Simulator. There's no force law. ");
		this.forceLaws = forceLaws;
		
		for(SimulatorObserver s: obsList) {
			s.onForceLawsChanged(forceLaws.toString()); 
		}
		
	}
	
	public void reset() {
		arrayBodies = new ArrayList<Body>(); 
		time = 0.0; 
		for(SimulatorObserver s: obsList) {
			s.onReset(arrayBodies, time, dt, forceLaws.toString()); 
		}
	}
	public void advance() {
		
		for (Body i : arrayBodies)
			i.resetForce();
		
		forceLaws.apply(arrayBodies);
		
		for (Body i : arrayBodies)
			i.move(dt);
		
		time += dt; 
		
		for(SimulatorObserver s: obsList) {
			s.onAdvance(arrayBodies, time); 
		}
	}
	
	public void addBody(Body b) throws IllegalArgumentException{
		
		if (arrayBodies.contains(b)) throw new IllegalArgumentException("All bodies must have distinct identificators.");
		
		arrayBodies.add(b);
		for(SimulatorObserver s: obsList) {
			s.onBodyAdded(arrayBodies, b); 
		}
		
	}
	
	public JSONObject getState() {
		JSONObject json = new JSONObject(); 
		
		json.put("time", time);
		
		JSONArray jsonarray = new JSONArray();
		
		for (Body i : arrayBodies)
			jsonarray.put(i.getState());
		
		json.put("bodies", jsonarray); 
		
		return json; 
	}
	
	public void addObserver(SimulatorObserver o) {
		boolean found = false; 
		for(SimulatorObserver s: obsList) {
			if(s==o) {found = true; break; }
		}
		if(!found) {
			obsList.add(o); 
			o.onRegister(arrayBodies, time, dt, forceLaws.toString());
		}
	}
	
	
	
	public String toString() {
		 
		return getState().toString(); 
		 
	}
	 

}

