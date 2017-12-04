package by.bstu.feis.ii12.controller;

import by.bstu.feis.ii12.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class TranslationController {

    private TranslationService translationService;

    @Autowired
    public TranslationController(TranslationService translationService) {
        this.translationService = translationService;
    }

    @PostMapping("/translate")
    @ResponseBody
    public Map<String, Object> translate(@RequestBody TranslateRequest request) {
        Map<String, Object> response = new HashMap<>();

        response.put("text", translationService.translate(request.getText(), request.getDict()));
        response.put("status", "ok");

        return response;
    }

    private static class TranslateRequest {
        private String text;
        private Map<String, String> dict;

        public TranslateRequest() {
        }

        public TranslateRequest(String text, Map<String, String> dict) {
            this.text = text;
            this.dict = dict;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Map<String, String> getDict() {
            return dict;
        }

        public void setDict(Map<String, String> dict) {
            this.dict = dict;
        }
    }

}
