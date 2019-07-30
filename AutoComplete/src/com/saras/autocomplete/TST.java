package com.saras.autocomplete;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * @author Kumar Saras
 * 
 * Simple implementation of TST which can be used for autocomplete feature.
 * It can be enhanced to get result in decreasing order of hits.
 *
 */
public class TST {
	
	private Node root;
	int n;
	private static class Node{
		private char c;
		private int val;
		private Node left, mid, right;
	}
	
	public int get(String key) {
		if(key == null) {
			throw new IllegalArgumentException();
		}
		
		if(key.length() == 0) {
			throw new IllegalArgumentException();
		}
		
		Node x = get(root, key, 0);
		if(x == null) {
			return Integer.MIN_VALUE;
		}
		return x.val;
	}
	
	public boolean contains(String key) {
		if(key == null) {
			throw new IllegalArgumentException();
		}
		
		return get(key) != Integer.MIN_VALUE;
	}
	
	private Node get(Node x, String key, int d) {
		if(x == null) {
			return null;
		}
		if(key.length() == 0) {
			throw new IllegalArgumentException();
		}
		
		char c = key.charAt(d);
		
		if(c < x.c) {
			return get(x.left, key, d);
		} else if(c > x.c) {
			return get(x.right, key, d);
		} else if (d < key.length() - 1) {
			return get(x.mid, key, d+1);
		} else {
			return x;
		}
	}
	
	public void put(String key, int val) {
		if(key == null || key.length() == 0) {
			throw new IllegalArgumentException();
		}
		
		if(!contains(key)) {
			n++;
		}
		
		root = put(root, key, val, 0);
		
		
	}
	
	private Node put(Node x, String key, int val, int d) {
		char c = key.charAt(d);
		if(x == null) {
			x = new Node();
			x.c = c;
			x.val = Integer.MIN_VALUE;
		}
		
		if(c < x.c) {
			x.left = put(x.left, key, val, d);
		} else if(c > x.c) {
			x.right = put(x.right, key, val, d);
		} else if (d < key.length() - 1) {
			x.mid = put(x.mid, key, val, d+1);
		} else {
			x.val = val;
		}
		
		return x;
	}
	
	public Iterable<String> keysWithPrefixes(String prefix){
		if(prefix == null || prefix.length() == 0) {
			throw new IllegalArgumentException();
		}
		
		LinkedList<String> queue = new LinkedList<>();
		Node x = get(root, prefix, 0);
		if(x == null || x.mid == null) {
			return queue;
		}
		System.out.println("PREFIX NODE:" + x.c + " " + x.val);
		if(x.val != Integer.MIN_VALUE) {
			queue.addFirst(prefix);
		}
		collect(x.mid, new StringBuilder(prefix), queue);
		return queue;
	}
	
	private void collect(Node x, StringBuilder prefix, LinkedList<String> queue) {
		if(x == null) {
			return;
		}
		collect(x.left, prefix, queue);
		if(x.val != Integer.MIN_VALUE) {
			queue.addFirst(prefix.toString() + x.c);
		}
		collect(x.mid, prefix.append(x.c), queue);
		prefix.deleteCharAt(prefix.length() - 1);
		collect(x.right, prefix, queue);
	}
	 
	/**
	 *Testing code.
	  1. Enter words to feed the TST.
	  2. Enter '*' after a prefix to search matches for the prefix
	  3. Enter '-' to shutdown 
	 * @param args
	 */
	public static void main(String[] args) {
		
		TST tst = new TST();
		String string;
		Scanner scanner;
		System.out.println("Enter search string: ");
		
		while(true) {
			scanner = new Scanner(System.in);
			string = scanner.nextLine();
			if(string.contains("*")) {
				String prefix = string.substring(0, string.indexOf('*'));
				for(String key : tst.keysWithPrefixes(prefix)){
					System.out.println("**"+key);
				}
			}
			else if(string.equals("-")) {
				break;
			} else {
				tst.put(string, 1);
			}
		}
		scanner.close();
		
	}

}
