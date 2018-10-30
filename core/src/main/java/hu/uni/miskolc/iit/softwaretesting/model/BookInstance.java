package hu.uni.miskolc.iit.softwaretesting.model;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidArgumentException;

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
        if (inventoryNumber <= 0) {
            throw new InvalidArgumentException();
        }
        this.inventoryNumber = inventoryNumber;
        this.book = book;
        this.isLoaned = isLoaned;
    }

    public long getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(long inventoryNumber) {
        if (inventoryNumber <= 0 || book == null) {
            throw new InvalidArgumentException();
        }
        this.inventoryNumber = inventoryNumber;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        if (book == null)
            throw new InvalidArgumentException();
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

    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof BookInstance) {
            BookInstance that = (BookInstance) obj;
            return (this.getBook().equals(that.getBook()) &&
                    this.getInventoryNumber() == that.getInventoryNumber());

        }
        else
            return false;

    }
}
