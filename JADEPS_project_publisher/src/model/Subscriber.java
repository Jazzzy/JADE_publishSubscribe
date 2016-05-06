package model;

import jade.core.AID;

/**
 * Created by gladi on 06/05/2016.
 */
public class Subscriber {

    private AID aid;
    private Integer numberOfCalls;
    private Float secondsSubscribed;
    private Integer numberOfCallsLeft;

    public Subscriber(AID aid, Float secondsSubscribed) {
        this.aid = aid;
        this.secondsSubscribed = secondsSubscribed;
        this.numberOfCalls = (int) (secondsSubscribed / 0.5f);
        this.numberOfCallsLeft = this.numberOfCalls;
    }


    public AID getAid() {
        return aid;
    }

    public void setAid(AID aid) {
        this.aid = aid;
    }

    public Integer getNumberOfCalls() {
        return numberOfCalls;
    }

    public void setNumberOfCalls(Integer numberOfCalls) {
        this.numberOfCalls = numberOfCalls;
    }

    public Float getSecondsSubscribed() {
        return secondsSubscribed;
    }

    public void setSecondsSubscribed(Float secondsSubscribed) {
        this.secondsSubscribed = secondsSubscribed;
    }

    public Integer getNumberOfCallsLeft() {
        return numberOfCallsLeft;
    }

    public void setNumberOfCallsLeft(Integer numberOfCallsLeft) {
        this.numberOfCallsLeft = numberOfCallsLeft;
    }

    public void oneCallLess() {
        if (this.numberOfCallsLeft > 0) {
            this.numberOfCallsLeft--;
        } else {
            System.err.println("The number of calls left would have been less than 0");
        }
    }
}
