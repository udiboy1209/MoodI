package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

public class VenueResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    public Venue[] venues;

    public boolean getStatus() {
        return status;
    }

    public class Venue {
        @SerializedName("venue_name")
        public String venue_name;
        @SerializedName("venue_id")
        public String venue_id;
        @SerializedName("location")
        public String location;

        public double getLat(){
            if(location.indexOf(',') > 0)
                return Double.valueOf(location.substring(0,location.indexOf(',')));
            else return 0;
        }

        public double getLng(){
            if(location.indexOf(',') > 0)
                return Double.valueOf(location.substring(location.indexOf(',')+1));
            else return 0;
        }
    }
}
