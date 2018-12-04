package hu.uni.miskolc.iit.softwaretesting.model;

public enum Genre {

    Scifi, Crimi, Natural, Fiction, Horror, Mystery, Romance, Science, History, Comics, Guide, Travel, Other;

    public static boolean isContained(String value) {

        for (Genre i : Genre.values()) {
            if (String.valueOf(i).equalsIgnoreCase(value))
                return true;
        }
        return false;
    }
}
