package com.example.simplepois.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.simplepois.data.PoisContract.PoiDetailsEntry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

// TODO: Extend PoiInfo

/**
 * An instance of this class is an immutable value-object representing a single POI (Point of
 * Interest).
 * Each field may be null.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoiDetails {

    // TODO: Move the conversion helper methods to another class

    /**
     * Create an instance from the current row of the provided cursor
     */
    public static PoiDetails fromCursor(Cursor cursor) {
        int remoteIdIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_REMOTE_ID);
        int titleIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_TITLE);
        int addressIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_ADDRESS);
        int transportIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_TRANSPORT);
        int emailIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_EMAIL);
        int geoCoordinatesIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_GEOCOORDINATES);
        int descriptionIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_DESCRIPTION);
        int phoneIndex = cursor.getColumnIndex(PoiDetailsEntry.COL_PHONE);

        long remoteId = cursor.getLong(remoteIdIndex);
        String title = cursor.getString(titleIndex);
        String address = cursor.getString(addressIndex);
        String transport = cursor.getString(transportIndex);
        String email = cursor.getString(emailIndex);
        String geoCoordinates = cursor.getString(geoCoordinatesIndex);
        String description = cursor.getString(descriptionIndex);
        String phone = cursor.getString(phoneIndex);

        return new PoiDetails(remoteId,
                title,
                address,
                transport,
                email,
                geoCoordinates,
                description,
                phone);
    }

    public static ContentValues toValues(PoiDetails poiInfo) {
        ContentValues values = new ContentValues();
        values.put(PoiDetailsEntry.COL_REMOTE_ID, poiInfo.remoteId);
        values.put(PoiDetailsEntry.COL_TITLE, poiInfo.title);
        values.put(PoiDetailsEntry.COL_ADDRESS, poiInfo.address);
        values.put(PoiDetailsEntry.COL_TRANSPORT, poiInfo.transport);
        values.put(PoiDetailsEntry.COL_EMAIL, poiInfo.email);
        values.put(PoiDetailsEntry.COL_GEOCOORDINATES, poiInfo.geoCoordinates);
        values.put(PoiDetailsEntry.COL_DESCRIPTION, poiInfo.description);
        values.put(PoiDetailsEntry.COL_PHONE, poiInfo.phone);

        return values;
    }


    @JsonProperty("id")
    public final long remoteId;
    public final String title;
    public final String address;
    public final String transport;
    public final String email;
    @JsonProperty("geocoordinates")
    public final String geoCoordinates;
    public final String description;
    public final String phone;

    @JsonCreator
    public PoiDetails(@JsonProperty("id") long remoteId,
                      @JsonProperty("title") String title,
                      @JsonProperty("address") String address,
                      @JsonProperty("transport") String transport,
                      @JsonProperty("email") String email,
                      @JsonProperty("geocoordinates") String geoCoordinates,
                      @JsonProperty("description") String description,
                      @JsonProperty("phone") String phone) {
        this.remoteId = remoteId;
        this.title = title;
        this.address = address;
        this.transport = transport;
        this.email = email;
        this.geoCoordinates = geoCoordinates;
        this.description = description;
        this.phone = phone;
    }

    @Override public String toString() {
        return "PoiDetails{" +
                "remoteId=" + remoteId +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", transport='" + transport + '\'' +
                ", email='" + email + '\'' +
                ", geoCoordinates='" + geoCoordinates + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}

//{
//    "id":"2",
//    "title":"Fundació Antoni Tàpies",
//    "address":"C/ ARAGÓ, 255, 08007 Barcelona",
//    "transport":"Underground:Passeig de Gràcia -L3",
//    "email":"http://www.fundaciotapies.org/",
//    "geocoordinates":"41.39154,2.163835",
//    "description":"The Fundació opened its doors in June 1990 in the building of the former Editorial Montaner i Simon publishing house, the work of the Modernist architect Lluís Domènech i MontanerRestored and refurbished by the architects Roser Amadó and Lluís Domènech Girbau. Constructed between 1880 and 1885, at an early stage of the evolution of Catalan Modernism, the building was the first in the Eixample district to integrate industrial typology and technology, combining exposed brick and iron, into the fabric of the city centre. ",
//    "phone":"undefined"
// }
