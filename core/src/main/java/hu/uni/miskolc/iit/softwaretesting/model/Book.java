package hu.uni.miskolc.iit.softwaretesting.model;

public class Book
{

    private int id;
    private String author;
    private String title;
    private Long isbn;
    private int publishDate;
    private String genre;
    private boolean isLoaned;

    public Book(int id, String author, String title, Long isbn, int publishDate, String genre, boolean isLoaned)
    {
        this.id = id;
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre;
        this.isLoaned = isLoaned;
    }

    public int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Long getIsbn()
    {
        return isbn;
    }

    public void setIsbn(Long isbn)
    {
        this.isbn = isbn;
    }

    public int getPublishDate()
    {
        return publishDate;
    }

    public void setPublishDate(int publishDate)
    {
        this.publishDate = publishDate;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public boolean isLoaned()
    {
        return isLoaned;
    }

    public void setLoaned(boolean loaned)
    {
        isLoaned = loaned;
    }

    @Override
    public String toString()
    {
        return "Details of the book are the following: " +
                "identifier = " + id +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", isbn=" + isbn +
                ", date of publish =" + publishDate +
                ", genre ='" + genre + '\'' +
                ", is the book loaned? " + isLoaned +
                '.';
    }

}
