import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class AgentSubscriber extends Agent {

    private boolean first;

    private boolean subscribed;

    protected void setup() {

        first = true;
        subscribed = false;


        addBehaviour(new ReceiveNumbers());

        addBehaviour(new SendSubscription());

    }

    public AID getPublisher() {

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Publisher");
        template.addServices(sd);
        try {

            DFAgentDescription[] result = DFService.search(this, template);
            ArrayList<AID> buyers = new ArrayList<>();
            for (int i = 0; i < result.length; ++i) {
                buyers.add(result[i].getName());
            }
            return buyers.get(0);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        return null;
    }


    private class ReceiveNumbers extends CyclicBehaviour {

        @Override
        public void action() {

            MessageTemplate mt1 = MessageTemplate.MatchPerformative(ACLMessage.CANCEL);
            ACLMessage msg1 = myAgent.receive(mt1);
            if (msg1 != null) {
                System.out.println("Ended the subscription");
                myAgent.addBehaviour(new SendSubscription());
            } else {


                MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage msg = myAgent.receive(mt);
                if (msg != null) {

                    float number = Float.parseFloat(msg.getContent());
                    System.out.println("Received a number: " + number);
                }
            }
        }
    }

    private class SendSubscription extends OneShotBehaviour {

        @Override
        public void action() {


            Scanner scanner = new Scanner(System.in);
            scanner.useLocale(Locale.US);

            System.out.print("Enter how many seconds of subscription you want: ");

            boolean go = false;
            float seconds = 0.5f;
            do {
                try {
                    System.out.print("Enter how many seconds of subscription you want: ");
                    seconds = scanner.nextFloat();
                    go = true;
                } catch (InputMismatchException ex) {
                    System.out.println("Error in the format of the number");
                    go = false;
                    scanner.next();
                }
            } while (!go);

            if(seconds==0f){
                Thread t = new Thread() {
                    public void run() {
                        try {
                            getContainerController().kill();
                        } catch (StaleProxyException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
            }

            ACLMessage msg = new ACLMessage(ACLMessage.SUBSCRIBE);
            msg.addReceiver(getPublisher());
            msg.setContent(String.valueOf(seconds));
            myAgent.send(msg);


        }
    }

}
