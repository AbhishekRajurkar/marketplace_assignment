package com.marketplace.wordsearch.simplewordsearch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class FileIndexTest {

    private List<File> files = new ArrayList();
    private FileIndex fileIndex = new FileIndex();

    @Before
    public void init() throws FileNotFoundException {
        File file_one = ResourceUtils.getFile(this.getClass().getResource("/newTextFile.txt"));
        File file_two = ResourceUtils.getFile(this.getClass().getResource("/newTextFile1.txt"));
        files.add(file_one);
        files.add(file_two);
    }

    @Test
    public void indexFiles_shouldReturnBothFilesWith2HitCount() throws IOException {
        fileIndex.indexFile(files);

        String[] searchWords = {"blue","ocean"};
        Map<String, Integer> filesWithSearchHit = fileIndex.getFilesForSearchQuery(searchWords);

        assertEquals(filesWithSearchHit.size(), (2));
        assertEquals(filesWithSearchHit.get("newTextFile.txt"), new Integer(2));
        assertEquals(filesWithSearchHit.get("newTextFile1.txt"), new Integer(2));
    }

    @Test
    public void indexFiles_shouldReturnBothFilesWith1HitCount() throws IOException {
        fileIndex.indexFile(files);

        String[] searchWords = {"green","revolution"};
        Map<String, Integer> filesWithSearchHit = fileIndex.getFilesForSearchQuery(searchWords);

        assertEquals(filesWithSearchHit.size(), (2));
        assertEquals(filesWithSearchHit.get("newTextFile.txt"), new Integer(1));
        assertEquals(filesWithSearchHit.get("newTextFile1.txt"), new Integer(1));
    }

    @Test
    public void indexFiles_shouldReturnEmptyResult() throws IOException {
        fileIndex.indexFile(files);

        String[] searchWords = {"oracle","java"};
        Map<String, Integer> filesWithSearchHit = fileIndex.getFilesForSearchQuery(searchWords);

        assertEquals(filesWithSearchHit.size(), (0));
    }

    @Test
    public void indexFiles_shouldSkipStopWords() throws IOException {
        fileIndex.indexFile(files);

        String[] searchWords = {"this","is"};
        Map<String, Integer> filesWithSearchHit = fileIndex.getFilesForSearchQuery(searchWords);

        assertEquals(filesWithSearchHit.size(), (0));
    }


}
