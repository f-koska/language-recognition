import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

class LanguagesMap{
    Map<String, List<String>> languages = new HashMap<>();

    public LanguagesMap(){ }
    public void getStringWithoutSpecialCharacters(String string) {
        String[] tab = string.split(" ");
        string = string.replaceAll("[^a-zA-z]", " ");
        string = string.replaceAll("\\b" + tab[tab.length - 1] + "\\b", "");
        if (!languages.containsKey(tab[tab.length - 1])) {
            languages.put(tab[tab.length - 1], new ArrayList<>());
        }
        languages.get(tab[tab.length-1]).add(string.toLowerCase());
    }
}
public class Main {

    public static void main(String[] args) throws FileNotFoundException {

        LanguagesMap languages = new LanguagesMap();
        List<Perceptron> perceptronList = new ArrayList<>();
        double bias = 0.7;
        double learningRate = 0.2;
        double accuracy = 0;

        Scanner scanner = new Scanner(new File("src/train-set"));

        while (scanner.hasNextLine()) {
            languages.getStringWithoutSpecialCharacters(scanner.nextLine());
        }

        for (String stringTmp : languages.languages.keySet()) {
            perceptronList.add(new Perceptron(stringTmp, bias,learningRate));
        }

        double result = 0;
        String language = "";
        double value = 0;
        int numberOfText = languages.languages.entrySet().stream().max(Comparator.comparing(x->x.getValue().size())).get().getValue().size();

        while (accuracy<0.7) {
            accuracy = 0;
            for (Perceptron perceptron : perceptronList){
                perceptron.counterOfMatchSpecLang=0;
            }
            for (int index = 0; index < numberOfText; index++) {

                for (String languageType : languages.languages.keySet()) {

                    String text = languages.languages.get(languageType).get(index);
                    double[] vectors = Perceptron.countVectorsOfLettersInText(text);

                    for (Perceptron perceptron : perceptronList) {
                        result = perceptron.countNet(vectors);
                        if (value < result) {
                            value = result;
                            language = perceptron.language;
                        }
                    }
                    value = 0;
                    if (language.equals(languageType)) {
                        for(Perceptron perceptron : perceptronList){
                            if(perceptron.language.equals(language)){
                                perceptron.counterOfMatchSpecLang++;
                            }
                        }
                    } else {
                        for (Perceptron perceptron : perceptronList) {
                            if (perceptron.language.equals(languageType)) {
                                perceptron.changeWeights(vectors, 1);
                            }
                        }
                    }
                }
            }
            for(Perceptron perceptron : perceptronList){
                accuracy+= perceptron.counterOfMatchSpecLang /(double) numberOfText;
            }
            accuracy/=languages.languages.size();
        }
        System.out.println("Accuracy: " +new BigDecimal(accuracy*100).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros() + "%");
        for (Perceptron perceptron : perceptronList){
            System.out.println(perceptron.language + "---------" + perceptron.counterOfMatchSpecLang);
        }

        System.out.println();
        Scanner scannerIn = new Scanner(System.in);
        int x = 0;
        System.out.println("Wciśnij: \n1.by użyć pliku testowego. \n2.by wpisywać własny tekst. \n3.by wyjść.");
        x = scannerIn.nextInt();
        while (x!=3){
            if(x==1){
                scanner = new Scanner(new File("src/test-set"));
                while (scanner.hasNextLine()){
                    String text = scanner.nextLine();
                    Perceptron.test(text,perceptronList);
                }
            }
            if(x==2){
                new Window(perceptronList);
            }
            x=scannerIn.nextInt();
        }
   }
}

