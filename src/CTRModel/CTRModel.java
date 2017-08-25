package CTRModel;

import ads.Utility;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zzc on 8/24/17.
 */
public class CTRModel {
    private static ArrayList<Double> weightsLogistic;
    private static Double biasLogistic;

    protected CTRModel(String logisticRegModelFile, String gdbtModelPath) {
        weightsLogistic = new ArrayList<Double>();
        try {
            BufferedReader ctrLogisticReader = new BufferedReader(new FileReader(logisticRegModelFile));
            String line;
            while ((line = ctrLogisticReader.readLine()) != null) {
                JSONObject jsonObject = new JSONObject(line);
                JSONArray weights = jsonObject.isNull("weights") ? null : jsonObject.getJSONArray("weights");
                for (int i = 0; i < weights.length(); i++) {
                    weightsLogistic.add(weights.getDouble(i));
                    System.out.println("weights=" + weights.getDouble(i));
                }
                biasLogistic = jsonObject.getDouble("bias");
                System.out.println(jsonObject.getDouble("bias"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double predictCTRWithLogisticRegression(ArrayList<Double> features) {
        double pClick = biasLogistic;
        if (features.size() != weightsLogistic.size()) {
            System.out.println("ERROR: size of features doesn't equals to weights");
            return pClick;
        }
        for (int i = 0; i < features.size(); i++) {
            pClick = pClick + weightsLogistic.get(i) * features.get(i);
        }
        System.out.println("sigmoid imput pclick = " + pClick);
        pClick = 1 / (1 + Math.exp(-pClick));
        return pClick;
    }
}
