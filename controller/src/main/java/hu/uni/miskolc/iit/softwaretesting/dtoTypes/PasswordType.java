//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.11.06 at 02:47:10 PM CET 
//


package hu.uni.miskolc.iit.softwaretesting.dtoTypes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for passwordType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="passwordType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="saltStrength" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="hashedPassword" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "passwordType", propOrder = {
    "saltStrength",
    "hashedPassword"
})
public class PasswordType {

    protected int saltStrength;
    @XmlElement(required = true)
    protected String hashedPassword;

    /**
     * Gets the value of the saltStrength property.
     * 
     */
    public int getSaltStrength() {
        return saltStrength;
    }

    /**
     * Sets the value of the saltStrength property.
     * 
     */
    public void setSaltStrength(int value) {
        this.saltStrength = value;
    }

    /**
     * Gets the value of the hashedPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Sets the value of the hashedPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashedPassword(String value) {
        this.hashedPassword = value;
    }

}
