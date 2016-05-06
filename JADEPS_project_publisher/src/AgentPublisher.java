import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.SubManager;
import model.Subscriber;




public class AgentPublisher extends Agent {

    private SubManager sm;

    protected void setup() {

        this.sm = new SubManager();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Publisher");
        sd.setName("JADE-Publisher");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new SendNumbers(this));
        addBehaviour(new ReceiveSubscriptions());

    }


    private class SendNumbers extends TickerBehaviour {

        public SendNumbers(Agent a) {
            super(a, 500);
        }

        @Override
        public void onTick() {

            Float numberToSend;

            for (Subscriber s : sm.getListOfSubscribers()) {

                numberToSend = (float) Math.random();
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(s.getAid());
                msg.setContent(String.valueOf(numberToSend));
                myAgent.send(msg);
                s.oneCallLess();

            }

            sm.removeEndedSubs();

        }
    }

    private class ReceiveSubscriptions extends CyclicBehaviour {


        @Override
        public void action() {

            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {

                try {

                    float seconds = Float.parseFloat(msg.getContent());
                    Subscriber newSub = new Subscriber(msg.getSender(), seconds);
                    sm.addSubscriber(newSub);
                    System.out.println("We have a new sub: " + newSub.getAid() + ", he will be subscribed for " + newSub.getSecondsSubscribed() + "(" + newSub.getNumberOfCalls() + ") calls");

                } catch (Exception ex) {

                    ex.printStackTrace();
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent(String.valueOf("Error processing subscriber"));
                    myAgent.send(reply);

                    System.err.println("REFUSE sent to the new sub");
                }

                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.AGREE);
                reply.setContent(String.valueOf("Subscribed succesfully"));
                myAgent.send(reply);

                System.out.println("AGREE sent to the new sub");

            }
        }
    }

}
