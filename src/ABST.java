// an abstract class representing a node in the BST
abstract class ABST<T> {
  IComparator<T> order;

  ABST(IComparator<T> order) {
    this.order = order;
  }

  abstract ABST<T> insert(T data);

  abstract T getLeftMost();

  abstract ABST<T> getRight();

  abstract ABST<T> getRightHelp(T data);

  abstract boolean sameTree(ABST<T> that);

  abstract boolean sameLeaf(Leaf<T> that);

  abstract boolean sameNode(Node<T> that);
}

// generic comparator that operates similarly to the String compareTo method
interface IComparator<T> {
  int compare(T t1, T t2);
}

// represents a Leaf node with no children/sub-nodes
class Leaf<T> extends ABST<T> {

  Leaf(IComparator<T> order) {
    super(order);
  }

  // produce a new node with leaves as both of its children
  @Override
  ABST<T> insert(T data) {
    return new Node<T>(data, this, this, this.order);
  }

  @Override
  T getLeftMost() {
    throw new RuntimeException("No leftmost item of an empty tree");
  }

  @Override
  ABST<T> getRight() {
    throw new RuntimeException("No right of an empty tree");
  }

  @Override
  ABST<T> getRightHelp(T data) {
    throw new RuntimeException("No right of an empty tree");
  }

  @Override
  boolean sameTree(ABST<T> that) {
    return that.sameLeaf(this);
  }

  @Override
  boolean sameLeaf(Leaf<T> that) {
    return this.order.equals(that.order);
  }

  @Override
  boolean sameNode(Node<T> that) {
    return false;
  }
}

// represents a generic node in the BST
class Node<T> extends ABST<T> {
  T data;
  ABST<T> left;
  ABST<T> right;

  Node(T data, ABST<T> left, ABST<T> right, IComparator<T> order) {
    super(order);
    this.data = data;
    this.left = left;
    this.right = right;
  }

  @Override
  ABST<T> insert(T data) {
    // if it should go to the left
    if (this.order.compare(this.data, data) < 0) {
      return this.left.insert(data);
    }
    else {
      return this.right.insert(data);
    }
  }

  @Override
  T getLeftMost() {
    if (this.left instanceof Node<?>) {
      return this.data;
    }
    return this.left.getLeftMost();
  }

  @Override
  ABST<T> getRight() {
    return null;
  }

  @Override
  ABST<T> getRightHelp(T data) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  boolean sameTree(ABST<T> that) {
    return that.sameNode(this);
  }

  @Override
  boolean sameLeaf(Leaf<T> that) {
    return false;
  }

  @Override
  boolean sameNode(Node<T> that) {
    return this.data.equals(that.data) && this.left.sameTree(that.left)
        && this.right.sameTree(that.right) && this.order.equals(that.order);
  }
}

// a comparator object for comparing books by their title
class BooksByTitle implements IComparator<Book> {

  @Override
  public int compare(Book t1, Book t2) {
    return t1.title.compareTo(t2.title);
  }

}

// a comparator object for comparing books by their author
class BooksByAuthor implements IComparator<Book> {

  @Override
  public int compare(Book t1, Book t2) {
    return t1.author.compareTo(t2.title);
  }

}

// a comparator object for comparing books by their price
class BooksByPrice implements IComparator<Book> {

  // if t1's price is greater than t2's price, return a number greater than 0
  @Override
  public int compare(Book t1, Book t2) {
    return t1.price - t2.price;
  }

}

// represents a Book object
class Book {
  String title;
  String author;
  int price;

  Book(String title, String author, int price) {
    this.title = title;
    this.author = author;
    this.price = price;
  }
}
