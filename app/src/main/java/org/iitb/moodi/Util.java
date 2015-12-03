package org.iitb.moodi;

import org.iitb.moodi.api.TimelineResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Util {
    public static String buildStringFromIS(InputStream is) {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));

            int c;
            while((c=bf.read()) != -1){
                sb.append((char)c);
            }

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<TimelineResponse.EventTime> mergeSort(ArrayList<TimelineResponse.EventTime> whole)
    {
        ArrayList<TimelineResponse.EventTime> left = new ArrayList<TimelineResponse.EventTime>();
        ArrayList<TimelineResponse.EventTime> right = new ArrayList<TimelineResponse.EventTime>();
        int center;

        if(whole.size()==1)
            return whole;
        else
        {
            center = whole.size()/2;
            // copy the left half of whole into the left.
            for(int i=0; i<center; i++)
            {
                left.add(whole.get(i));
            }

            //copy the right half of whole into the new arraylist.
            for(int i=center; i<whole.size(); i++)
            {
                right.add(whole.get(i));
            }

            // Sort the left and right halves of the arraylist.
            left  = mergeSort(left);
            right = mergeSort(right);


            // Merge the results back together.
            merge(left,right,whole);

        }
        return whole;
    }

    private static void merge(ArrayList<TimelineResponse.EventTime> left, ArrayList<TimelineResponse.EventTime> right,
                       ArrayList<TimelineResponse.EventTime> whole) {

        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;


        // As long as neither the left nor the right arraylist has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).
        while (leftIndex < left.size() && rightIndex < right.size())
        {
            if ((left.get(leftIndex).compareTo(right.get(rightIndex)))<0)
            {
                whole.set(wholeIndex,left.get(leftIndex));
                leftIndex++;
            }
            else
            {
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }

        ArrayList<TimelineResponse.EventTime> rest;
        int restIndex;
        if (leftIndex >= left.size()) {
            // The left arraylist has been use up...
            rest = right;
            restIndex = rightIndex;
        }
        else {
            // The right arraylist has been used up...
            rest = left;
            restIndex = leftIndex;
        }

        // Copy the rest of whichever arraylist (left or right) was
        // not used up.
        for (int i=restIndex; i<rest.size(); i++) {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
}
