package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.Body;

public class BasicBodyBuilder extends Builder<Body> {

	private static final String TYPETAG = "basic";
	private static final String DESC = "Default body.";
	
	public BasicBodyBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public Body createTheInstance(JSONObject jo)throws IllegalArgumentException {
		try {
			if (jo.getDouble("m") < 0.0)
				throw new IllegalArgumentException("Unable to create the Body. Mass must be a positive value.");
		JSONArray v = jo.getJSONArray("v"), p = jo.getJSONArray("p");
		if((v.length() != 2)||(p.length() != 2)) {throw new IllegalArgumentException("Unable to create the Body. Vector must be 2d"); }
		return new Body(jo.getString("id"), new Vector2D(v.getDouble(0), v.getDouble(1)), new Vector2D(p.getDouble(0), p.getDouble(1)), jo.getDouble("m"));
	
		}
		catch (JSONException e) {
			throw new IllegalArgumentException("Unable to create the Body. " + e.getMessage());
		}
		
	}
	
	public JSONObject createData() {
		
		JSONObject jo = new JSONObject();
		jo.put("id", "b1");

		JSONArray ja = new JSONArray();
		ja.put(0.0e00);
		ja.put(0.0e00);
		jo.put("p", ja);
		
		ja = new JSONArray();
		ja.put(0.05e04);
		ja.put(0.0e00);
		jo.put("v", ja);
		
		jo.put("m", 5.97e24);
		
		return jo;
	}
	
}
