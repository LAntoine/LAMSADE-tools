package com.github.lamsadetools.map;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.sun.star.lang.IllegalArgumentException;

/**
 * ItineraryMap allows you to open an itinerary into your browser on openstreetmap's page.
 *
 */
public class ItineraryMap {
	private final static Logger LOGGER = Logger.getLogger(AddressInfos.class);
	private static final String path = "src/test/resources/log4j.properties";
	
	private String longitudeA;
	private String latitudeA;
	private String longitudeB;
	private String latitudeB;
	
	/**
	 * ItineraryMap needs the longitude and the latitude of the departure and arrival points.
	 * 
	 * 
	 * @param longitudeA
	 * @param latitudeA
	 * @param longitudeB
	 * @param latitudeB
	 */
	public ItineraryMap(String longitudeA, String latitudeA, String longitudeB, String latitudeB) {
		this.latitudeA=latitudeA;
		this.longitudeA= longitudeA;
		this.latitudeB=latitudeB;
		this.longitudeB=longitudeB;
	}
	
	/**
	 * This method sets the openstreetmap url according to the longitudes and latitudes entered in the class attributes
	 * @return url (a String)
	 */
	public String setMapUrl(){
		String url ="http://www.openstreetmap.org/directions?engine=osrm_car&route="+latitudeA+
				"%2C"+longitudeA+"%3B"+latitudeB+"%2C"+longitudeB;
		return url;	
	}
	
	/**
	 * Open an URL into the default browser
	 * @param url (must be a String)
	 */
	private void openMapUrl(String url){
		if(Desktop.isDesktopSupported())
		{
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public String getLongitudeA() {
		return longitudeA;
	}

	public void setLongitudeA(String longitudeA) {
		this.longitudeA = longitudeA;
	}

	public String getLatitudeA() {
		return latitudeA;
	}

	public void setLatitudeA(String latitudeA) {
		this.latitudeA = latitudeA;
	}

	public String getLongitudeB() {
		return longitudeB;
	}

	public void setLongitudeB(String longitudeB) {
		this.longitudeB = longitudeB;
	}

	public String getLatitudeB() {
		return latitudeB;
	}

	public void setLatitudeB(String latitudeB) {
		this.latitudeB = latitudeB;
	}
	
	public static void main(String[] args) {
		PropertyConfigurator.configure(path);
		
		String rawAdressA = "Paris";
		String rawAdressB = "Cologne";
		try {
			AddressInfos paris = new AddressInfos(rawAdressA);
			AddressInfos cologne = new AddressInfos(rawAdressB);
			ItineraryMap parisCologne = new ItineraryMap(paris.getLongitude(), paris.getLatitude(), 
					cologne.getLongitude(), cologne.getLatitude());
			LOGGER.info("\n" + paris.toString());
			LOGGER.info("\n" + cologne.toString());
			String url = parisCologne.setMapUrl();
			System.out.println(url);
			parisCologne.openMapUrl(url);
						
		} catch (IllegalArgumentException | IOException e) {
			LOGGER.error("Error : ", e);
		}	
		
	}	
}