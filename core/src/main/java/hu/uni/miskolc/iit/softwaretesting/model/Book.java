package hu.uni.miskolc.iit.softwaretesting.model;

public class Book
{

    /**
     * @param isbn - The international standard book number of the book.
     */
    private Long isbn;

    /**
     * @param author - The author of the book.
     */
    private String author;

    /**
     * @param title - The title of the book.
     */
    private String title;

    /**
     * @param publishDate - The publish date of the book defined by year.
     */
    private int publishDate;

    /**
     * @param genre - The genre of the book.
     */
    private Genre genre;


    public Book(String author, String title, Long isbn, int publishDate, Genre genre)
    {
        this.author = author;
        this.title = title;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre;
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

    public Genre getGenre()
    {
        return genre;
    }

    public void setGenre(Genre genre)
    {
        this.genre = genre;
    }


    @Override
    public String toString()
    {
        return "Details of the book are the following: " +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", isbn=" + isbn +
                ", date of publish =" + publishDate +
                ", genre ='" + genre + '\'';
    }
}
