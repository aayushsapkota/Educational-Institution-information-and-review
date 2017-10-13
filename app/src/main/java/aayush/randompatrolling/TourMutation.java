package aayush.randompatrolling;

import java.text.CharacterIterator;

/**
 * Created by Aayush Sapkota on 12/10/2017.
 */

public class TourMutation {

    private static final double mutuationRate = 0.015;
    public static final int competitionSize = 5;
    private static final boolean enchance = true;

    public static TourPopulation evolveTourPopuation(TourPopulation tPopulation) {
        TourPopulation newTourPopulation = new TourPopulation(tPopulation.tourPopulationSize(), false);

        int enchancementValue = 0;
        if (enchance) {
            newTourPopulation.saveTour(0, tPopulation.getFittestTour());
            enchancementValue = 1;
        }
        //Crossover tours
        //loop over new tours size to create individual/baby from current tours
        for (int i = enchancementValue; i < newTourPopulation.tourPopulationSize(); i++) {
            addPlace mom = competitionSelection(tPopulation);
            addPlace dad = competitionSelection(tPopulation);
            //
            addPlace baby = crossover(mom, dad);
            //add baby to new tours
            newTourPopulation.saveTour(i, baby);
        }

        //Mutate and add genetics
        for (int i = enchancementValue; i < newTourPopulation.tourPopulationSize(); i++) {
            mutate(newTourPopulation.getTour(i));
        }

        return newTourPopulation;

    }

    //crossover parents mom and dad to create babies
    public static addPlace crossover(addPlace mom, addPlace dad) {
        addPlace baby = new addPlace();

        int startPosition = (int) (Math.random() * mom.tourSize());
        int endPosition = (int) (Math.random() * dad.tourSize());

        for (int i = 0; i < baby.tourSize(); i++) {
            if (startPosition < endPosition && i < endPosition && i > startPosition) {
                baby.setLocation(i, mom.getLocation(1));
            } else if (startPosition > endPosition) {
                if (!(i < startPosition && i > endPosition)) {
                    baby.setLocation(i, mom.getLocation(i));
                }
            }
        }

        for (int i = 0; i < dad.tourSize(); i++) {
            if(!baby.hasLocation(dad.getLocation(i))){
                for(int j = 0; j < baby.tourSize(); j++){
                    if(baby.getLocation(j)==null){
                        baby.setLocation(i, dad.getLocation(i));
                        break;
                    }
                }
            }
        }
        return baby;
    }

    //Mutate a tour using swap mutation
    private static void mutate(addPlace tour){
        for(int i=0;i<tour.tourSize();i++){
            if(Math.random() < mutuationRate){
                int j = (int) (tour.tourSize()*Math.random());

                SelectedLocation location1 = tour.getLocation(i);
                SelectedLocation location2 = tour.getLocation(j);

                //swap
                tour.setLocation(j, location1);
                tour.setLocation(i, location2);
            }
        }
    }

    private static addPlace competitionSelection(TourPopulation tPopulation1){
        //create competiting tour population
        TourPopulation competition = new TourPopulation(competitionSize,false);

        //for each competiton get a random tour and add it
        for(int i=0;i<competitionSize; i++){
            int randomIndex =  (int) (Math.random()*tPopulation1.tourPopulationSize());
            competition.saveTour(i,tPopulation1.getTour(randomIndex));
        }
        addPlace fittest = competition.getFittestTour();
        return fittest;

    }
}

