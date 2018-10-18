import tester.Tester;

// represents an arithmetic expression
interface IArith {
  // accept a visitor
  <R> R accept(IArithVisitor<R> visitor);
}

// represents a constant number
class Const implements IArith {
  // value of the Const
  double num;

  Const(double num) {
    this.num = num;
  }

  /* TEMPLATE:
   * Fields:
   * ... this.num ... -- Double
   * Methods:
   * ... this.accept(IArithVisitor<R>) ... -- <R>
   */

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitConst(this);
  }
}

// represents a formula
class Formula implements IArith {
  // function for the formula
  IFunc2<Double, Double, Double> fun;
  // name of the formula
  String name;
  // left side of the formula
  IArith left;
  // right side of the formula
  IArith right;

  Formula(IFunc2<Double, Double, Double> fun, String name, IArith left, IArith right) {
    this.fun = fun;
    this.name = name;
    this.left = left;
    this.right = right;
  }

  /* TEMPLATE:
   * Fields:
   * ... this.fun ...   -- IFunc2<Double, Double, Double>
   * ... this.name ...  -- String
   * ... this.left ...  -- IArith
   * ... this.right ... -- IArith
   * Methods:
   * ... this.accept(IArithVisitor<R>) ... -- <R>
   * Methods of Fields:
   * ... this.fun.apply(Double, Double) ...       -- Double
   * ... this.left.accept(IArithVisitor<R>) ...   -- <R>
   * ... this.right.accept(IArithVisitor<R>) ...  -- <R>
   */

  public <R> R accept(IArithVisitor<R> visitor) {
    return visitor.visitFormula(this);
  }
}

// represents a function that takes two arguments of type A1 and A2 and returns an R
interface IFunc2<A1, A2, R> {
  // apply the function to a1 and a2
  R apply(A1 a1, A2 a2);
}

// represents addition
class Add implements IFunc2<Double, Double, Double> {

  /* TEMPLATE:
   * Methods:
   * ... this.apply(Double, Double) ... -- Double
   */

  public Double apply(Double a1, Double a2) {
    return a1 + a2;
  }
}

// represents subtraction
class Subtract implements IFunc2<Double, Double, Double> {

  /* TEMPLATE:
   * Methods:
   * ... this.apply(Double, Double) ... -- Double
   */

  public Double apply(Double a1, Double a2) {
    return a1 - a2;
  }
}

// represents division
class Divide implements IFunc2<Double, Double, Double> {

  /* TEMPLATE:
   * Methods:
   * ... this.apply(Double, Double) ... -- Double
   */

  public Double apply(Double a1, Double a2) {
    return a1 / a2;
  }
}

// represents multiplication
class Multiply implements IFunc2<Double, Double, Double> {

  /* TEMPLATE:
   * Methods:
   * ... this.apply(Double, Double) ... -- Double
   */

  public Double apply(Double a1, Double a2) {
    return a1 * a2;
  }
}

// a visitor that visits IAriths
interface IArithVisitor<R> {
  // when visiting a Const
  R visitConst(Const c);

  // when visiting a Formula
  R visitFormula(Formula f);
}

// a visitor that evaluates an IArith to a Double
class EvalVisitor implements IArithVisitor<Double> {

  /* TEMPLATE:
   * Methods:
   * ... this.visitConst(Const c) ...     -- Double
   * ... this.visitFormula(Formula f) ... -- Double
   */

  public Double visitConst(Const c) {
    return c.num;
  }

  public Double visitFormula(Formula f) {
    return f.fun.apply(f.left.accept(this), f.right.accept(this));
  }
}

// a visitor that prints out an IArith to a String
class PrintVisitor implements IArithVisitor<String> {

  /* TEMPLATE:
   * Methods:
   * ... this.visitConst(Const c) ...     -- String
   * ... this.visitFormula(Formula f) ... -- String
   */

  public String visitConst(Const c) {
    return Double.toString(c.num);
  }

  public String visitFormula(Formula f) {
    return "(" + f.name + " " + f.left.accept(this)
      + " " + f.right.accept(this) + ")";
  }
}

// a visitor that Doubles every const in an IArith
class DoublerVisitor implements IArithVisitor<IArith> {

  /* TEMPLATE:
   * Methods:
   * ... this.visitConst(Const c) ...     -- IArith
   * ... this.visitFormula(Formula f) ... -- IArith
   */

  public IArith visitConst(Const c) {
    return new Const(c.num * 2);
  }

  public IArith visitFormula(Formula f) {
    return new Formula(f.fun, f.name, f.left.accept(this), f.right.accept(this));
  }
}

// a visitor that figures out if every Const in the tree is less than 10
class AllSmallVisitor implements IArithVisitor<Boolean> {

  /* TEMPLATE:
   * Methods:
   * ... this.visitConst(Const c) ...     -- Boolean
   * ... this.visitFormula(Formula f) ... -- Boolean
   */

  public Boolean visitConst(Const c) {
    return c.num < 10;
  }

  public Boolean visitFormula(Formula f) {
    return f.left.accept(this) && f.right.accept(this);
  }
}

// a visitor that figures out if there is not division by 0 in the tree
class NoDivBy0 implements IArithVisitor<Boolean> {

  /* TEMPLATE:
   * Methods:
   * ... this.visitConst(Const c) ...     -- Boolean
   * ... this.visitFormula(Formula f) ... -- Boolean
   */

  public Boolean visitConst(Const c) {
    return true;
  }

  public Boolean visitFormula(Formula f) {
    // check deeper formulas first as to avoid accidental
    // zero division when using EvalVisitor
    if (f.left.accept(this) && f.right.accept(this)) {
      if (f.name.equals("div")) {
        return (Math.abs(f.right.accept(new EvalVisitor())) > 0.0001);
      } else {
        return true;
      }
    } else {
      return false;
    }
  }
}

// class for data examples and unit tests
class ExamplesVisitor {
  Const cex1 = new Const(5.0);
  Const cex2 = new Const(3.14);
  Const cex3 = new Const(10.0);
  Formula fex1 = new Formula(new Add(), "plus", cex1, cex2);
  Formula fex2 = new Formula(new Subtract(), "minus", fex1, cex2);
  Formula fex3 = new Formula(new Multiply(), "times", fex2, cex3);
  Formula fex4 = new Formula(new Divide(), "div", fex3, fex1);
  Formula fex01 = new Formula(new Divide(), "div", fex3, new Const(0.0));
  Formula fex02 = new Formula(new Divide(), "div", fex3, new Const(0.0001));
  Formula fex03 = new Formula(new Divide(), "div", fex3, new Const(-0.0001));
  Formula fex04 = new Formula(new Divide(), "div", fex01, new Const(0.0));
  Formula fex05 = new Formula(new Divide(), "div", fex01, new Const(0.0001));
  Formula fex06 = new Formula(new Divide(), "div", fex03, new Const(0.0001));

  // test the IFunc2s
  boolean testIFunc2s(Tester t) {
    return t.checkExpect(new Add().apply(1.5, 2.5), 4.0)
      && t.checkExpect(new Add().apply(1.5, -2.5), -1.0)
      && t.checkExpect(new Add().apply(1.5, 0.0), 1.5)
      && t.checkExpect(new Subtract().apply(1.5, 2.5), -1.0)
      && t.checkExpect(new Subtract().apply(1.5, -2.5), 4.0)
      && t.checkExpect(new Subtract().apply(1.5, 0.0), 1.5)
      && t.checkExpect(new Multiply().apply(1.5, 2.5), 3.75)
      && t.checkExpect(new Multiply().apply(1.5, -2.5), -3.75)
      && t.checkExpect(new Multiply().apply(1.5, 0.0), 0.0)
      && t.checkExpect(new Divide().apply(5.0, 2.5), 2.0)
      && t.checkExpect(new Divide().apply(5.0, -2.5), -2.0)
      && t.checkExpect(new Divide().apply(0.0, 1.5), 0.0);
  }

  // test the EvalVisitor
  boolean testEvalVisitor(Tester t) {
    return t.checkExpect(cex2.accept(new EvalVisitor()), 3.14)
      && t.checkExpect(fex1.accept(new EvalVisitor()), 8.14)
      && t.checkExpect(fex2.accept(new EvalVisitor()), 5.0)
      && t.checkExpect(fex3.accept(new EvalVisitor()), 50.0)
      && t.checkExpect(fex4.accept(new EvalVisitor()), 50.0 / 8.14);
  }

  // test the PrintVisitor
  boolean testPrintVisitor(Tester t) {
    return t.checkExpect(cex2.accept(new PrintVisitor()), "3.14")
      && t.checkExpect(fex1.accept(new PrintVisitor()),
          "(plus 5.0 3.14)")
      && t.checkExpect(fex2.accept(new PrintVisitor()),
          "(minus (plus 5.0 3.14) 3.14)")
      && t.checkExpect(fex3.accept(new PrintVisitor()),
          "(times (minus (plus 5.0 3.14) 3.14) 10.0)")
      && t.checkExpect(fex4.accept(new PrintVisitor()),
          "(div (times (minus (plus 5.0 3.14) 3.14) 10.0) (plus 5.0 3.14))");
  }

  // test the DoublerVisitor
  boolean testDoublerVisitor(Tester t) {
    return t.checkExpect(cex2.accept(new DoublerVisitor()),
        new Const(6.28))
      && t.checkExpect(fex1.accept(new DoublerVisitor()),
          new Formula(new Add(), "plus", new Const(10.0), new Const(6.28)))
      && t.checkExpect(fex2.accept(new DoublerVisitor()),
          new Formula(new Subtract(), "minus",
            new Formula(new Add(), "plus", new Const(10.0), new Const(6.28)),
            new Const(6.28)))
      && t.checkExpect(fex3.accept(new DoublerVisitor()),
          new Formula(new Multiply(), "times",
            new Formula(new Subtract(), "minus",
              new Formula(new Add(), "plus", new Const(10.0), new Const(6.28)),
              new Const(6.28)),
            new Const(20.0)))
      && t.checkExpect(fex4.accept(new DoublerVisitor()),
          new Formula(new Divide(), "div",
            new Formula(new Multiply(), "times",
              new Formula(new Subtract(), "minus",
                new Formula(new Add(), "plus", new Const(10.0), new Const(6.28)),
                new Const(6.28)),
              new Const(20.0)),
            new Formula(new Add(), "plus", new Const(10.0), new Const(6.28))));
  }

  // test the AllSmallVisitor
  boolean testAllSmallVisitor(Tester t) {
    return t.checkExpect(cex2.accept(new AllSmallVisitor()), true)
      && t.checkExpect(cex3.accept(new AllSmallVisitor()), false)
      && t.checkExpect(fex1.accept(new AllSmallVisitor()), true)
      && t.checkExpect(fex2.accept(new AllSmallVisitor()), true)
      && t.checkExpect(fex3.accept(new AllSmallVisitor()), false)
      && t.checkExpect(fex4.accept(new AllSmallVisitor()), false);
  }

  // test the NoDivBy0 visitor
  boolean testNoDivBy0(Tester t) {
    return t.checkExpect(cex2.accept(new NoDivBy0()), true)
      && t.checkExpect(cex3.accept(new NoDivBy0()), true)
      && t.checkExpect(fex1.accept(new NoDivBy0()), true)
      && t.checkExpect(fex2.accept(new NoDivBy0()), true)
      && t.checkExpect(fex3.accept(new NoDivBy0()), true)
      && t.checkExpect(fex4.accept(new NoDivBy0()), true)
      && t.checkExpect(fex01.accept(new NoDivBy0()), false)
      && t.checkExpect(fex02.accept(new NoDivBy0()), true)
      && t.checkExpect(fex03.accept(new NoDivBy0()), true)
      && t.checkExpect(fex04.accept(new NoDivBy0()), false)
      && t.checkExpect(fex05.accept(new NoDivBy0()), false)
      && t.checkExpect(fex06.accept(new NoDivBy0()), true);
  }
}
