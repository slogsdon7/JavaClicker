import model.Game;
import model.Producer;
import model.Currency;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.concurrent.atomic.AtomicInteger;


public class Example {
    static Game game;
    JFrame frame = new JFrame("Example");
    JTable table = new JTable(10, 4);
     Example(){
        game = new Game();
    }

    void createView() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JPanel());
        Container pane = frame.getContentPane();
        table.setShowGrid(true);
        table.setPreferredSize(new Dimension(500, 500));
        pane.add(table);
        frame.pack();
        frame.setVisible(true);


    }

    void setupListeners() {
        PropertyChangeListener currencyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.println("currencyEvt = [" + evt + "]");
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        table.setValueAt(evt.getNewValue(), 0, 0);
                    }
                });

                //System.out.printf("currencyChange: {\n\tpropertyName: %s,\n\tnewValue: %s}\n", evt.getPropertyName(), evt.getNewValue());
            }
        };
        PropertyChangeListener producerListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {

            }
        };
        game.getCurrency().addPropertyChangeListener(currencyListener);
        game.getManualProducer().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        if (evt.getPropertyName().equals("level"))
                            table.setValueAt("Level " + evt.getNewValue().toString() + game.getManualProducer().getName(), 1, 0);
                        if (evt.getPropertyName().equals("cost"))
                            table.setValueAt(evt.getNewValue(), 1, 1);
                        if (evt.getPropertyName().equals("lastProduced"))
                            table.setValueAt(evt.getNewValue(), 1, 2);

                    }
                });

            }
        });


        AtomicInteger i = new AtomicInteger(1);
        for (Producer producer : game.getAutoProducers()) {

            producer.addPropertyChangeListener(new PropertyChangeListener() {
                int j = i.incrementAndGet();
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    System.out.println("evt = [" + evt + "]");
                    EventQueue.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            if (evt.getPropertyName().equals("level"))
                                table.setValueAt("Level " + evt.getNewValue() + " " + producer.getName(), j, 0);
                            if (evt.getPropertyName().equals("cost"))
                                table.setValueAt(evt.getNewValue(), j, 1);
                            if (evt.getPropertyName().equals("lastProduced"))
                                table.setValueAt(evt.getNewValue(), j, 2);
                        }
                    });

                }
            });
        }

    }
    void doClick(){
         game.getManualProducer().produce();
    }



    public static void main(String[] args){
         Example ex = new Example();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ex.createView();
            }
        });
        ex.setupListeners();
        game.start();
         while(true){
             ex.doClick();
             game.getAutoProducers().forEach(Producer::tryPurchase);
             game.getManualProducer().tryPurchase();
             try {
                 Thread.sleep(3000);
             }
             catch (InterruptedException e){

             }
         }

    }

}

class ExView {

}