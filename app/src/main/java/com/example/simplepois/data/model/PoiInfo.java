package com.example.simplepois.data.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.simplepois.data.PoisContract.PoiInfoEntry;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * An instance of this class is an immutable value-object representing partial info about a single
 * POI (Point of
 * Interest).
 * Each field may be null.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PoiInfo {

    /**
     * Create an instance from the current row of the provided cursor
     */
    public static PoiInfo fromCursor(Cursor cursor) {
        int remoteIdIndex = cursor.getColumnIndex(PoiInfoEntry.COL_REMOTE_ID);
        int titleIndex = cursor.getColumnIndex(PoiInfoEntry.COL_TITLE);
        int geoCoordinatesIndex = cursor.getColumnIndex(PoiInfoEntry.COL_GEOCOORDINATES);

        long remoteId = cursor.getLong(remoteIdIndex);
        String title = cursor.getString(titleIndex);
        String geoCoordinates = cursor.getString(geoCoordinatesIndex);

        return new PoiInfo(remoteId,
                title,
                geoCoordinates);
    }

    public static WithId<PoiInfo> fromCursorWithId(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(PoiInfoEntry.COL_ID);
        long id = cursor.getLong(idIndex);

        PoiInfo poiInfo = fromCursor(cursor);

        return new WithId<>(id, poiInfo);
    }

    public static List<WithId<PoiInfo>> listFromCursor(Cursor cursor) {
        List<WithId<PoiInfo>> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            WithId<PoiInfo> poiInfoWithId = PoiInfo.fromCursorWithId(cursor);
            list.add(poiInfoWithId);
        }

        return list;
    }


    public static ContentValues toValues(PoiInfo poiInfo) {
        ContentValues values = new ContentValues();
        values.put(PoiInfoEntry.COL_REMOTE_ID, poiInfo.remoteId);
        values.put(PoiInfoEntry.COL_TITLE, poiInfo.title);
        values.put(PoiInfoEntry.COL_GEOCOORDINATES, poiInfo.geoCoordinates);

        return values;
    }


    @JsonProperty("id")
    public final long remoteId;
    public final String title;
    @JsonProperty("geocoordinates")
    public final String geoCoordinates;

    @JsonCreator
    public PoiInfo(@JsonProperty("id") long remoteId,
                   @JsonProperty("title") String title,
                   @JsonProperty("geocoordinates") String geoCoordinates) {
        this.remoteId = remoteId;
        this.title = title;
        this.geoCoordinates = geoCoordinates;
    }

    @Override public String toString() {
        return "Poi{" +
                "remoteId=" + remoteId +
                ", title='" + title + '\'' +
                ", geoCoordinates='" + geoCoordinates + '\'' +
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
