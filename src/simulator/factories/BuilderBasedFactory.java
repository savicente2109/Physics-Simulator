package simulator.factories;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONObject;

public class BuilderBasedFactory<T> implements Factory<T> {

	private List<Builder<T>> builders;
	private ArrayList<JSONObject> arrayJO;
	private List<JSONObject> arrayJO_RO;
	
	public BuilderBasedFactory(List<Builder<T>> builders) {
		
		this.builders = builders;
		arrayJO = new ArrayList<JSONObject>();
		
		for (Builder<T> builder : builders)
			arrayJO.add(builder.getBuilderInfo());
		
		arrayJO_RO = Collections.unmodifiableList(arrayJO);
	}

	@Override
	public T createInstance(JSONObject info) throws IllegalArgumentException {
		
		T aux;
			
		for (Builder<T> builder : builders) {
			aux = builder.createInstance(info);
			if (aux != null)
				return aux;
		}
		
		throw new IllegalArgumentException("Unable to create instance. Invalid type in JSON.");
	}

	@Override
	public List<JSONObject> getInfo() {
		return arrayJO_RO;
	}

}
