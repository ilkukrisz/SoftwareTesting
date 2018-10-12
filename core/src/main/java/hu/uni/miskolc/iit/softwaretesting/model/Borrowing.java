package hu.uni.miskolc.iit.softwaretesting.model;

import java.util.Date;

/**
 * Represents a book borrowing.
 */
public class Borrowing {
    /**
     * The ID of the borrowing.
     */
    private long borrowID;

    /**
     * The reader, who borrows.
     */
    private Reader reader;

    /**
     * The creation date of the borrowing.
     */
    private Date creationDate;

    /**
     * The expiration of the borrowing.
     */
    private Date expirationDate;

    /**
     * The status of the Borrowing.
     * @see BorrowStatus
     */
    private BorrowStatus status;

    /**
     * The borrowed book instance.
     */
    private BookInstance bookInstance;

    public Borrowing(long borrowID, Reader reader, Date creationDate, Date expirationDate, BorrowStatus status, BookInstance bookInstance) {
        this.borrowID = borrowID;
        this.reader = reader;
        this.creationDate = creationDate;
        this.expirationDate = expirationDate;
        this.status = status;
        this.bookInstance = bookInstance;
    }

    public long getBorrowID() {
        return borrowID;
    }

    public void setBorrowID(long borrowID) {
        this.borrowID = borrowID;
    }

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public BorrowStatus getStatus() {
        return status;
    }

    public void setStatus(BorrowStatus status) {
        this.status = status;
    }

    public BookInstance getBookInstance() {
        return bookInstance;
    }

    public void setBookInstance(BookInstance bookInstance) {
        this.bookInstance = bookInstance;
    }

    @Override
    public String toString() {
        return "Borrowing{" +
                "borrowID=" + borrowID +
                ", reader=" + reader +
                ", creationDate=" + creationDate +
                ", expirationDate=" + expirationDate +
                ", status=" + status +
                ", bookInstance=" + bookInstance +
                '}';
    }
}
