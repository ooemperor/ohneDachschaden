package ch.ohne.dachschaden.client;

import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    /**
     * Return an Explanation of a Danger
     * Structure: 1: What is it? 2. What it does to the House?
     */
    public String getDangerExplanation(String danger) {
        String prompt = """
                You are a certified specialist for building safety.
                The customer provides you with a specific danger for the house: "%s"
                
                Your task:
                
                Explain this danger in 2–3 sentences in German, and make sure to mention several realistic reasons or mechanisms how this danger can affect the building (at least 2, maximum 3).
                
                In a short and clear statement, describe what this danger could do to the house.
                
                Format your answer exactly like this:
                
                [Explanation in 2–3 sentences, mentioning several realistic reasons] + [Short consequence for the house]
                
                
                Do not add anything else. Always answer in German.
                """.formatted(danger);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }

    /**
     * Return three Solutions for Danger
     */
    public String getDangerSolutions(AdminBuilding building, String danger, String address) {
        String GBAUJPrompt= "";
        String GASTWPrompt= "";
        if (building.getGBAUJ() != null) {
            GBAUJPrompt = "\nBuilt in %s.".formatted(building.getGBAUJ());
        }
        if (building.getGASTW() != null) {
            GASTWPrompt= "\nThe building has %s floors.".formatted(building.getGASTW());
        }
        String addtionalPrompt = "Additional information to the building to consider for the solutions: %s %s\n".formatted(GBAUJPrompt, GASTWPrompt);


        String prompt = """
                You are a professional for building assurances and building safety.
                Danger: "%s"
                Location: Switzerland, "%s"
                
                Your task:
                Provide exactly six different, established solutions that a typical private house owner can implement to make the house safer from this danger.
                Do not suggest measures that must be carried out by the Bund, Kanton, Gemeinde, or other public authorities — only realistic actions for the property owner.
                Six solution must be low-cost, two medium-cost, and tone high-cost.
                
                Each solution must contain:
                A clear recommendation (short and precise, based on commonly used methods in Switzerland).
                An estimated cost range in CHF (realistic for Swiss residential houses).
                "%s"
                
                Write the six solutions in German only.
                Separate the solutions with (@).
                Do not add introductions, explanations, numbering, or extra words — only the six solutions.
                
                Format it like this: [Günstige Lösung: Empfehlung und Kosten in CHF (lowend - highend)] @ [Günstige Lösung: Empfehlung und Kosten in CHF (lowend - highend)] @ [Günstige Lösung: Empfehlung und Kosten in CHF (lowend - highend)] @ [Mittlere Lösung: Empfehlung und Kosten in CHF (lowend - highend)] @ [Mittlere Lösung: Empfehlung und Kosten in CHF (lowend - highend)] @ [Teure Lösung: Empfehlung und Kosten in CHF (lowend - highend)]
            """.formatted(danger, address, addtionalPrompt);

        String answer = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        System.out.println(answer);
        return answer;
    }
}
