package hu.uni.miskolc.iit.softwaretesting.web.config;

import org.springframework.core.convert.converter.Converter;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateFormatter implements Converter<String, XMLGregorianCalendar> {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public XMLGregorianCalendar convert(String source) {
        try {
            Date date = dateFormat.parse(source);
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(date);
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (ParseException | DatatypeConfigurationException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
