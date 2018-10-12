public class ExamplesABST {
  Book book1 = new Book("Harry Potter", "JK Rowling", 10);
  Book book2 = new Book("Snow White", "Dr. Seuss", 12);
  Book book3 = new Book("Hunger Games", "Suzanne Collins", 15);
 
  
  ABST<Book> leaf1 = new Leaf<Book>(new BooksByTitle());
  ABST<Book> leaf2 = new Leaf<Book>(new BooksByAuthor());
  ABST<Book> leaf3 = new Leaf<Book>(new BooksByPrice());
  
  ABST<Book> abst1 = new Node<Book>(book1, leaf1, leaf1, new BooksByTitle());
  ABST<Book> abst2 = new Node<Book>(book2, leaf1, leaf1, new BooksByTitle());
  ABST<Book> abst3 = new Node<Book>(book3, abst1, abst2, new BooksByTitle());
  
}
