import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Window extends JFrame implements ActionListener {
List<Perceptron> perceptronList;
Map<String,JLabel> map = new HashMap<>();
JTextArea textArea;

    public Window(List<Perceptron> perceptronList){
        this.perceptronList = perceptronList;
        JPanel jPanelMain = new JPanel();
        jPanelMain.setBackground(new Color(204, 228, 255));;
        BorderLayout borderLayout = new BorderLayout();
        jPanelMain.setBorder(new EmptyBorder(30,50,30,50));
        jPanelMain.setLayout(borderLayout);
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane jScrollPane = new JScrollPane(textArea);
        textArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
        this.textArea=textArea;
        jPanelMain.add(jScrollPane,BorderLayout.CENTER);


        JPanel jPanelBottom = new JPanel();
        jPanelBottom.setBackground(new Color(204, 228, 255));
        jPanelBottom.setBorder(new EmptyBorder(30,50,10,50));
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(60);
        jPanelBottom.setLayout(flowLayout);
        jPanelMain.add(jPanelBottom,BorderLayout.PAGE_END);

        Font font = new Font(Font.MONOSPACED,Font.BOLD,17);
        for(Perceptron str : perceptronList){
            JLabel jLabel = new JLabel(new Locale(str.language).getDisplayLanguage(Locale.ENGLISH));
            jLabel.setFont(font);
            map.put(str.language,jLabel);
            jPanelBottom.add(jLabel);
        }

        Button button = new Button("OK");
        jPanelMain.add(button,BorderLayout.PAGE_START);

        button.addActionListener(this);

        add(jPanelMain);
        setSize(700,350);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String language = Perceptron.test(textArea.getText(),perceptronList);
        SwingUtilities.invokeLater(() -> {
            for (String s : map.keySet()){
                map.get(s).setForeground(Color.BLACK);
            }
            map.get(language).setForeground(Color.RED);
        });

    }
}
