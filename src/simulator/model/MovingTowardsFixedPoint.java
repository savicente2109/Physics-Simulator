package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class MovingTowardsFixedPoint implements ForceLaws{
	
	public static final double g = 9.81;
	
	public Vector2D _c;
	public Double _g;
	
	public MovingTowardsFixedPoint() {
		_c = new Vector2D();
		_g = g;
	}
	
	public MovingTowardsFixedPoint(Vector2D c, Double g) {
		_c = c;
		_g = g;
	}
	
	public void apply(List<Body> bs) {
		
		for (Body i : bs) {
			Vector2D d = i.getPosition().minus(_c).direction();
			i.addForce(d.scale(-_g*i.getMass())); //Cambiamos la fuerza de cada cuerpo al modificar su aceleración 
		}
		
	}
	public String toString() {
		return "Moving towards " + _c + " with constant acceleration " + _g; 
	}
}
