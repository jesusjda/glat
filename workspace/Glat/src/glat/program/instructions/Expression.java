package glat.program.instructions;

import java.util.ArrayList;
import java.util.List;

import glat.program.instructions.expressions.Terminal;

public class Expression {
	
	public Expression(){
		operator = "";
		operands = new ArrayList<Terminal>();
	}

	public String toString(){
		return "$op("+operator+","+operands+")";
	}
	
	public String getOperator(){
		return operator;
	}
	
	public List<Terminal> getOperands(){
		return operands;
	}
	
	public Terminal getOperand(int pos){
		return operands.get(pos);
	}
	
	public void setOperator(String op){
		operator = op;
	}
	
	public void addOperand(Terminal t){
		operands.add(t);
	}
	
	private List<Terminal> operands;
	private String operator;
	 
}