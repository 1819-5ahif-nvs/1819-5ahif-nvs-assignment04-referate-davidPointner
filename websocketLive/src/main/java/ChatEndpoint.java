import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint("/chat/{name}")
public class ChatEndpoint{
    private Session session;

    @OnOpen
    public void onCreateSession(Session session){
        this.session = session;
    }

    @OnMessage
    public void onTextMessage(String message, @PathParam("name") String name) throws IOException {
        System.out.println("message: " + message);
        if(this.session != null && this.session.isOpen()){
            for (Session session : session.getOpenSessions()){
                session.getBasicRemote().sendText(name + ": " + message);
            }
        }
    }
}