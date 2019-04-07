package com.marketplace.wordsearch.simplewordsearch;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    @Mock
    private FileIndex fileIndex;
    private SearchService searchService;


    @Before
    public void init() throws FileNotFoundException {
        searchService = new SearchService(fileIndex);
    }

    @Test
    public void shouldReturnHitForBothFiles() throws IOException {
        Map<String, Integer> filesWithSearchHit = new HashMap<>();
        filesWithSearchHit.put("newTextFile.txt",2);
        filesWithSearchHit.put("newTextFile1.txt",2);

        String[] searchWords = {"blue","ocean"};
        when(fileIndex.getFilesForSearchQuery(searchWords)).thenReturn(filesWithSearchHit);

        Map<String, Float> filesNamesWithSearchHit = searchService.searchData("blue ocean");

        assertEquals(filesNamesWithSearchHit.get("newTextFile.txt"), new Float(100.0));
        assertEquals(filesNamesWithSearchHit.get("newTextFile1.txt"), new Float(100.0));
    }

    @Test
    public void shouldReturnMaxHitForOneFiles() throws IOException {
        Map<String, Integer> filesWithSearchHit = new HashMap<>();
        filesWithSearchHit.put("newTextFile.txt",1);
        filesWithSearchHit.put("newTextFile1.txt",2);

        String[] searchWords = {"green","environment"};
        when(fileIndex.getFilesForSearchQuery(searchWords)).thenReturn(filesWithSearchHit);

        Map<String, Float> filesNamesWithSearchHit = searchService.searchData("green environment");

        assertEquals(filesNamesWithSearchHit.get("newTextFile.txt"), new Float(50.0));
        assertEquals(filesNamesWithSearchHit.get("newTextFile1.txt"), new Float(100.0));
    }

}
