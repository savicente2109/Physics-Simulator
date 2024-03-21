package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.misc.Vector2D;
import simulator.model.Body;
import simulator.model.MassLosingBody;

public class MassLosingBodyBuilder extends Builder<Body> {

	private static final String TYPETAG = "mlb";
	private static final String DESC = "Body that loses its mass with a loss factor and frequency.";
	
	public MassLosingBodyBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public MassLosingBody createTheInstance(JSONObject jo) throws IllegalArgumentException{
		
		try {
			if (jo.getDouble("m") < 0.0)
				throw new IllegalArgumentException("Unable to create the Body. Mass must be a positive value.");
			if (jo.getDouble("freq") < 0.0)
				throw new IllegalArgumentException("Unable to create the Body. Loss Frequency must be a positive value.");
			Double factor = jo.getDouble("factor");
			if (factor < 0.0 || factor > 1.0)
				throw new IllegalArgumentException("Unable to create the Body. Loss Factor must be a value between 0 and 1.");
			JSONArray v = jo.getJSONArray("v"), p = jo.getJSONArray("p");
			if((v.length() != 2)||(p.length() != 2)) {throw new IllegalArgumentException("Unable to create the Body. Vector must be 2D"); }
			return new MassLosingBody(jo.getString("id"), new Vector2D(v.getDouble(0), v.getDouble(1)),
					new Vector2D(p.getDouble(0), p.getDouble(1)), jo.getDouble("m"),
					jo.getDouble("factor"), jo.getDouble("freq"));
		}
		catch (JSONException e) {
			throw new IllegalArgumentException("Unable to create the MassLosingBody. " + e.getMessage());
		}
		
	}
	
	public JSONObject createData() {
		
		JSONObject jo = new JSONObject();
		jo.put("id", "b1");

		JSONArray ja = new JSONArray();
		ja.put(-3.5e10);
		ja.put(0.0e00);
		jo.put("p", ja);
		
		ja = new JSONArray();
		ja.put(0.0e00);
		ja.put(1.4e03);
		jo.put("v", ja);
		
		jo.put("m", 3.0e28);
		
		jo.put("freq", 1e3);
		
		jo.put("factor", 1e-3);
		
		return jo;
	}
	
}
