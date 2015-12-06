package org.iitb.moodi.api;

import com.google.gson.annotations.SerializedName;

public class FriendFinderRespone {
    @SerializedName("status")
    private boolean status;

    @SerializedName("data")
    public Friend[] friends;

    public class Friend{
        @SerializedName("name")
        public String name;

        @SerializedName("fbid")
        public String fbid;

        @SerializedName("lat_lon")
        public String location;

        @SerializedName("timestamp")
        public String lastUpdateTime;

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

        public String initials(){
            String in=name.substring(0,1);
            for(int i=1; i<name.length(); i++)
                if(name.charAt(i)==' ' && i+1<name.length())
                    in+=name.charAt(i+1);

            return in;
        }
    }
}
