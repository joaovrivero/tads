package br.edu.ifsul.cstsi.tads_rivero.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/teste")
public class TesteHashController {

    private final PasswordEncoder passwordEncoder;

    public TesteHashController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/gerar-hash/{senha}")
    public Map<String, String> gerarHash(@PathVariable String senha) {
        String hash = passwordEncoder.encode(senha);
        Map<String, String> response = new HashMap<>();
        response.put("senha", senha);
        response.put("hash", hash);
        response.put("valida", String.valueOf(passwordEncoder.matches(senha, hash)));
        return response;
    }

    @GetMapping("/testar-hash")
    public Map<String, String> testarHash(
        @RequestParam String senha,
        @RequestParam String hash
    ) {
        Map<String, String> response = new HashMap<>();
        response.put("senha", senha);
        response.put("hash", hash);
        response.put("valida", String.valueOf(passwordEncoder.matches(senha, hash)));
        return response;
    }
}
