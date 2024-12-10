package inf.unideb.hu.riziko.requests;

import lombok.Getter;
import lombok.Setter;

public class CombatRequest extends BaseRequest {

    @Getter @Setter
    private String from;
    @Getter @Setter
    private String to;
}
