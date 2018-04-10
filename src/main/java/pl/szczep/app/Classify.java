package pl.szczep.app;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import pl.szczep.app.classify.AlgorithmEvaluation;
import pl.szczep.app.classify.AlgorithmTraining;
import pl.szczep.app.classify.DataMining;
import pl.szczep.app.classify.FeatureExtracting;
import pl.szczep.app.classify.FeatureSelection;
import weka.classifiers.AbstractClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

/**
 * Classify
 */
public class Classify {


    public static void main(String[] args) throws Exception {


        System.out.println("DataMining........" + LocalDateTime.now());
        List<String> myData = DataMining.readAndAdjustData();

        System.out.println("FeatureDefining........" + LocalDateTime.now());
        Instances dataSet = FeatureExtracting.defineAndExtractFeatures(myData);


        System.out.println("FeatureSelection........" + LocalDateTime.now());
        dataSet = FeatureSelection.removeIrrelevantFeatures(dataSet);

        saveWords(FeatureExtracting.allWords);

        saveTrainingSet(dataSet);

        System.out.println("AlgorithmTraining........" + LocalDateTime.now());
        AbstractClassifier classifier = AlgorithmTraining.trainClassyfier(dataSet);

        System.out.println("AlgorithmEvaluation........" + LocalDateTime.now());
        AlgorithmEvaluation.evaluateClassifier(dataSet, classifier);

        System.out.println("done........" + LocalDateTime.now());


        classifyInline(classifier, dataSet);

    }

//    public static void main(String[] args) throws Exception {
//
//        FeatureExtracting.senders = read("senders.txt");
//        FeatureExtracting.allWords = read("words.txt");
//
//        Instances dataSet = readArff();
//
//        System.out.println("AlgorithmTraining........" + LocalDateTime.now());
//        AbstractClassifier classifier = AlgorithmTraining.trainClassyfier(dataSet);
//
//        System.out.println("done........" + LocalDateTime.now());
//
//        classifyInline(classifier, dataSet);
//    }

    private static void classifyInline(AbstractClassifier classifier, Instances dataSet) {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        in.lines().forEach(
            line -> {
                try {
                    dataSet.add(FeatureExtracting.createSingleInstanceFromMessageWithoutClass(line));

                    Double messageSender = classifier.classifyInstance(dataSet.instance(dataSet.numInstances() - 1));

                    double[] v = classifier.distributionForInstance(dataSet.instance(dataSet.numInstances() - 1));

                    for (int i = 0; i < v.length; i++) {
                        System.out.println("Instance distribution for " + dataSet.classAttribute().value(i) + " is " + v[i]);
                    }

                    System.out.println("The message wrote: " + dataSet.classAttribute().value(messageSender.intValue()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        );
    }

    private static void saveTrainingSet(Instances dataSet) throws IOException {
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataSet);
        saver.setFile(new File("test.arff"));
        saver.writeBatch();
    }

    private static Instances readArff() throws IOException {
        BufferedReader reader =
            new BufferedReader(new FileReader("data.arff"));
        ArffLoader.ArffReader arff = new ArffLoader.ArffReader(reader, 1000);
        Instances data = arff.getStructure();
        data.setClassIndex(data.numAttributes() - 1);
        Instance inst;
        while ((inst = arff.readInstance(data)) != null) {
            data.add(inst);
        }
        return data;
    }


    private static void saveWords(List<String> allWords) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("words.txt"));
        allWords.forEach(pw::println);
        pw.close();
    }


    private static List<String> read(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(fileName);
        return Files.readAllLines(path);
    }

}
