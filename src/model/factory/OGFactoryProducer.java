package model.factory;

public class OGFactoryProducer {
    public static OGAbstractFactory getFactory() {
        return new ObjetoGraficoFactory();
    }
}