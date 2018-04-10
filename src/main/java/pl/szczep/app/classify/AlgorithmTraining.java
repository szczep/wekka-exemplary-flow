package pl.szczep.app.classify;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.meta.Bagging;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveUseless;


public class AlgorithmTraining {

    public static AbstractClassifier trainClassyfier(Instances dataSet) throws Exception {

        RandomForest forest = new RandomForest();
        forest.buildClassifier(dataSet);

        return forest;
    }
}
