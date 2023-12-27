public interface Subject {
    void subscribe(Observer observer);
    void unsubscribe(Observer observer);
    void sendNotification(String notificationType, String message);
}
