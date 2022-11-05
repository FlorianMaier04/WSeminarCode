package rendering.input;

import logic.PhysicThread;
import logic.objects.PhysikObjekt;
import tools.FeedbackBuilder;

import javax.swing.*;
import java.awt.event.*;

public class InputFrame extends JFrame {

    private JTextField rektField;
    private JTextField dekField;
    private JTextField distanceField;
    private JButton startButton;
    private JTextField nameField;
    private JComboBox<String> planetDropdown;

    private String currentSelectedPlanet;


    /**
     * Create the frame.
     */
    public InputFrame(PhysicThread pt) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 450, 329);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        planetDropdown = new JComboBox();
        getContentPane().add(planetDropdown);


        nameField = new JTextField();
        getContentPane().add(nameField);

        JPanel panel = new JPanel();
        getContentPane().add(panel);

        JLabel lblRektaszension = new JLabel("Rektaszension");
        panel.add(lblRektaszension);

        JLabel lblDeklination = new JLabel("Deklination");
        panel.add(lblDeklination);

        JLabel lblDistanz = new JLabel("Distanz");
        panel.add(lblDistanz);

        JPanel panel_1 = new JPanel();
        getContentPane().add(panel_1);

        rektField = new JTextField();
        panel_1.add(rektField);
        rektField.setColumns(10);

        dekField = new JTextField();
        panel_1.add(dekField);
        dekField.setColumns(10);

        distanceField = new JTextField();
        panel_1.add(distanceField);
        distanceField.setColumns(10);

        startButton = new JButton("StartSimulation");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSimulation(pt);
            }
        });
        getContentPane().add(startButton);

        initPlanets();
        initTextfields();

        setVisible(true);
        setAlwaysOnTop(true);

        planetDropdown.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    startSimulation(pt);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void startSimulation(PhysicThread pt) {
        pt.runPhysicsSimulation = true;
        FeedbackBuilder.writtenPlanet = currentSelectedPlanet;
        FeedbackBuilder.writtenPlanetFileName = currentSelectedPlanet + nameField.getText();
        setVisible(false);
    }

    private void initTextfields() {
        rektField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressedSimple(e, rektField);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        dekField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressedSimple(e, dekField);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void handleKeyPressedSimple(KeyEvent e, JTextField textField) {
        boolean deleting = e.getKeyCode() == KeyEvent.VK_BACK_SPACE;
        int index = textField.getText().length();
        if(deleting) {
            switch (index) {
                case 3:
                    textField.setText(textField.getText().substring(0,2));
                    break;
                case 6:
                    textField.setText(textField.getText().substring(0,5));
                    break;
                case 9:
                    textField.setText(textField.getText().substring(0,8));
                    break;
            }
        } else {
            switch (index) {
                case 2:
                case 5:
                    textField.setText(textField.getText() + "/");
                    break;
                case 8:
                    textField.setText(textField.getText() + ".");
                    break;
            }
        }
    }

    private void initPlanets() {
        int marsIndex = -1;
        int index = 0;
        for(PhysikObjekt o: PhysicThread.physikObjekte) {
            planetDropdown.addItem(o.name);
            if(o.name.equals("mars"))
                planetDropdown.setSelectedItem(o.name);
            index++;
        }
//        planetDropdown.getItemAt(marsIndex);
        planetDropdown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                    currentSelectedPlanet = (String) e.getItem();
            }
        });
        currentSelectedPlanet = (String) planetDropdown.getSelectedItem();
    }

}
