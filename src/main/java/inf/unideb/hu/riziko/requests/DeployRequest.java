package inf.unideb.hu.riziko.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class DeployRequest extends BaseRequest {
    @Getter @Setter
    List<Deployment> deployments = new ArrayList<>();

}
