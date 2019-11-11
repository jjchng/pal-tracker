package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    public Map<String, String> envMap = new HashMap<>();


//    private final String port;
//    private final String memoryLimit;
//    private final String cfInstanceIndex;
//    private final String cfInstanceAddress;
//
//    public EnvController(
//            @Value("${port:NOT SET}") String port,
//            @Value("${memory.limit:NOT SET}") String memoryLimit,
//            @Value("${cf.instance.index:NOT SET}") String cfInstanceIndex,
//            @Value("${cf.instance.addr:NOT SET}") String cfInstanceAddress
//    ) {
//        this.port = port;
//        this.memoryLimit = memoryLimit;
//        this.cfInstanceIndex = cfInstanceIndex;
//        this.cfInstanceAddress = cfInstanceAddress;
//    }
    public EnvController (@Value("${port:NOT SET}") String port, @Value("${memory.limit:NOT SET}") String aMemLimit,
                          @Value("${cf.instance.index:NOT SET}") String aCFIndex, @Value("${cf.instance.address:NOT SET}") String aCFInstance) {
        this.envMap.put("PORT", port);
        this.envMap.put("MEMORY_LIMIT", aMemLimit);
        this.envMap.put("CF_INSTANCE_INDEX", aCFIndex);
        this.envMap.put("CF_INSTANCE_ADDR", aCFInstance);

    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {
        return this.envMap;
    }

//    @GetMapping("/env")
//    public Map<String, String> getEnv() {
//        Map<String, String> env = new HashMap<>();
//
//        env.put("PORT", port);
//        env.put("MEMORY_LIMIT", memoryLimit);
//        env.put("CF_INSTANCE_INDEX", cfInstanceIndex);
//        env.put("CF_INSTANCE_ADDR", cfInstanceAddress);
//
//        return env;
//    }
}
