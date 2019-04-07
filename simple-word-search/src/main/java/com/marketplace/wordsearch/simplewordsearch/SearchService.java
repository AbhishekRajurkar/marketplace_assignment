package com.marketplace.wordsearch.simplewordsearch;

import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toMap;

@Service
public class SearchService {

    private FileIndex fileIndex;

    public SearchService (FileIndex fileIndex) {
        this.fileIndex = fileIndex;
    }

    public Map<String, Float> searchData(String searchData) {
        String[] searchWords = searchData.toLowerCase().split("([.,@^*#$%&(){}!?:;'\"-]|\\s)+");
        Map<String, Integer> searchWordStats = fileIndex.getFilesForSearchQuery(searchWords);
        return caculateRate(searchWordStats,searchWords.length);
    }

    private Map<String, Float> caculateRate(Map<String, Integer> searchWordStats, int numberOfWordsSearchString) {
        Map<String, Integer> sorted = searchWordStats
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));
        Map<String, Float> percentageRating = new HashMap<>();
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            percentageRating.put(entry.getKey(),(float)(100*entry.getValue())/numberOfWordsSearchString);
        }
        return percentageRating;
    }

}
