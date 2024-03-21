package simulator.model;

import org.json.JSONObject;

import simulator.misc.Vector2D;

public class Body {
	protected String id; 
	protected Vector2D fuerza; 
	protected Vector2D velocidad; 
	protected Vector2D posicion;
	protected double masa; 
	
	public Body(String id, Vector2D velocidad, Vector2D posicion, double masa){
		fuerza = new Vector2D(); 
		this.id = id; 
		this.velocidad = velocidad; 
		this.posicion = posicion; 
		this.masa = masa; 
	}
	
	public String getId() {
		return id; 
	}
	public Vector2D getForce() {
		return fuerza; 
	}
	public Vector2D getVelocity() {
		return velocidad; 
	}
	
	public Vector2D getPosition() {
		return posicion; 
	}
	
	public double getMass() {
		return masa; 
	}
	
	protected void addForce(Vector2D f) {
		fuerza = fuerza.plus(f);
	}
	
	protected void resetForce() {
		 fuerza = new Vector2D(); 
	 }
	 
	 protected void move(double t) {
		 Vector2D aceleracion = new Vector2D();
		 
		 if(masa != 0.0) {aceleracion = fuerza.scale(1.0/masa); }
		
		 posicion = posicion.plus(velocidad.scale(t).plus(aceleracion.scale(t*t*0.5)));
		 velocidad = velocidad.plus(aceleracion.scale(t));		 
	 }
	 
	 public JSONObject getState() {
		 JSONObject json = new JSONObject(); 
		 
		 json.put("id", id); 
		 json.put("m", masa); 
		 json.put("p", posicion.asJSONArray());
		 json.put("v", velocidad.asJSONArray()); 
		 json.put("f", fuerza.asJSONArray());
		 
		 return json; 
	 }
	 
	 
	 public String toString() {
		 return getState().toString();
	 }
	 
	 // Sobreescribimos el método equals
	 public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Body other = (Body) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}
}
