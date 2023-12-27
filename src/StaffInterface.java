public interface StaffInterface {
    void addProductionSystem(Production p);
    void addActorSystem(Actor a);
    void removeProductionSystem(String name);
    void removeActorSystem(String name);
    @SuppressWarnings("unused")
    void updateProduction(Production p);
    @SuppressWarnings("unused")
    void updateActor(Actor a);

    void closeRequest(Request request, boolean solved);

    void solveRequest(Request request);
}
