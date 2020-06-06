package model.factory;

public class OGFactoryProducer {
    public static OGAbstractFactory getFactory(int id) {
        return new ObjetoGraficoFactory();
    }
}