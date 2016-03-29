/* Generated By:JavaCC: Do not edit this line. Glat.java */
package glat;
import glat.program.*;
import glat.program.instructions.*;
import glat.program.instructions.expressions.*;
import glat.program.instructions.expressions.terminals.*;
import glat.program.instructions.expressions.terminals.Number;
import java.io.*;

public class Glat implements GlatConstants {
  private static Program p;

  public Glat()
  {}

  public Program parse(String args []) throws ParseException, FileNotFoundException
  {
    Glat parser;
    p = new Program();
    System.out.println("Welcome");
    if (args.length > 0)
    {
      System.out.println("File");
      FileInputStream is = new FileInputStream(new File(args [0]));
      parser = new Glat(new BufferedInputStream(is));
    }
    else
    {
      System.out.println("Hand");
      parser = new Glat(System.in);
    }
    System.out.println("Parser");
    parser.Input();
    System.out.println("End\u005cn\u005cn\u005cn");
    return p;
  }

/* Program */
  static final public void Input() throws ParseException {
  Declaration v;
  Method m;
    label_1:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VAR:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
      v = Decl("global");
      jj_consume_token(DOTC);
      p.addDeclaration(v);
    }
    label_2:
    while (true) {
      m = Func();
      p.addMethod(m);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case THRE:
      case VOID:
      case LOCK:
      case ID:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
    }
    jj_consume_token(0);
    p.checkCalls();
  }

//-------------------------------------------------/* Variables */
  static final public Declaration Decl(String env) throws ParseException {
  Declaration v;
  String s;
  Token t;
    jj_consume_token(VAR);
    s = V_type();
    t = jj_consume_token(ID);
    v = new Declaration(s, t.image,env);
    {if (true) return v;}
    throw new Error("Missing return statement in function");
  }

/* Methods */
  static final public Method Func() throws ParseException {
  Method m;
  String s;
  Token t;
  Declaration v;
  String label;
    s = Type();
    t = jj_consume_token(ID);
    m = new Method(s, t.image);
    label = m.getLabel();
    jj_consume_token(LPAR);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VAR:
      v = Decl(label);
      m.addParameter(v);
      label_3:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMA:
          ;
          break;
        default:
          jj_la1[2] = jj_gen;
          break label_3;
        }
        jj_consume_token(COMA);
        v = Decl(label);
        m.addParameter(v);
      }
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    jj_consume_token(RPAR);
    jj_consume_token(LBRA);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VAR:
        ;
        break;
      default:
        jj_la1[4] = jj_gen;
        break label_4;
      }
      v = Decl(label);
      jj_consume_token(DOTC);
      m.addDeclaration(v);
    }
    Cfg(m);
    jj_consume_token(RBRA);
    {if (true) return m;}
    throw new Error("Missing return statement in function");
  }

/* CFG */
  static final public void Cfg(Method m) throws ParseException {
  Token t;
  Transition tr;
  int j = 0;
    jj_consume_token(START);
    t = jj_consume_token(ID);
    m.addEntryPoint(t.image);
    jj_consume_token(DOTC);
    label_5:
    while (true) {
      tr = Tran(m, j++);
      m.addTransition(tr);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case TRAN:
        ;
        break;
      default:
        jj_la1[5] = jj_gen;
        break label_5;
      }
    }
  }

  static final public Transition Tran(Method m, int j) throws ParseException {
  Transition tr;
  Token t1, t2;
  Instruction i;
    jj_consume_token(TRAN);
    t1 = jj_consume_token(ID);
    jj_consume_token(TO);
    t2 = jj_consume_token(ID);
    tr = new Transition(m, j, t1.image, t2.image);
    jj_consume_token(LBRA);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case ASSU:
      case ASSE:
      case THRE:
      case JOIN:
      case LOCK:
      case UNLOCK:
      case RETU:
      case CALL:
      case ID:
        ;
        break;
      default:
        jj_la1[6] = jj_gen;
        break label_6;
      }
      i = Inst(m, tr);
      tr.addInstruction(i);
    }
    jj_consume_token(RBRA);
    {if (true) return tr;}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Inst(Method m, Transition tr) throws ParseException {
  Instruction i;
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ID:
      i = Asig(m, tr);
      break;
    case LOCK:
    case UNLOCK:
      i = Lock(m, tr);
      break;
    case RETU:
      i = Return(m, tr);
      break;
    case JOIN:
      i = Join(m, tr);
      break;
    case THRE:
    case CALL:
      i = Call(m, tr);
      break;
    case ASSU:
      i = Assume(m, tr);
      break;
    case ASSE:
      i = Assert(m, tr);
      break;
    default:
      jj_la1[7] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    t = jj_consume_token(DOTC);
    i.setPosition(t.beginLine);
    {if (true) return i;}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Call(Method m, Transition tr) throws ParseException {
        Token t,n;
        Call c;
        Variable v;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case THRE:
      t = jj_consume_token(THRE);
      break;
    case CALL:
      t = jj_consume_token(CALL);
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    n = jj_consume_token(ID);
        c = new Call(n.image,t.image);
    jj_consume_token(LPAR);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ID:
      v = Var(m,tr);
        c.addParameter(v);
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case COMA:
          ;
          break;
        default:
          jj_la1[9] = jj_gen;
          break label_7;
        }
        jj_consume_token(COMA);
        v = Var(m,tr);
        c.addParameter(v);
      }
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
    jj_consume_token(RPAR);
    {if (true) return c;}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Lock(Method m, Transition tr) throws ParseException {
        Token t;
        Variable v;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LOCK:
      t = jj_consume_token(LOCK);
      break;
    case UNLOCK:
      t = jj_consume_token(UNLOCK);
      break;
    default:
      jj_la1[11] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    v = Var(m,tr);
    {if (true) return new Lock(t.image,v);}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Return(Method m, Transition tr) throws ParseException {
  Terminal t;
    jj_consume_token(RETU);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AST:
    case ID:
    case NUM:
      t = Term(m, tr);
      {if (true) return new Return(t);}
      break;
    default:
      jj_la1[12] = jj_gen;
      ;
    }
    {if (true) return new Return();}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Join(Method m, Transition tr) throws ParseException {
        Variable v;
    jj_consume_token(JOIN);
    v = Var(m,tr);
    {if (true) return new Join(v);}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Assert(Method m, Transition tr) throws ParseException {
  String o;
  Terminal t1, t2;
    jj_consume_token(ASSE);
    jj_consume_token(LPAR);
    t1 = Term(m, tr);
    o = Op_c();
    t2 = Term(m, tr);
    jj_consume_token(RPAR);
    {if (true) return new Assert(o,t1,t2);}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Assume(Method m, Transition tr) throws ParseException {
  String o;
  Terminal t1, t2;
    jj_consume_token(ASSU);
    jj_consume_token(LPAR);
    t1 = Term(m, tr);
    o = Op_c();
    t2 = Term(m, tr);
    jj_consume_token(RPAR);
    {if (true) return new Assume(o,t1,t2);}
    throw new Error("Missing return statement in function");
  }

  static final public Instruction Asig(Method m, Transition tr) throws ParseException {
  Variable v;
  Expression exp;
  Instruction i;
    v = Var(m, tr);
    jj_consume_token(ASIG);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case AST:
    case ID:
    case NUM:
      exp = Expr(m, tr);
      {if (true) return new Asignation(v, exp);}
      break;
    case THRE:
    case CALL:
      i = Call(m, tr);
      ((Call) i).setReturn(v);
      {if (true) return i;}
      break;
    default:
      jj_la1[13] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Expression Expr(Method m, Transition tr) throws ParseException {
  String o = "-1";
  Terminal t1, t2=new TopValue();
  Instruction i;
  Token t;
    t1 = Term(m, tr);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SUM:
    case MIN:
    case DIV:
    case AST:
    case LT:
    case GT:
    case LET:
    case GET:
    case EQ:
    case NEQ:
      o = Op();
      t2 = Term(m, tr);
      break;
    default:
      jj_la1[14] = jj_gen;
      ;
    }
      if (o.equals("-1")) {if (true) return new Expression(t1);}
      else {if (true) return new Expression(o, t1, t2);}
    throw new Error("Missing return statement in function");
  }

/*	Terminal Tokens...*/
  static final public Terminal Term(Method m, Transition tr) throws ParseException {
  Token t;
  Terminal ter;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case ID:
      ter = Var(m,tr);
      {if (true) return ter;}
      break;
    case NUM:
      ter = Numb();
      {if (true) return ter;}
      break;
    case AST:
      t = jj_consume_token(AST);
      {if (true) return new TopValue(t);}
      break;
    default:
      jj_la1[15] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Variable Var(Method m, Transition tr) throws ParseException {
        Token t;
    t = jj_consume_token(ID);
      Declaration d = m.getVariable(t.image);
      if (d == null)
      {
        d = p.getVariable(t.image);
        if (d == null) {if (true) throw new Error("Missing Declaration for " + t.image);}
      }
      {if (true) return new Variable(d, t);}
    throw new Error("Missing return statement in function");
  }

  static final public String Op_c() throws ParseException {
  Token t;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LT:
      t = jj_consume_token(LT);
      break;
    case GT:
      t = jj_consume_token(GT);
      break;
    case LET:
      t = jj_consume_token(LET);
      break;
    case GET:
      t = jj_consume_token(GET);
      break;
    case EQ:
      t = jj_consume_token(EQ);
      break;
    case NEQ:
      t = jj_consume_token(NEQ);
      break;
    default:
      jj_la1[16] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  static final public String Op() throws ParseException {
  Token t;
  String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SUM:
    case MIN:
    case DIV:
    case AST:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case SUM:
        t = jj_consume_token(SUM);
        break;
      case MIN:
        t = jj_consume_token(MIN);
        break;
      case DIV:
        t = jj_consume_token(DIV);
        break;
      case AST:
        t = jj_consume_token(AST);
        break;
      default:
        jj_la1[17] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      {if (true) return t.image;}
      break;
    case LT:
    case GT:
    case LET:
    case GET:
    case EQ:
    case NEQ:
      s = Op_c();
      {if (true) return s;}
      break;
    default:
      jj_la1[18] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public Number Numb() throws ParseException {
  Token t;
    t = jj_consume_token(NUM);
    {if (true) return new Number(t.image);}
    throw new Error("Missing return statement in function");
  }

  static final public String Type() throws ParseException {
  Token t;
  String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VOID:
      t = jj_consume_token(VOID);
      {if (true) return t.image;}
      break;
    case THRE:
    case LOCK:
    case ID:
      s = V_type();
      {if (true) return s;}
      break;
    default:
      jj_la1[19] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public String V_type() throws ParseException {
  Token t;
  String s;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case LOCK:
      t = jj_consume_token(LOCK);
      {if (true) return t.image;}
      break;
    case THRE:
      t = jj_consume_token(THRE);
      {if (true) return t.image;}
      break;
    case ID:
      s = P_type();
      {if (true) return s;}
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  static final public String P_type() throws ParseException {
  Token t;
    t = jj_consume_token(ID);
    {if (true) return t.image;}
    throw new Error("Missing return statement in function");
  }

  static private boolean jj_initialized_once = false;
  /** Generated Token Manager. */
  static public GlatTokenManager token_source;
  static SimpleCharStream jj_input_stream;
  /** Current token. */
  static public Token token;
  /** Next token. */
  static public Token jj_nt;
  static private int jj_ntk;
  static private int jj_gen;
  static final private int[] jj_la1 = new int[21];
  static private int[] jj_la1_0;
  static private int[] jj_la1_1;
  static {
      jj_la1_init_0();
      jj_la1_init_1();
   }
   private static void jj_la1_init_0() {
      jj_la1_0 = new int[] {0x200000,0x68000000,0x0,0x200000,0x200000,0x800000,0xde000000,0xde000000,0x8000000,0x0,0x0,0xc0000000,0x4000,0x8004000,0x1ff800,0x4000,0x1f8000,0x7800,0x1ff800,0x68000000,0x48000000,};
   }
   private static void jj_la1_init_1() {
      jj_la1_1 = new int[] {0x0,0x4,0x80,0x0,0x0,0x0,0x7,0x7,0x2,0x80,0x4,0x0,0xc,0xe,0x0,0xc,0x0,0x0,0x0,0x4,0x4,};
   }

  /** Constructor with InputStream. */
  public Glat(java.io.InputStream stream) {
     this(stream, null);
  }
  /** Constructor with InputStream and supplied encoding */
  public Glat(java.io.InputStream stream, String encoding) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser.  ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new GlatTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  /** Reinitialise. */
  static public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  /** Constructor. */
  public Glat(java.io.Reader stream) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new GlatTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  static public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  /** Constructor with generated Token Manager. */
  public Glat(GlatTokenManager tm) {
    if (jj_initialized_once) {
      System.out.println("ERROR: Second call to constructor of static parser. ");
      System.out.println("       You must either use ReInit() or set the JavaCC option STATIC to false");
      System.out.println("       during parser generation.");
      throw new Error();
    }
    jj_initialized_once = true;
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  /** Reinitialise. */
  public void ReInit(GlatTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 21; i++) jj_la1[i] = -1;
  }

  static private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }


/** Get the next Token. */
  static final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

/** Get the specific Token. */
  static final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  static private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  static private java.util.List<int[]> jj_expentries = new java.util.ArrayList<int[]>();
  static private int[] jj_expentry;
  static private int jj_kind = -1;

  /** Generate ParseException. */
  static public ParseException generateParseException() {
    jj_expentries.clear();
    boolean[] la1tokens = new boolean[40];
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 21; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
          if ((jj_la1_1[i] & (1<<j)) != 0) {
            la1tokens[32+j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 40; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.add(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = jj_expentries.get(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  /** Enable tracing. */
  static final public void enable_tracing() {
  }

  /** Disable tracing. */
  static final public void disable_tracing() {
  }

}
