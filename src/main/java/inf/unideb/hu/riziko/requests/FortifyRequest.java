package inf.unideb.hu.riziko.requests;

import lombok.Getter;
import lombok.Setter;

public class FortifyRequest extends BaseRequest {
    @Getter @Setter
    String from;
    @Getter @Setter
    String to;
    @Getter @Setter
    int amount;

}
