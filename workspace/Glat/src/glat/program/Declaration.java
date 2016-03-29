package glat.program;

public class Declaration {
/*
 Get name
 Get type
*/
	public Declaration(String t,String n,String e){
		name=n;type=t;
		isPrimitive = !(t.equals("lock") 
				|| t.equals("thread") );
		env=e;
	}
	
	public String getLabel() {
		return env+"_"+name;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public String getEnv(){
		return env;
	}
	
	public boolean isPrimitive(){
		return isPrimitive;
	}
		
	private String type;
	private String name;
	private boolean isPrimitive;
	private String env;
	
}
