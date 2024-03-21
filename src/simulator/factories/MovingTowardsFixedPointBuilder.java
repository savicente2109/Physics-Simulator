package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;
import simulator.model.NewtonUniversalGravitation;

public class MovingTowardsFixedPointBuilder extends Builder<ForceLaws> {

	private static final String TYPETAG = "mtfp";
	private static final String DESC = "Moving towards a fixed point";
	
	public MovingTowardsFixedPointBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public MovingTowardsFixedPoint createTheInstance(JSONObject jo) throws IllegalArgumentException{
		
		try {
			if (!jo.has("c")) {
				JSONArray c = new JSONArray();
				c.put(0.0);
				c.put(0.0);
				jo.put("c", c);
			}
			if (!jo.has("g"))
				jo.put("g", MovingTowardsFixedPoint.g);
			JSONArray ja = jo.getJSONArray("c");
			if(ja.length() != 2)
				throw new IllegalArgumentException("Unable to create the Body. Vector must be 2D.");
			
			return new MovingTowardsFixedPoint(new Vector2D(ja.getDouble(0), ja.getDouble(1)), jo.getDouble("g"));
		}
		
		catch (JSONException e) {
			throw new IllegalArgumentException("Unable to create the MovingTowardsFixedPoint forceLaw. " + e.getMessage());
		}
	}
	
	public JSONObject createData() {
		
		JSONObject jo = new JSONObject();
		
		
		jo.put("c", "the point towards which bodies move (a json list of 2 numbers, e.g., [100.0,50.0])");
		
		jo.put("g", "the length of the acceleration vector (a number)");
		
		return jo;
	}
	
}
