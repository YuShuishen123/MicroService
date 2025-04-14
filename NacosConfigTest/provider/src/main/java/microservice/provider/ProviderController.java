package microservice.provider;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yu'S'hui'shen
 */
@RestController
@RefreshScope
public class ProviderController {

    @Value("${a}")
    private String a;

    @GetMapping("/a")
    public String getA(){
        return a;
    }
}
