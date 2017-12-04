package by.bstu.feis.ii12.service;

import by.bstu.feis.ii12.exception.NoWordException;
import by.bstu.feis.ii12.model.LangFunc;
import javafx.util.Pair;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslationService {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public TranslationService(DriverManagerDataSource dataSource) {
        jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        langFuncs = jdbcTemplate.query("select * from Func", new FuncRowMapper());

        for (LangFunc func : langFuncs) {
            List<String> rusPlaceHolders = jdbcTemplate.queryForList("select PosRus from Pos where IDFunc = " + func.getFuncId().toString(), new HashMap<>(), String.class);
            func.setRusPlaceHolders(rusPlaceHolders);

            List<String> engPlaceHolders = jdbcTemplate.queryForList("select PosEng from Pos where IDFunc = " + func.getFuncId().toString(), new HashMap<>(), String.class);
            func.setEngPlaceHolders(engPlaceHolders);

            List<Pair<String, String>> fDict =
                    jdbcTemplate.query(
                            "select * from Func_Wrd fw join Dict d on fw.IDWrd = d.IDWrd where fw.IDFunc = " + func.getFuncId().toString(),
                            new HashMap<>(),
                            (resultSet, i) -> new Pair<>(resultSet.getString("WrdRus"), resultSet.getString("WrdEng"))
                    );

            HashMap<String, String> fDictMap = new HashMap<>();

            for (Pair<String, String> pair : fDict) {
                fDictMap.put(pair.getKey(), pair.getValue());
            }

            func.setSuitableWords(fDictMap);
        }

        List<Pair<String, String>> cDict =
                jdbcTemplate.query(
                        "select * from Dict",
                        new HashMap<>(),
                        (resultSet, i) -> new Pair<>(resultSet.getString("WrdRus"), resultSet.getString("WrdEng"))
                );

        dict = new HashMap<>();

        for (Pair<String, String> pair : cDict) {
            dict.put(pair.getKey(), pair.getValue());
        }

        Collections.sort(langFuncs, new Comparator<LangFunc>() {
            @Override
            public int compare(LangFunc o1, LangFunc o2) {
                Integer o1_c = o1.getfTxtRus().split("\\s+").length;
                Integer o2_c = o2.getfTxtRus().split("\\s+").length;

                return o1_c > o2_c ? -1 : (o1_c < o2_c) ? 1 : 0;
            }
        });
    }

    private List<LangFunc> langFuncs;
    private HashMap<String, String> dict;

    public String translate(String input, Map<String, String> localDict) {
        String normInput = input.toLowerCase().trim();

        // Lang functions block
        for (LangFunc langFunc : langFuncs) {
            Pattern pattern = Pattern.compile(langFunc.getRegexString());
            Matcher matcher = pattern.matcher(normInput);

            List<Pair<String, String>> instructions = new ArrayList<>();

            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();

                List<String> placeholderWords = new ArrayList<>(); // Get x1 x2 ... words

                for (String placeholder : langFunc.getRusPlaceHolders()) {
                    placeholderWords.add(matcher.group(placeholder));
                }

                for (int i = 0; i < placeholderWords.size(); i++) { // Translate them
                    placeholderWords.set(i,
                                         translateWordForFunc(placeholderWords.get(i),
                                                              langFunc.getSuitableWords(),
                                                              localDict));
                }

                String eng = langFunc.getfTxtEng(); // Form english expression

                for (int i = 0; i < langFunc.getEngPlaceHolders().size(); i++) {
                    eng = eng.replaceAll(langFunc.getEngPlaceHolders().get(i), placeholderWords.get(i));
                }

                instructions.add(new Pair<>(normInput.substring(start, end), eng));
            }

            for (Pair<String, String> instruction : instructions) {
                normInput = normInput.replaceAll(instruction.getKey(), instruction.getValue());
            }
        }

        // Other words

        Pattern pattern = Pattern.compile("(?<word>[а-яА-Я\\-]+)");
        Matcher matcher = pattern.matcher(normInput);

        List<Pair<String, String>> instructions = new ArrayList<>();

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            String rus = normInput.substring(start, end);
            String eng = translateWord(rus, localDict);

            instructions.add(new Pair<>(rus, eng));
        }

        for (Pair<String, String> instruction : instructions) {
            normInput = normInput.replaceAll(instruction.getKey(), instruction.getValue());
        }

        return normInput;
    }

    private String translateWordForFunc(String rus, Map<String, String> funcDict, Map<String, String> localDict) {
        String eng = funcDict.get(rus);

        if (eng == null && localDict != null) {
            eng = localDict.get(rus);
        }

        if (eng == null) {
            throw new NoWordException("Word " + rus + " is not found", rus);
        }

        return eng;
    }

    private String translateWord(String rus, Map<String, String> localDict) {
        String eng = dict.get(rus);

        if (eng == null && localDict != null) {
            eng = localDict.get(rus);
        }

        if (eng == null) {
            throw new NoWordException("Word " + rus + " is not found", rus);
        }

        return eng;
    }

    private static class FuncRowMapper implements RowMapper<LangFunc> {
        @Override
        public LangFunc mapRow(ResultSet resultSet, int i) throws SQLException {
            LangFunc langFunc = new LangFunc();

            langFunc.setFuncId(resultSet.getInt("IDFunc"));
            langFunc.setfTxtRus(resultSet.getString("FTxtRus"));
            langFunc.setfTxtEng(resultSet.getString("FTxtEng"));

            return langFunc;
        }
    }

}
