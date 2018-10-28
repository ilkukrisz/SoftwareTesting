package hu.uni.miskolc.iit.softwaretesting.model;

import hu.uni.miskolc.iit.softwaretesting.exceptions.InvalidArgumentException;

import java.util.Calendar;
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

    public Borrowing(long borrowID, Reader reader, Date creationDate,
                     Date expirationDate, BorrowStatus status, BookInstance bookInstance) {

        Calendar maxDaysForBorrowing = Calendar.getInstance();
        maxDaysForBorrowing.add(Calendar.DATE, 40);

        if (expirationDate.after(new Date(maxDaysForBorrowing.getTime().getTime()))
                || creationDate.after(expirationDate)
                || creationDate.after(new Date(Calendar.getInstance().getTime().getTime()))
                || creationDate.getTime() < 0 || expirationDate.getTime() < 0)
            throw new InvalidArgumentException();

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
        testCreationDate(creationDate);
        this.creationDate = creationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        testExpirationDate(expirationDate);
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

    private void testCreationDate(Date testCreation) {
        if (testCreation.after(this.expirationDate) || testCreation.getTime() < 0)
            throw new InvalidArgumentException();

        if (testCreation.getTime() > Calendar.getInstance().getTime().getTime())
            throw new InvalidArgumentException();
    }
    private void testExpirationDate(Date testExp) {
        if (testExp.before(this.creationDate) || testExp.getTime() > Calendar.getInstance().getTime().getTime()
                || testExp.before(this.expirationDate) || testExp.getTime() < 0)
            throw new InvalidArgumentException();
    }

    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof Borrowing) {
            Borrowing that = (Borrowing) obj;
            return (this.getReader().equals(that.getReader()) &&
                    this.getBookInstance().equals(that.getBookInstance()) &&
                    this.getBorrowID() == that.getBorrowID() &&
                    isOnTheSameDay(this.getCreationDate(),that.getCreationDate()) &&
                    isOnTheSameDay(this.getExpirationDate(), that.getExpirationDate()) &&
                    this.getStatus().equals(that.getStatus()));
        }
        else
            return false;

    }

    private boolean isOnTheSameDay(Date one, Date other) {
        return (one.getDay() == other.getDay() &&
                one.getMonth() == other.getMonth() &&
                one.getYear() == other.getYear());
    }


}
