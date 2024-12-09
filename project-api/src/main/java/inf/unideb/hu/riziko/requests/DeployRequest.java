package inf.unideb.hu.riziko.requests;

import lombok.Getter;
import lombok.Setter;

public class DeployRequest extends BaseRequest {

    @Getter @Setter
    String deploy;
    @Getter @Setter
    int amount;


}
