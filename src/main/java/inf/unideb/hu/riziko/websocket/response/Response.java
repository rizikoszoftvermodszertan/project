package inf.unideb.hu.riziko.websocket.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class Response {
    @JsonProperty
    int id;
    @JsonProperty
    int statusCode;
    @JsonProperty
    String message;

    public Response(int id, int statusCode){
        this.id = id;
        this.statusCode = statusCode;
    }


}
