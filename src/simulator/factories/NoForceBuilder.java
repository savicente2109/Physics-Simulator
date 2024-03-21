package simulator.factories;

import org.json.JSONObject;

import simulator.model.ForceLaws;
import simulator.model.MovingTowardsFixedPoint;
import simulator.model.NoForce;

public class NoForceBuilder extends Builder<ForceLaws> {

	private static final String TYPETAG = "nf";
	private static final String DESC ="No force";
	
	public NoForceBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public NoForce createTheInstance(JSONObject jo) {
		return new NoForce();
	}
	
	public JSONObject createData() {
		return new JSONObject();
	}
	
}
