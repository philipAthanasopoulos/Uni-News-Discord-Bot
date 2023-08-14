package summarizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class Summarizer {
    public Summarizer(){
    }
    
    public void summarizeFile(String inputFile, String outputFile) {
        
        try {
            System.out.println("Reading input file "+ inputFile + " ...");
        
            // Read the text from the input file
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();
            
            // Load the OpenNLP models for sentence detection and tokenization
            SentenceModel sentenceModel = new SentenceModel(getClass().getResourceAsStream("/en-sent.bin"));
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(sentenceModel);
            TokenizerModel tokenizerModel = new TokenizerModel(getClass().getResourceAsStream("/en-token.bin"));
            TokenizerME tokenizer = new TokenizerME(tokenizerModel);
            
            // Detect sentences in the text
            String documentText = builder.toString();
            Span[] sentenceSpans = sentenceDetector.sentPosDetect(documentText);
            List<String> sentences = Arrays.stream(sentenceSpans)
                                            .map(span -> span.getCoveredText(documentText).toString())
                                            .collect(Collectors.toList());
            
            // Tokenize the sentences

            List<String[]> tokenizedSentences = sentences.stream().map(tokenizer::tokenize).collect(Collectors.toList());
            
            // Summarize the text using the first sentence of each paragraph
            List<String> summarySentences = tokenizedSentences.stream().filter(tokens -> tokens.length > 0).map(tokens -> tokens[0]).collect(Collectors.toList());
            String summary = String.join(" ", summarySentences);
            
            // Save the summary to a file
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(summary);
            writer.close();
            
            System.out.println("Summary saved to " + outputFile);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}