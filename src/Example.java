import model.Game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Example {
    static Game game;

     Example(){
        game = new Game();
        setupListeners();
        game.start();
    }

    void setupListeners(){
        PropertyChangeListener currencyListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                System.out.printf("currencyChange: {\n\tpropertyName: %s,\n\tnewValue: %s}\n", evt.getPropertyName(), evt.getNewValue());

            }
        };
        PropertyChangeListener producerListener = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                //System.out.println("evt = [" + evt + "]");
                System.out.printf("producerChange: {\n\tpropertyName: %s,\n\tnewValue: %s}\n", evt.getPropertyName(), evt.getNewValue());
            }
        };
        game.getCurrency().addPropertyChangeListener(currencyListener);
        game.getManualProducer().addPropertyChangeListener(producerListener);
//        game.getProducers().forEach(producer -> producer.addPropertyChangeListener(producerListener));

    }
    void doClick(){
         game.getManualProducer().produce();
    }



    public static void main(String[] args){
         Example ex = new Example();
         while(true){
             ex.doClick();
             if(game.getManualProducer().tryPurchase())
                 System.out.println("Level up!");
             try {
                 Thread.sleep(3000);
             }
             catch (InterruptedException e){

             }
         }

    }

}
