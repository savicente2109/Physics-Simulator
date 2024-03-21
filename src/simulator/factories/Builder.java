package simulator.factories;

import org.json.JSONObject;

public class Builder<T> {

	protected String _typeTag;
	protected String _desc;
	
	public Builder() {
		// TODO Auto-generated constructor stub
	}
	
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		
		if (_typeTag.equals(info.getString("type"))) {
			return createTheInstance(info.getJSONObject("data"));
		}
			
		else return null;
	}
	
	public JSONObject getBuilderInfo() {
		
		JSONObject jo = new JSONObject();
		
		jo.put("type", _typeTag);
		jo.put("data", createData());
		jo.put("desc", _desc);
		
		return jo;
	}
	
	public T createTheInstance(JSONObject jo) {
		return null;
	}
	
	public JSONObject createData() {
		return null;
	}

}
