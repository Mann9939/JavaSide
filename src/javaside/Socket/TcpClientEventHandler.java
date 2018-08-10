package javaside.Socket;

public interface TcpClientEventHandler{
    public void onMessage(String line);
    public void onOpen();
    public void onClose();
}
