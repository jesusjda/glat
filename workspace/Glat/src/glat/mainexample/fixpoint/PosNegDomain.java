package glat.mainexample.fixpoint;

import java.util.Iterator;
import java.util.List;

import glat.fixpoint.AbstractDomain;
import glat.fixpoint.AbstractState;
import glat.program.GlatInstruction;
import glat.program.instructions.Assignment;
import glat.program.instructions.Expression;
import glat.program.instructions.expressions.Terminal;
import glat.program.instructions.expressions.terminals.Values;
import glat.program.instructions.expressions.terminals.Variable;

public class PosNegDomain implements AbstractDomain {
	

	
	private PosNegState c(AbstractState s){return (PosNegState)s;}

	@Override
	public AbstractState initVars(List<Variable> vars) {
		PosNegState p = new PosNegState();
		vars.forEach((d)->p.init(d.getLabel()));
		return p;
	}

	@Override
	public AbstractState empty() {
		return new PosNegState();
	}
	
	public PosNegValues getV(PosNegState pt, Terminal t){
		if(t.isVar())
			return pt.get(((Variable)t).getLabel());
		else{
			Values v = (Values)t;
			if(v.getType().equals("nondeterministic"))
				return PosNegValues.TOP;
			else{
				float f = v.getFloatNumber();
				if(f == 0)
					return PosNegValues.CERO;
				else if (f>0)
					return PosNegValues.POS;
				else
					return PosNegValues.NEG;
			}
		}
	}
	
	@Override
	public AbstractState abstractExec(GlatInstruction i, AbstractState st) {
		PosNegState pt = new PosNegState();
		pt.extend(c(st));
		switch(i.getType()){
		case ASSIGNMENT:
			Assignment a = (Assignment)i;
			Expression exp = a.getExpr();
			if(exp.getOperands().size() == 1)
				pt.add(a.getDest().getLabel(), getV(pt,exp.getOperand(0)));
			else
				pt.add(a.getDest().getLabel(), op(pt,exp.getOperator(),exp.getOperand(0),exp.getOperand(1)));
			break;
		case ASSERT:
		case ASSUME:
			break;
		case ASYNCCALL:
		case SYNCCALL:
			break;
		case JOIN:
			break;
		case LOCK:
		case UNLOCK:
			break;
		case RETURN:
			break;
		default:
			break;
		}
		return pt;
	}

	private PosNegValues op(PosNegState pt, String op, Terminal t1, Terminal t2) {
		PosNegValues v1 = getV(pt,t1);
		PosNegValues v2 = getV(pt,t2);
		if (v1 == PosNegValues.NONE || v2 == PosNegValues.NONE)
			return PosNegValues.NONE;
		switch(op){
		case "+":
			if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if(v1 == PosNegValues.CERO)
				return v2;
			else if(v2 == PosNegValues.CERO)
				return v1;
			else if (v1 != v2)
				return PosNegValues.TOP;
			else
				return v1;
		case "-":
			if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if(v2 == PosNegValues.CERO)
				return v1;
			else if(v1 == PosNegValues.CERO)
				return (v2==PosNegValues.POS)?PosNegValues.NEG:PosNegValues.POS;
			else if (v1 != v2)
				return v1;
			else
				return PosNegValues.TOP;
		case "/":
			if(v2 == PosNegValues.CERO)
				return PosNegValues.NONE;
			else if (v1 == PosNegValues.CERO)
				return PosNegValues.CERO;
			else if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if (v1 == v2)
				return PosNegValues.POS;
			else
				return PosNegValues.NEG;
		case "*":
			if (v1 == PosNegValues.TOP || v2 == PosNegValues.TOP)
				return PosNegValues.TOP;
			else if (v1 == PosNegValues.CERO || v2 == PosNegValues.CERO)
				return PosNegValues.CERO;
			else if (v1 == v2)
				return PosNegValues.POS;
			else
				return PosNegValues.NEG;
		default:
			break;
		}
		return PosNegValues.NONE;
	}

	@Override
	public AbstractState extend(AbstractState s0, AbstractState st) {
		PosNegState p0 = new PosNegState(),p1=c(st);
		p0.extend(c(s0));
		p0.extend(p1);
		return p0;
	}

	@Override
	public AbstractState project(AbstractState s0, List<Variable> lv) {
		PosNegState s = c(s0);
		PosNegState s1 = new PosNegState();
		lv.forEach((var)->s1.add(var.getLabel(), s.get(var.getLabel())));
		return s1;
	}

	@Override
	public AbstractState rename(AbstractState s0, List<Variable> actual, List<Variable> formal) {
		PosNegState p0 = new PosNegState();
		p0.extend(c(s0));
		String v1,v2;
		Iterator<Variable> iA = actual.iterator(), iF = formal.iterator();
		while(iA.hasNext() && iF.hasNext()){
			v1 = iA.next().getLabel();
			v2 = iF.next().getLabel();
			p0.rename(v1, v2);
		}
		return p0;
	}

	@Override
	public AbstractState lub(AbstractState s0, AbstractState s1) {
		PosNegState p0 = new PosNegState(c(s0)),p1 = new PosNegState(c(s1));
		return p0.lub(p1);
	}

	@Override
	public boolean le(AbstractState s0, AbstractState s1) {
		return c(s0).size() <= c(s1).size();
	}

}
