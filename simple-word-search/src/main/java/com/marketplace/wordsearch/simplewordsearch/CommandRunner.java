package com.marketplace.wordsearch.simplewordsearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class CommandRunner implements CommandLineRunner {

    private FileIndex fileIndex;
    private SearchService searchService;

    @Autowired
    public CommandRunner (FileIndex fileIndex, SearchService searchService) {
        this.fileIndex = fileIndex;
        this.searchService = searchService;
    }
    private static Logger LOG = LoggerFactory
            .getLogger(CommandRunner.class);

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            throw new IllegalArgumentException("No Directory available for Indexes");
        }
        List<File> filesInFolder = Files.walk(Paths.get(args[0]))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .filter(file -> file.getName().endsWith(".txt"))
                .collect(Collectors.toList());

        if (filesInFolder.size() > 10) {
            throw new IllegalArgumentException("Can't handle more than 10 files as yet.");
        }
        fileIndex.indexFile(filesInFolder);
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("search>");
            String searchData = input.nextLine();
            if (searchData.equalsIgnoreCase("exit")) {
                System.exit(0);
            }
            Map<String, Float> results = searchService.searchData(searchData);
            if (results == null) {
                System.out.println("No results found");
            }
            results.entrySet().forEach(entry -> System.out.println(entry.getKey() + "-" + entry.getValue()));
        }
    }

}
