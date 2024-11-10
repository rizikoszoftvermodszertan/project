package inf.unideb.hu.riziko.websocket.response;

public class BadRequestResponse extends Response {
    public BadRequestResponse() {
        super(0, 400);
        message = "Bad Request";
    }
}
