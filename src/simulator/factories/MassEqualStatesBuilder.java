package simulator.factories;

import org.json.JSONObject;

import simulator.control.MassEqualStates;
import simulator.control.StateComparator;
import simulator.model.NoForce;

public class MassEqualStatesBuilder extends Builder<StateComparator> {

	private static final String TYPETAG = "masseq";
	private static final String DESC = "Comparator based on mass and time equality.";
	
	public MassEqualStatesBuilder() {
		_typeTag = TYPETAG;
		_desc = DESC;
	}
	
	public MassEqualStates createTheInstance(JSONObject jo) {
		
		return new MassEqualStates();
	}
	
	public JSONObject createData() {
		return new JSONObject();
	}
	
}
