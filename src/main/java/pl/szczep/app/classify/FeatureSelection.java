package pl.szczep.app.classify;

import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.RemoveUseless;


public class FeatureSelection {

    public static Instances removeIrrelevantFeatures(Instances dataSet) throws Exception {

//        RemoveUseless removeUseless = new RemoveUseless();
//        removeUseless.setOptions(new String[]{"-M", "99"});
//        removeUseless.setInputFormat(dataSet);
//
//        Instances filteredData = Filter.useFilter(dataSet, removeUseless);

        InfoGainAttributeEval eval = new InfoGainAttributeEval();
        Ranker search = new Ranker();
        search.setOptions(new String[] { "-T", "0.001" });	// information gain threshold
        AttributeSelection attSelect = new AttributeSelection();
        attSelect.setEvaluator(eval);
        attSelect.setSearch(search);

        // apply attribute selection
        attSelect.SelectAttributes(dataSet);

//         remove the attributes not selected in the last run
        return attSelect.reduceDimensionality(dataSet);

    }
}
