package hu.uni.miskolc.iit.softwaretesting.model;

/**
 * Represents a real instance of a book.
 */
public class BookInstance {
    /**
     * The inventory number of the book instance.
     */
    long inventoryNumber;

    /**
     * The reference to the data container of the book instance.
     * @see Book
     */
    Book book;

    public BookInstance(long inventoryNumber, Book book) {
        this.inventoryNumber = inventoryNumber;
        this.book = book;
    }

    public long getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(long inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
