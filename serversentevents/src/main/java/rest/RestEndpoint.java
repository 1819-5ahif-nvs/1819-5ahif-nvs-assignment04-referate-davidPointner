package rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@Path("primes")
public class RestEndpoint {
    private Sse sse;
    private SseBroadcaster sseBroadcaster;
    private OutboundSseEvent.Builder eventBuilder;

    @Context
    public void setSse(Sse sse) {
        this.sse = sse;
        this.eventBuilder = sse.newEventBuilder();
        this.sseBroadcaster = sse.newBroadcaster();
    }

    @GET
    @Produces("text/event-stream")
    public void getVisitors(@Context SseEventSink sseEventSink, @Context Sse sse)
            throws InterruptedException {
        for(int i = 0; i <= 100; i++){
            if(isPrime(i)){
                OutboundSseEvent sseEvent = this.eventBuilder
                        .name("prime")
                        .id("primeNumber")
                        .mediaType(MediaType.APPLICATION_JSON_TYPE)
                        .data(String.class, "new prime number: " + i)
                        .reconnectDelay(3000)
                        .comment("new prime number")
                        .build();
                sseEventSink.send(sseEvent);
            }
            Thread.sleep(100);
        }
        sseEventSink.close();

    }

    boolean isPrime(int n) {
        for(int i=2;i<n;i++) {
            if(n%i==0)
                return false;
        }
        return true;
    }
}
