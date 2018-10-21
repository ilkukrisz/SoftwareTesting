package hu.uni.miskolc.iit.softwaretesting.model;

public class Librarian extends User {

    public Librarian(String username, Password password, String firstName, String lastName, String email, String mobileNumber) {
        super(username, password, firstName, lastName, email, mobileNumber);
    }


    @Override
    public boolean equals(final Object obj) {
        if(obj instanceof Librarian) {
            Librarian that = (Librarian) obj;
            return (this.getUsername().equalsIgnoreCase(that.getUsername())
                    && this.getEmail().equalsIgnoreCase(that.getEmail())
                    && this.getPassword().toString().equalsIgnoreCase(that.getPassword().toString())
                    && this.getFirstName().equalsIgnoreCase(that.getFirstName())
                    && this.getLastName().equalsIgnoreCase(that.getLastName())
                    && this.getMobileNumber().equalsIgnoreCase(that.getMobileNumber()));
        }
        else
            return false;

    }
}

