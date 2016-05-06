package model;

import jade.core.AID;

import java.util.ArrayList;
import java.util.Iterator;

public class SubManager {

    ArrayList<Subscriber> listOfSubscribers;

    public SubManager() {
        this.listOfSubscribers = new ArrayList<>();
    }

    public ArrayList<Subscriber> getListOfSubscribers() {
        return listOfSubscribers;
    }

    public Subscriber findSubscriberByAID(AID aid) {

        for (Subscriber s : this.listOfSubscribers) {
            if (aid.equals(s.getAid())) {
                return s;
            }
        }

        return null;
    }

    public void addSubscriber(Subscriber s) {
        if (findSubscriberByAID(s.getAid()) == null) {
            this.listOfSubscribers.add(s);
        } else {
            System.err.println("The subscriber that you want to add is already in there");
        }
    }

    public void removeSubscriberByAID(AID aid) {
        Subscriber s = findSubscriberByAID(aid);
        if (s != null) {
            this.listOfSubscribers.remove(s);
        } else {
            System.err.println("The subscriber that you want to remove does not exist");
        }
    }

    public void removeEndedSubs() {
        Iterator<Subscriber> ite = this.listOfSubscribers.iterator();
        while (ite.hasNext()) {
            Subscriber sub = ite.next();
            if (sub.getNumberOfCallsLeft() <= 0){
                System.out.println("We removed "+sub.getAid()+" because we did all the neccesary calls");
                ite.remove();
            }
        }
    }


}
