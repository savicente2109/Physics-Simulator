package simulator.model;

import simulator.misc.Vector2D;

public class MassLosingBody extends Body {
	protected double lossFactor; 
	protected double lossFrequency; 
	private double c; 

	public MassLosingBody(String id, Vector2D velocidad, Vector2D posicion, double masa, double lossFactor, double lossFrequency){
		super(id, velocidad, posicion, masa); 
		this.lossFactor = lossFactor; 
		this.lossFrequency = lossFrequency; 
		this.c = 0.0; 
	}
	
	protected void move(double t) {
		 super.move(t);
		 c+=t; 
		 if(c >= lossFrequency) {
			 masa *= (1-lossFactor); 
			 c = 0.0; 
		 }
		 
	}
}
