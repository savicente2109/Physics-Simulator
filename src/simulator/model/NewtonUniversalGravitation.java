package simulator.model;

import java.util.List;

import simulator.misc.Vector2D;

public class NewtonUniversalGravitation implements ForceLaws{

	public static final double G = 6.67E-11; 
	
	private double _G;
	
	public NewtonUniversalGravitation() {
		_G = G;
	}
	
	public NewtonUniversalGravitation(Double G) {
		_G = G;
	}
	
	public void apply(List<Body> bs) {
		
		for (Body i : bs)
			for (Body j : bs)
				if (i != j)
					if (i.getMass() != 0.0) {
						double d = i.getPosition().distanceTo(j.getPosition()), f;
						if (d != 0.0)
							f = _G*i.getMass()*j.getMass()/(Math.pow(d, 2));
						else 
							f = 0.0;
						
						Vector2D pos_j = j.getPosition(), pos_i = i.getPosition();
						
						Vector2D dij = (pos_j.minus(pos_i)).direction();
						
						Vector2D Fij = dij.scale(f); 
						
						i.addForce(Fij); 
					}
	}
	public String toString() {
		return "Newton’s Universal Gravitation with G=" + _G; 
	}
}
