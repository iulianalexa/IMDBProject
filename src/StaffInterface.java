public interface StaffInterface {
    void addProductionSystem(Production p);
    void addActorSystem(Actor a);
    void removeProductionSystem(Production production);
    void removeActorSystem(Actor actor);
    @SuppressWarnings("unused")
    void updateProduction(Production p);
    @SuppressWarnings("unused")
    void updateActor(Actor a);

    void closeRequest(Request request, boolean solved);

    void solveRequest(Request request);
}
