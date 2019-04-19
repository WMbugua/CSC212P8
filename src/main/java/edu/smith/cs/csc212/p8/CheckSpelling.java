package edu.smith.cs.csc212.p8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		List<String> words;
		try {
			// Read from a file:
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		return words;
	}
	
	/**
	 * Load project Gutenberg book to words.
	 * @param filePath try something like "PrideAndPrejudice.txt"
	 * @return a list of words in the book, in order.
	 */
	public static List<String> loadBook(String filePath) {
		long start = System.nanoTime();
		List<String> words = new ArrayList<>();
		try {
			// Read from a file:
			for (String line : Files.readAllLines(new File(filePath).toPath())) {
				words.addAll(WordSplitter.splitTextToWords(line));
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " from book in " + time +" seconds.");
		return words;
	}
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		List<String> misspelled = new ArrayList<>();
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
			else {
				misspelled.add(w);
			}
		}
		
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double misspelledRatio = misspelled.size()/(double)words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println(dictionary.getClass().getSimpleName()+": Lookup of items found="+fractionFound+" time="+nsPerItem+" ns/item");
		System.out.println("The ratio of misspelled words is "+misspelledRatio);
		for (int i=0; i<Math.min(10, misspelled.size());i++) {
			System.out.println(misspelled.get(i));
		}
	}
	
	public static List<String> createMixedDataset(List<String> yesWords, int numSamples, double fractionYes) {
		// Hint to the ArrayList that it will need to grow to numSamples size:
		List<String> output = new ArrayList<>(numSamples);
		// select numSamples * fractionYes words from yesWords; create the rest as no words.
		double numYesWords = numSamples * fractionYes;
		Random rand = new Random();
		int start = rand.nextInt(yesWords.size()/5);
		int end = start + (int)numYesWords;
		List<String> yayWords = yesWords.subList(start,end);
		output.addAll(yayWords);
		int N = output.size();
		for (String word: yesWords){
		if(N!=0 && output.size()==numSamples) {
				break;
			}
			else {
				String newWord = word+"wrt";
				output.add(newWord);
				continue;
			}
		}
		return output;
	}
	
	
	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		
		//---Load the book.
		List<String> bookWords = loadBook("74-0.txt");
		
		// --- Create a bunch of data structures for testing:
		//Calculate the time spent to insert an item into each data structure
		long startTreeInsertion = System.nanoTime();
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		long endTreeInsertion = System.nanoTime();
		double Treetime = (endTreeInsertion - startTreeInsertion) / 1e9;
		double timeSpentperTreeInsertion = (endTreeInsertion - startTreeInsertion)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the TreeSet in " + Treetime +" seconds, at "+ timeSpentperTreeInsertion + "ns/word");
		
		long startTreeInsertion2 = System.nanoTime();
		TreeSet<String> treeofWords = new TreeSet<>();
		for (String w : listOfWords) {
			treeofWords.add(w);
		}
		long endTreeInsertion2 = System.nanoTime();
		double Treetime2 = (endTreeInsertion2 - startTreeInsertion2) / 1e9;
		double timeSpentperTreeInsertion2 = (endTreeInsertion2 - startTreeInsertion2)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the TreeSet2 in " + Treetime2 +" seconds, at "+ timeSpentperTreeInsertion2 + "ns/word");
		
		long startHashInsertion = System.nanoTime();
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		long endHashInsertion = System.nanoTime();
		double Hashtime = (endHashInsertion - startHashInsertion) / 1e9;
		double timeSpentperHashInsertion = (endHashInsertion - startHashInsertion)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the HashSet in " + Hashtime +" seconds, at "+ timeSpentperHashInsertion + "ns/word");
		
		long startHashInsertion2 = System.nanoTime();
		HashSet<String> hashofWords = new HashSet<>();
		for (String w : listOfWords) {
			hashofWords.add(w);
		}
		long endHashInsertion2 = System.nanoTime();
		double Hashtime2 = (endTreeInsertion2 - startTreeInsertion2) / 1e9;
		double timeSpentperHashInsertion2 = (endHashInsertion2 - startHashInsertion2)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the HashSet2 in " + Hashtime2 +" seconds, at "+ timeSpentperHashInsertion2 + "ns/word");
		
		long startListInsertion = System.nanoTime();
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		long endListInsertion = System.nanoTime();
		double Listtime = (endListInsertion - startListInsertion) / 1e9;
		double timeSpentperListInsertion = (endListInsertion - startListInsertion)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the StringList in " + Listtime +" seconds, at "+ timeSpentperListInsertion + "ns/word");
		
		if (bsl.contains("zzjakasdlfkjasldkjf")) {
			throw new RuntimeException();
		}
		
		long startTrieInsertion = System.nanoTime();
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);
		}
		long endTrieInsertion = System.nanoTime();
		double Trietime = (endTrieInsertion - startTrieInsertion) / 1e9;
		double timeSpentperTrieInsertion = (endTrieInsertion - startTrieInsertion)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the CharTrie in " + Trietime +" seconds, at "+ timeSpentperTrieInsertion + "ns/word");
		
		long startLLHashInsertion = System.nanoTime();
		LLHash hm100k = new LLHash(100000);
		for (String w : listOfWords) {
			hm100k.add(w);
		}
		long endLLHashInsertion = System.nanoTime();
		double LLHashtime = (endLLHashInsertion - startLLHashInsertion) / 1e9;
		double timeSpentperLLHashInsertion = (endLLHashInsertion - startLLHashInsertion)/((double) listOfWords.size());
		System.out.println("Inserted " + listOfWords.size() + " entries into the LLHash in " + LLHashtime +" seconds, at "+ timeSpentperLLHashInsertion + "ns/word");
		
		// --- Make sure that every word in the dictionary is in the dictionary:
		/**timeLookup(listOfWords, treeOfWords);
		timeLookup(listOfWords, hashOfWords);
		timeLookup(listOfWords, bsl);
		timeLookup(listOfWords, trie);
		timeLookup(listOfWords, hm100k);*/
		
		// --- Make sure that every word in the book is in the dictionary:
		timeLookup(bookWords, treeOfWords);
		timeLookup(bookWords, hashOfWords);
		timeLookup(bookWords, bsl);
		timeLookup(bookWords, trie);
		timeLookup(bookWords, hm100k);
		
		for (int i=0; i<10; i++) {
			// --- Create a dataset of mixed hits and misses with p=i/10.0
			List<String> hitsAndMisses = createMixedDataset(listOfWords, 10_000, i/10.0);
			
			// --- Time the data structures.
			timeLookup(hitsAndMisses, treeOfWords);
			timeLookup(hitsAndMisses, hashOfWords);
			timeLookup(hitsAndMisses, bsl);
			timeLookup(hitsAndMisses, trie);
			timeLookup(hitsAndMisses, hm100k);
		}
			

		
		// --- linear list timing:
		// Looking up in a list is so slow, we need to sample:
		System.out.println("Start of list: ");
		timeLookup(listOfWords.subList(0, 1000), listOfWords);
		System.out.println("End of list: ");
		timeLookup(listOfWords.subList(listOfWords.size()-100, listOfWords.size()), listOfWords);
		
	
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		System.out.println("Done!");
	}
}
