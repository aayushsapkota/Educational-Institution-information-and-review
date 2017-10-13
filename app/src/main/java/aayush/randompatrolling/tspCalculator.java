package aayush.randompatrolling;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aayush Sapkota on 13/10/2017.
 */

public class tspCalculator {
    ArrayList<SelectedLocation> LocationList;
    addPlace addPlace1 = new addPlace();
    placeManager place = new placeManager();

    public tspCalculator() {

    }

    public String getTspRoute() {

//        placeManager locationManager = new placeManager();
//        locationManager.addCity(addPlace1.getLocationObj());
        LocationList = place.getdestinationList();
        //start Population
        TourPopulation tours = new TourPopulation(30, true);

        //Evolve
        tours = TourMutation.evolveTourPopuation(tours);
        for (int i = 0; i < 100; i++) {
            tours = TourMutation.evolveTourPopuation(tours);
        }

        String result = "Best Distance " + tours.getFittestTour().getTourDistance() + "\n\n Solution \n\n" + tours.getFittestTour();
        return result;
    }
}
