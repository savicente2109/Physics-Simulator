package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;
import simulator.model.NewtonUniversalGravitation;

public class EpsilonEqualStatesBuilder extends Builder<StateComparator> {

	private static final String TYPETAG = "epseq";
	private static final String DESC = "Comparator based on epsilon module equality.";
	
	public EpsilonEqualStatesBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public EpsilonEqualStates createTheInstance(JSONObject jo) throws IllegalArgumentException {
		
		try {
			if (!jo.has("eps"))
				jo.put("eps", EpsilonEqualStates.EPS);
			return new EpsilonEqualStates(jo.getDouble("eps"));
		}
		catch (JSONException e) {
			throw new IllegalArgumentException("Unable to create the Epsilon comparator. " + e.getMessage());
			
		}
	}
	
	public JSONObject createData() {
		JSONObject jo = new JSONObject();
		
		jo.put("eps", 0.1);
		
		return jo;
	}
	
}
