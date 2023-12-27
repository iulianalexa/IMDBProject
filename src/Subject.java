public interface Subject {
    void subscribe(String observerType, Observer observer);
    @SuppressWarnings("unused")
    void unsubscribe(String observerType, Observer observer);
    void sendNotification(String notificationType, String message);
}
