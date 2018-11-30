package hu.uni.miskolc.iit.softwaretesting.converterMethods;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateAdapter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static String marshal(XMLGregorianCalendar value) {
        Calendar calendar = value.toGregorianCalendar();
        return dateFormat.format(calendar.getTime());
    }

    public static XMLGregorianCalendar unmarshal(String value) {
        try {
            Date date = dateFormat.parse(value);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (ParseException | DatatypeConfigurationException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}