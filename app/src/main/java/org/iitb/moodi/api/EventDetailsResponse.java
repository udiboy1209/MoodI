package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kalpesh on 29/11/15.
 */
public class EventDetailsResponse {
    @SerializedName("status")
    private boolean status;
    @SerializedName("data")
    private EventDetails[] eventDetails;

    public boolean getStatus() {
        return status;
    }

    public int getMin() {
        if (eventDetails.length==0)
            return 0;
        return eventDetails[0].min_team_size;
    }

    public int getMax() {
        if (eventDetails.length==0)
            return 0;
        return eventDetails[0].max_team_size;
    }

    public class EventDetails {
        @SerializedName("event_id")
        public int event_id;
        @SerializedName("min_team_size")
        public int min_team_size;
        @SerializedName("max_team_size")
        public int max_team_size;
    }
}
