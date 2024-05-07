package diabeGuard;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class LifestyleRecommendationAgent extends Agent {
    
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    // Parse the risk score from the message
                    double riskScore = Double.parseDouble(msg.getContent().split("|")[0]);
                    double geneticScore = Double.parseDouble(msg.getContent().split("|")[1]);
                    
                    // Get lifestyle recommendations based on the risk score
                    String lifestyleRecommendations = getLifestyleRecommendations(riskScore, geneticScore);
                    String medicationRecommendations = getMedicationRecommendations(riskScore, geneticScore);
                    
                    // Send lifestyle recommendations back to the Risk Assessment Agent
                    ACLMessage reply = msg.createReply();
                    //ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                    reply.setPerformative(ACLMessage.INFORM);
                    //reply.addReceiver(msg.getSender());
                    reply.setContent(lifestyleRecommendations+"/"+medicationRecommendations);
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }

    // Placeholder method to get lifestyle recommendations
    private String getLifestyleRecommendations(double riskScore, double geneticScore) {
        // Dummy implementation, replace with actual recommendations based on risk score
        if (riskScore < 80 && geneticScore > 2) {
            return "Instrctuctions: You are not at a risk, Follow the usual diaery and exercise routines";
        } else {
            return "Instrctuctions: You are at a risk of having diabitics. Do exercise one hour per day, limit foods contains sugar and fat ";
        }
    }
    private String getMedicationRecommendations(double riskScore, double geneticScore) {
        // Dummy implementation, replace with actual recommendations based on risk score
        if (riskScore < 80 && geneticScore > 2) {
            return "No medications needed";
        } else {
            return "Take Metformin 20mg every night";
        }
    }
}