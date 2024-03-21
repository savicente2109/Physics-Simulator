package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.NewtonUniversalGravitation;

public class NewtonUniversalGravitationBuilder extends Builder<ForceLaws> {

	private static final String TYPETAG = "nlug";
	private static final String DESC = "Newton’s law of universal gravitation";
	
	public NewtonUniversalGravitationBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public NewtonUniversalGravitation createTheInstance(JSONObject jo) throws IllegalArgumentException {
		try {
			if (!jo.has("G"))
				jo.put("G", NewtonUniversalGravitation.G);
			return new NewtonUniversalGravitation(jo.getDouble("G"));
		}
		catch (JSONException e) {
			throw new IllegalArgumentException("Unable to create the NewtonUniversalGravitation forceLaw. " + e.getMessage());
		}
	}
	
	public JSONObject createData() {
		
		JSONObject jo = new JSONObject();
		jo.put("G", "the gravitational constant (a number)");
		
		return jo;
	}
	
}
