package aayush.randompatrolling;


public class TourPopulation {

    addPlace[] tours;

    public TourPopulation(int tourPopulationSize, boolean start){
        tours = new addPlace[tourPopulationSize];
        if(start){
            for(int i=0;i<tourPopulationSize; i++){
                addPlace newTour = new addPlace();
                newTour.generateIndividual();
                saveTour(i, newTour);
            }
        }
    }

    //saves tour to a index
    public void saveTour(int i, addPlace tour){
        tours[i] = tour;
    }

    //get tour through a index
    public addPlace getTour(int i){
        return tours[i];
    }

    //Get the shortest(in GA Terms fittest) tour overall
    public addPlace getFittestTour(){
        addPlace fittestTour = tours[0];

        for(int i=1;i < tours.length;i++){
            if(fittestTour.getFitness() <= getTour(i).getFitness()){
                fittestTour = getTour(i);
            }
        }
        return fittestTour;
    }

    public int tourPopulationSize(){
        return tours.length;
    }



}
