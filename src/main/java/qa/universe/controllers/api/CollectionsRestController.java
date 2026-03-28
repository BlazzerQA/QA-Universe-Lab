package qa.universe.controllers.api;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/collections")
public class CollectionsRestController {

    private List<String> logs = new ArrayList<>(Arrays.asList("Started","Connected"));

    private Set<Integer> uniqueUsers = new HashSet<>(Arrays.asList(101,102,103));

    private Map<String,String> config = new HashMap<>() {{
        put("env", "QA");
        put("timeout", "5000");
    }};

    @GetMapping("/list")
    public List<String> getLog() {
        return logs;
    }

    @PostMapping("/list")
    public void addLog(@RequestParam String log) {
        logs.add(log);
    }

    @GetMapping("/set")
    public Set<Integer> getUsers() {
        return uniqueUsers;
    }

    @PostMapping("/set")
    public String addUser(@RequestParam Integer id) {
        boolean added = uniqueUsers.add(id);
        return added ? "User added" : "User already exists!";
    }

    @GetMapping("/map")
    public Map<String, String> getConfig() {
        return config;
    }


}
