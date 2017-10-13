package aayush.randompatrolling;

import java.util.ArrayList;



public class placeManager {

    private static ArrayList<SelectedLocation> destinationLocations = new ArrayList<SelectedLocation>();

    public static void addCity(SelectedLocation location){
        destinationLocations.add(location);
    }

    public static SelectedLocation getCity(int index){
        return (SelectedLocation) destinationLocations.get(index);
    }

    public static int numberOfLocations(){
        return destinationLocations.size();
    }

    public ArrayList<SelectedLocation> getdestinationList(){
        return destinationLocations;
    }
}
