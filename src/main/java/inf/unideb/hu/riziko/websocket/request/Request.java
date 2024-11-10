package inf.unideb.hu.riziko.websocket.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import inf.unideb.hu.riziko.websocket.response.Response;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = RegisterRequest.class, name = "register"),
        @JsonSubTypes.Type(value = ReconnectRequest.class, name = "reconnect")
})
public abstract class Request {
    @JsonProperty
    int requestid;
    public abstract Response respond();
}
