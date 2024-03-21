package simulator.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Body;
import simulator.model.ForceLaws;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;

public class Controller {
	
	private PhysicsSimulator physicsSimulator;
	private Factory<Body> factoryBodies;
	private Factory<ForceLaws> factoryLaws; 

	public Controller() {
		// TODO Auto-generated constructor stub
	}
	
	public Controller(PhysicsSimulator physicsSimulator, Factory<Body> factoryBodies, Factory<ForceLaws> factoryLaws) {
		this.physicsSimulator = physicsSimulator;
		this.factoryBodies = factoryBodies;
		this.factoryLaws = factoryLaws; 
	}
	
	public void loadBodies(InputStream in) throws Exception {
		JSONObject jsonInupt = new JSONObject(new JSONTokener(in));
		
		JSONArray ja = jsonInupt.getJSONArray("bodies");
		
		for (int i = 0; i < ja.length(); i++) {
			physicsSimulator.addBody(factoryBodies.createInstance(ja.getJSONObject(i)));
			// crea una instancia de Body del JSONARRAY de bodies y la añade al simulador 
		}
		
	}
	
	public void run(int n, OutputStream out, InputStream expOut, StateComparator cmp) throws Exception {
		
		// AÑADIR PARA QUE MODO GUI NO ESCRIBA
		
		PrintStream p = new PrintStream(out);
		JSONObject jsonInput;
		JSONArray ja = new JSONArray();
		
		p.println("{");
		p.println("\"states\": [");
		
		p.println(physicsSimulator);
		
		
		if (expOut != null) {
			jsonInput = new JSONObject(new JSONTokener(expOut));
			ja = jsonInput.getJSONArray("states");
			if (!cmp.equal(physicsSimulator.getState(), ja.getJSONObject(0)))
				throw new NotEqualStatesException("Mismatch in step 0. Output: "+ physicsSimulator + ". Expected output: " + ja.getJSONObject(0));
		}
		
		for (int i = 1; i <= n; ++i) {
			physicsSimulator.advance();
			p.println("," + physicsSimulator);
			if (expOut != null) {
				if (!cmp.equal(physicsSimulator.getState(), ja.getJSONObject(i)))
					throw new NotEqualStatesException("Mismatch in step " + i +". Output: "+physicsSimulator + ". Expected output: " + ja.getJSONObject(i));
			}
		}
		
		p.println("]");
		p.println("}");
	}
	
	public void reset() {
		physicsSimulator.reset(); 
	}
	
	public void setDeltaTime(double dt) {
		physicsSimulator.setDeltaTime(dt);
	}
	public void addObserver(SimulatorObserver o) {
		physicsSimulator.addObserver(o);
	}
	
	public List<JSONObject> getForceLawsInfo() {
		return factoryLaws.getInfo();
	}
	
	public void setForceLaws(JSONObject info) throws IllegalArgumentException {
		physicsSimulator.setForceLaws(factoryLaws.createInstance(info));
	}
	
	public void run(int n) throws Exception {
		
		OutputStream os = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			};
		};

		run(n, os, null, null);
	}
}
