package hu.uni.miskolc.iit.softwaretesting.model;

public class Book
{

    // data tags wwich defines a Book object properly

    private int id;
    private String author;
    private String title;
    private Long isbn;   // international standard book number, some kind of id but it's long (13 numbers)
    private int publishDate;
    private String genre;  // defines the type of the book i.e: scifi, thriller, crimi etc.
    private boolean isLoaned;   // shows if the book is loaned by somebody at this time

    // the constructor gets the data tags as arguments

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

    // getters and setters to reach and modify the data tags if it's necessary

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

    // overrided the default toString function

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
