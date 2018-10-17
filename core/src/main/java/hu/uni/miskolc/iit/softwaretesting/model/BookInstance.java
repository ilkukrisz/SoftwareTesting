package hu.uni.miskolc.iit.softwaretesting.model;

/**
 * Represents a real instance of a book.
 */
public class BookInstance {
    /**
     * The inventory number of the book instance.
     */
    private long inventoryNumber;

    /**
     * The reference to the data container of the book instance.
     * @see Book
     */
    private Book book;

    /**
     * Contains whether the book instance is loaned or nor.
     */
    private boolean isLoaned;

    public BookInstance(long inventoryNumber, Book book, boolean isLoaned) {
        this.inventoryNumber = inventoryNumber;
        this.book = book;
        this.isLoaned = isLoaned;
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

    public boolean isLoaned() {
        return isLoaned;
    }

    public void setLoaned(boolean loaned) {
        isLoaned = loaned;
    }


    @Override
    public String toString() {
        return "BookInstance{" +
                "inventoryNumber=" + inventoryNumber +
                ", book=" + book +
                ", isLoaned=" + isLoaned +
                '}';
    }
}
