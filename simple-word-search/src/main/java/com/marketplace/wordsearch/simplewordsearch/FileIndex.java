package com.marketplace.wordsearch.simplewordsearch;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class FileIndex {

    private Map<String, List<FileMeta>> indexData = new ConcurrentHashMap<>();

    public void indexFile (List<File> files) throws IOException {
        for (File file : files) {
            Map<String, Integer> wordCountEachFile = new HashMap<>();
            FileReader in = new FileReader(file);
            BufferedReader br = new BufferedReader(in);
            readLineExcludeStopWord(wordCountEachFile, br);
            mapWordToFileIndex(file, wordCountEachFile);
        }

    }

    private void readLineExcludeStopWord(Map<String, Integer> wordCountEachFile, BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            String[] splitWords = line.split("([.,@^*#$%&(){}!?:;'\"-]|\\s)+");
            for (String word : splitWords) {
                word = word.toLowerCase();
                if (!isStopWord(word)) {
                    mapWordCountForFile(wordCountEachFile, word);
                }
            }
        }
    }

    private void mapWordCountForFile(Map<String, Integer> wordCountEachFile, String word) {
        wordCountEachFile.compute(word, (w,count) -> count == null ? 1 : count + 1);
    }

    private void mapWordToFileIndex(File file, Map<String, Integer> wordCountEachFile) {
        for (Map.Entry<String, Integer> entry : wordCountEachFile.entrySet()) {
            FileMeta fileMeta = new FileMeta();
            fileMeta.fileName = file.getName();
            fileMeta.wordCount = entry.getValue();
            List<FileMeta> fileMetas = indexData.get(entry.getKey()) == null ? new ArrayList<>() : indexData.get(entry.getKey());
            fileMetas.add(fileMeta);
            indexData.putIfAbsent(entry.getKey(), fileMetas);
        }
    }

    private boolean isStopWord (String word) {
        String[] stopWrds={"i", "a", "about","an","are","as","at","be","by","com","for","from","how","in", "is","it",
                "of","on","or","that","the","this","to","was","what","when","where","who","will","with"};
        List<String> stopWords = Arrays.asList(stopWrds);
        return stopWords.contains(word);

    }

    public Map<String, Integer> getFilesForSearchQuery (String[] searchWords) {
        Map<String, Integer> searchWordStats = new HashMap<>();
        for (String word : searchWords) {
            List<FileIndex.FileMeta> fileMetas = indexData.get(word);
            calculateSearchWordFrequencyAcrossIndexFiles(searchWordStats, fileMetas);
        }
        return searchWordStats;
    }

    private void calculateSearchWordFrequencyAcrossIndexFiles(Map<String, Integer> searchWordStats, List<FileMeta> fileMetas) {
        if (fileMetas != null) {
            for (FileMeta fileMeta: fileMetas){
                mapWordCountForFile(searchWordStats, fileMeta.fileName);
            }
        }
    }


    public static class FileMeta{
        private String fileName;
        private Integer wordCount;

        public String getFileName() {
            return fileName;
        }

        public Integer getWordCount() {
            return wordCount;
        }



        @Override
        public boolean equals (Object o) {
        if (o == this) return true;

        if (!(o instanceof FileMeta))
            return false;

        FileMeta other = (FileMeta) o;
            return other.fileName.equals(fileName)
                    && other.wordCount.equals(wordCount);
        }

    @Override
    public int hashCode() {
        int result = 37;
        result = result + result * fileName.hashCode();
        result = result + result * wordCount.hashCode();
        return result;
    }

    }
}
