package pl.szczep.app;


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import pl.szczep.app.parsers.LogsAdjuster;
import pl.szczep.app.parsers.Tokenizer;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Classify
 */
public class Classify {

    public static void main(String[] args) throws Exception {

//        CSVLoader loader = new CSVLoader();
//        loader.setSource(new File(args[0]));
//        Instances data = loader.getDataSet();

        Path path = Paths.get(Classify.class.getClassLoader()
            .getResource("lsd_data.txt").toURI());

        List<String> lines = LogsAdjuster.mergeLinesWithNoTimeStamp(
            LogsAdjuster.cutNewDayLines(
                Files.readAllLines(path)
            )
        );

        ArrayList<Attribute> attributeList = new ArrayList<>();
        attributeList.add(new Attribute("time", (List<String>) null));
        attributeList.add(new Attribute("message", (List<String>) null));
        attributeList.add(new Attribute("person", (List<String>) null));

        Instances dataSet = new Instances("MyRelation", attributeList, 0);

        lines.forEach(line -> {
            double[] vals = new double[dataSet.numAttributes()];
            vals[0] = dataSet.attribute("time").addStringValue(
                MessageTime.createMessageTime(Tokenizer.extractTime(line)).toString()
            );
            vals[1] = dataSet.attribute("message").addStringValue(
                Tokenizer.extractMessage(line)
            );
            vals[2] = dataSet.attribute("person").addStringValue(
                Tokenizer.extractSender(line)
            );
            dataSet.add(new DenseInstance(1, vals));
        });

        dataSet.setClassIndex(attributeList.size() - 1);

        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataSet);
        saver.setFile(new File("test.arff"));
        saver.writeBatch();


//
//        J48 classifier = new J48(); // decision tree
//        classifier.setOptions(new String[] { "-U" });

    }

}
