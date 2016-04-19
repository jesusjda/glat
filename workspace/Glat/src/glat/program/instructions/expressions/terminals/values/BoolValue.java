package glat.program.instructions.expressions.terminals.values;

import glat.program.instructions.expressions.terminals.Values;

public class BoolValue implements Values {

	private Boolean value;
	
	public BoolValue(String s){
		value = Boolean.parseBoolean(s);
	}
	
	@Override
	public boolean isVar() {return false;}


	@Override
	public String getValue() {
		return value.toString();
	}


	@Override
	public int getIntNumber() {
		return (value)?1:0;
	}


	@Override
	public float getFloatNumber() {
		return (value)?1.0F:0.0F;
	}


	@Override
	public boolean getBoolean() {
		return value;
	}

	@Override
	public String getType() {
		return "bool";
	}

}