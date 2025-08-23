package ch.ohne.dachschaden.client;

import ch.ohne.dachschaden.client.adminBuilding.AdminBuilding;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

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
                You are a building safety expert.
                The user provides a danger to the house: "%s"
                
                Explain the danger in two to three phrases.
                Then then say shortly what this danger can do to the house.
                Separate those two things with a ;.
                
                Give the Answer in German.
                            Do not add any introduction, explanation, or extra words — only the three solutions.

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
            GASTWPrompt= "\nThe building has %s floors.".formatted(building.getGBAUJ());
        }
        String addtionalPrompt = "Additional information to the building to consider for the solutions: %s %s\n".formatted(building.getGASTW(), building.getGBAUJ());


        String prompt = """
            You are an professional for building-assurances and building-safety.
            Danger: "%s".
            Location: Switzerland, "%s".
            
            Provide exactly three solutions to make the house safer from this danger.
            Each solution must include a recommendation and an estimated cost.
            "%s".
           
            Separate the three solutions with a semicolon.
            Answer in German only.
            Do not add any introduction, explanation, or extra words — only the three solutions.
            """.formatted(danger, address, addtionalPrompt);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}
