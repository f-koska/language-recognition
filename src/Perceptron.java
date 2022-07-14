import java.awt.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Perceptron {

    double[] weights;
    String language;
    double counterOfMatchSpecLang = 0;
    double bias;
    double learningRate;


    public Perceptron(String language, double bias, double learningRate){
        this.language = language;

        weights = new double['z'-'a'+2];
        for(int i =0 ; i<weights.length-2 ; i++){
            weights[i] = (Math.random()*11)-5;
        }
        weights[weights.length-1]=bias;
        //this.bias=bias;
        this.learningRate=learningRate;
    }

    public void changeWeights(double[] vectors,int result){
        for(int i = 0; i<weights.length ; i++){
            weights[i]=weights[i] + learningRate*result*vectors[i];
        }
    }

    public double countNet(double[] vectors){
        double value = 0;
        for(int i = 0; i<weights.length ; i++){
            value+=vectors[i]*weights[i];
        }
        //value+=bias;
        return  value;
    }

    public static double[] countVectorsOfLettersInText(String text){
        double[] vectors = new double[27];
        for(char letter = 'a' ; letter <= 'z' ; letter++){
            int counter = 0;

            for(int i = 0 ; i<text.length() ; i++){
                if(text.charAt(i)==letter){
                    counter++;
                }
            }
            vectors[letter-'a'] = counter/(double)text.length()*100;
            vectors[vectors.length-1]=1;

        }
        return vectors;
    }

    public static String test(String string, List<Perceptron> perceptronList){
            String output="Zdanie: \"" + string + "\" jest w języku ";
            String[] tab = string.split(" ");
            string = string.replaceAll("[^a-zA-z]", " ");
            string = string.replaceAll("\\b" + tab[tab.length - 1] + "\\b", "");
            double[] vectors = countVectorsOfLettersInText(string);
            String language = "";
            double result = 0;
            double value = Integer.MIN_VALUE;
            for(Perceptron perceptron : perceptronList){
                result = perceptron.countNet(vectors);
                if (value < result) {
                    value = result;
                    language = perceptron.language;
                }
            }
            if(language.equals(tab[tab.length - 1])){
                System.out.println( output +" " +new Locale(language).getDisplayLanguage(Locale.ENGLISH));
            }else {
                System.err.println(output +" "+ new Locale(language).getDisplayLanguage(Locale.ENGLISH));
            }
            return language;
    }
}
