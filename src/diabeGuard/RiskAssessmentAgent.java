package diabeGuard;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Arrays;

public class RiskAssessmentAgent extends Agent {
    
    protected void setup() {
        addBehaviour(new CyclicBehaviour(this) {
            public void action() {
            	
//            	MessageTemplate mtRequest = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
//                ACLMessage msg = receive(mtRequest);
            	ACLMessage msg = receive();
                
                if (msg != null) {
                	
                    // Parse the health data from the message
                    String healthData = msg.getContent();
                    
                    
                    if(healthData != null && healthData.contains("Instrctuctions:")) {
                    	
                    	String[] arr = healthData.split("/");
                    	//System.out.println(healthData);
                    	System.out.println(arr[0]);
                    	System.out.println("Medications: "+arr[1]);
                    	
                    	block();
                    }else {
                    	
                    	String[] healthDataArr = healthData.split(",");
                        
                        int age = Integer.valueOf(healthDataArr[0].split(":")[1]);
                        int bmi = Integer.valueOf(healthDataArr[1].split(":")[1]);
                        int average_glucose = Integer.valueOf(healthDataArr[2].split(":")[1]);
                        int family_history = Integer.valueOf(healthDataArr[3].split(":")[1]);
                        int exercise_frequency = Integer.valueOf(healthDataArr[4].split(":")[1]);
                        
                        int gmarker_a = Integer.valueOf(healthDataArr[5].split(":")[1]);
                        int gmarker_b = Integer.valueOf(healthDataArr[6].split(":")[1]);
                        int gmarker_c = Integer.valueOf(healthDataArr[7].split(":")[1]);
                        
                    	
                    	double riskScore = calculateRiskScore(
                    			age,bmi,average_glucose,family_history,exercise_frequency
                    			);
                    	
                    	double geneticScore = calculateGeneticScore(gmarker_a,gmarker_b,gmarker_c);
                    	
                    	if(riskScore >= 180) {
                    		System.out.println("CONTACT DOCTOR IMMEDIATELY !!!");
                    	}else {
                    		// Send the risk score to the Lifestyle Recommendation Agent
                            ACLMessage toLifestyleAgent = new ACLMessage(ACLMessage.REQUEST);
                            toLifestyleAgent.addReceiver(new jade.core.AID("lifestylerecagent", jade.core.AID.ISLOCALNAME));
                            toLifestyleAgent.setContent(riskScore+"|"+geneticScore);
                            send(toLifestyleAgent);
                    	}
                        
                    }
                    
                } else {
                    block();
                }
            }
        });
    }

    // Placeholder method to calculate risk score
    private double calculateRiskScore(
    		int age, 
    		int bmi, 
    		int average_glucose, 
    		int family_history, 
    		int exercise_frequency) {
    	
        return (age*0.25)+(bmi*0.25)+(average_glucose*0.75)+(family_history*0.5)+(exercise_frequency*0.5);
    }
    private double calculateGeneticScore(int gmarker_a, int gmarker_b, int gmarker_c) {
    	return (1.5*gmarker_a)+(1*gmarker_b)+(0.5*gmarker_c);
    }
}
