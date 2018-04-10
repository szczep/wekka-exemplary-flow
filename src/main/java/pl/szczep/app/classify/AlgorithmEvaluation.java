package pl.szczep.app.classify;

import java.util.Random;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;


public class AlgorithmEvaluation {

    public static void evaluateClassifier(Instances trainData, Classifier classifier) throws Exception {

        final int numFolds = 10;

        Evaluation evaluation = new Evaluation(trainData);
//        evaluation.crossValidateModel(classifier, trainData, numFolds, new Random(1));
        evaluation.crossValidateModel(new RandomForest(), trainData, numFolds, new Random(1));

        System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
    }
}
