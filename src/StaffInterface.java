public interface StaffInterface {
    void addProductionSystem(Production p);
    void addActorSystem(Actor a);
    void removeProductionSystem(Production production);
    void removeActorSystem(Actor actor);

    /**
     * Unused because it only has one parameter, therefore I cannot change the title. I prefer to remove and re-add.
     */
    @SuppressWarnings("unused")
    void updateProduction(Production p);

    /**
     * Unused because it only has one parameter, therefore I cannot change the name. I prefer to remove and re-add.
     */
    @SuppressWarnings("unused")
    void updateActor(Actor a);

    void closeRequest(Request request, boolean solved);

    void solveRequest(Request request);
}
