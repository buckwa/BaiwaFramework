//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.17 at 08:04:19 PM ICT 
//


package com.buckwa.ws.newws.oxm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="advisorStudent" type="{http://www1.reg.kmitl.ac.th/service/}advisorStudent" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "advisorStudent"
})
@XmlRootElement(name = "getAdvisorStudentResponse")
public class GetAdvisorStudentResponse {

    protected AdvisorStudent advisorStudent;

    /**
     * Gets the value of the advisorStudent property.
     * 
     * @return
     *     possible object is
     *     {@link AdvisorStudent }
     *     
     */
    public AdvisorStudent getAdvisorStudent() {
        return advisorStudent;
    }

    /**
     * Sets the value of the advisorStudent property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdvisorStudent }
     *     
     */
    public void setAdvisorStudent(AdvisorStudent value) {
        this.advisorStudent = value;
    }

}