package pl.szczep.app.classify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pl.szczep.app.parsers.Booleans;
import pl.szczep.app.parsers.MessageTime;
import pl.szczep.app.parsers.TextParser;
import pl.szczep.app.parsers.Tokenizer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;


public class FeatureExtracting {

    private static final int MIN_POSTS = 100;

    public static List<String> senders;
    public static List<String> allWords;

    public static Instances defineAndExtractFeatures(List<String> data) {

        senders = extractSenders(data);
        List<String> filteredData = data
            .stream()
            .filter(line -> senders.contains(TextParser.extractSender(line)))
            .collect(Collectors.toList());

        allWords = Tokenizer.extractWords(filteredData);
//        allWords = Collections.emptyList();

        Instances dataSet = defineFeatures();

        int[] idx = {0};
        filteredData.forEach(line -> {
            printProgress(filteredData, idx);

            dataSet.add(createSingleInstanceFromMessage(line));
        });

        return dataSet;
    }



    public static Instance createSingleInstanceFromMessageWithoutClass(String line) {
        DenseInstance singleInstanceFromMessage = createSingleInstanceFromMessage(line);
        singleInstanceFromMessage.deleteAttributeAt(singleInstanceFromMessage.numAttributes()-1);
        return singleInstanceFromMessage;
    }

    public static DenseInstance createSingleInstanceFromMessage(String line) {


        DenseInstance newInstance = new DenseInstance(allWords.size() + 8);

        for (int i = 0; i < allWords.size(); i++) {
            newInstance.setValue(i, TextParser.isAnyWordIncluded(line,
                new HashSet<>(Collections.singletonList(allWords.get(i)))) ? 1 : 0);
        }

        newInstance.setValue(allWords.size(), MessageTime.createMessageTime(TextParser.extractTime(line)).ordinal());

        newInstance.setValue(allWords.size() + 1, TextParser.extractMessage(line).length());

        newInstance.setValue(allWords.size() + 2, TextParser.isAnyStringIncluded(line,
            new HashSet<>(Arrays.asList("http", "https"))) ? 1 : 0);

        newInstance.setValue(allWords.size() + 3, TextParser.isAnyWordIncluded(line,
            new HashSet<>(Arrays.asList("stawka", "oferta", "kasa", "hays", "cebulak", "hajs"))) ? 1 : 0);

        newInstance.setValue(allWords.size() + 4, TextParser.isAnyWordIncluded(line,
            new HashSet<>(Arrays.asList("sex", "laska", "dziwka", "panienka", "dupa", "foch"))) ? 1 : 0);

        newInstance.setValue(allWords.size() + 5, TextParser.isAnyStringIncluded(line,
            new HashSet<>(Arrays.asList(":)", ";)", ":(", ";]", ";("))) ? 1 : 0);

        newInstance.setValue(allWords.size() + 6, TextParser.isAnyStringIncluded(line,
            new HashSet<>(Arrays.asList(":>", ":D", ":p"))) ? 1 : 0);

        newInstance.setValue(allWords.size() + 7, senders.indexOf(TextParser.extractSender(line)));
        return newInstance;
    }


    public static Instances defineFeatures() {
        ArrayList<Attribute> attributeList = new ArrayList<>();

        allWords.forEach(
            word -> attributeList.add(new Attribute(word, Booleans.stringValues()))
        );

        attributeList.add(new Attribute("messageTime", MessageTime.stringValues()));
        attributeList.add(new Attribute("messageLength"));
        attributeList.add(new Attribute("linkIncluded", Booleans.stringValues()));
        attributeList.add(new Attribute("talkedAboutStawki", Booleans.stringValues()));
        attributeList.add(new Attribute("sexIncluded", Booleans.stringValues()));
        attributeList.add(new Attribute("emoty", Booleans.stringValues()));
        attributeList.add(new Attribute("emotyExtra", Booleans.stringValues()));
        attributeList.add(new Attribute("person", senders));
        Instances dataSet = new Instances("MyRelation", attributeList, 0);
        dataSet.setClassIndex(dataSet.numAttributes() - 1);
        return dataSet;
    }

    private static List<String> extractSenders(List<String> data) {
        Set<String> senders = new HashSet<>();

        data.forEach(line -> {
            senders.add(TextParser.extractSender(line));
        });

        Map<String, Integer> countPosts = new HashMap<>();
        senders.forEach(sender -> countPosts.put(sender, 0));

        data.forEach(line -> {
            String sender = TextParser.extractSender(line);
            countPosts.put(sender, countPosts.get(sender) + 1);
        });

        return senders
            .stream()
            .filter(sender -> countPosts.get(sender) > MIN_POSTS)
            .collect(Collectors.toList());
    }

    private static void printProgress(List<String> filteredData, int[] idx) {
        if (idx[0] % 10 == 0) { System.out.println(idx[0] / 10 + "/" + filteredData.size() / 10); }
        idx[0]++;
    }
}
