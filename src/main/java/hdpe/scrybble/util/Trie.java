package hdpe.scrybble.util;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author Ryan Pickett
 *
 */
public class Trie implements Collection<String> {
	
	private Node rootNode = new Node();
	private int size;
	private boolean empty;
	
	/**
	 * 
	 */
	public Trie() {
		
	}
	
	/**
	 * @param words
	 */
	public Trie(Collection<String> words) {
		addAll(words);
	}

	public Iterator<String> iterator() {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return size;
	}
	
	public boolean add(String e) {
		getOrCreateNode(e.toLowerCase());
		return true;
	}
	
	public boolean addAll(Collection<? extends String> c) {
		boolean result = false;
		for (String s : c) {
			result |= add(s);
		}
		return result;
	}

	public void clear() {
		rootNode = new Node();
		size = 0;
		empty = true;
	}
	
	/**
	 * @param word
	 * @return
	 */
	public Node getNode(String word) {
		return rootNode.getChild(word.toLowerCase());
	}

	public boolean contains(Object o) {
		return o instanceof String && getNode(((String)o)) != null;
	}
	
	/**
	 * @param word
	 * @return
	 */
	public boolean containsWord(String word) {
		Node node = getNode(word);
		return node != null && node.terminal;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object object : c) {
			if (!contains(object)) {
				return false;
			}
		}
		return true;
	}

	public boolean isEmpty() {
		return empty;
	}

	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}
	
	private Node getOrCreateNode(String str) {
		try {
			return getOrCreateNode(str.charAt(0), str.length() == 0 ? "" : 
				str.substring(1), rootNode);
		} catch (InvalidCharacterException e) {
			System.err.println(e);
			return null;
		}
	}
	
	private Node getOrCreateNode(char head, String tail, Node parent) throws InvalidCharacterException {
		Node node = parent.getChild(head);
		if (node == null) {
			node = new Node();
			parent.setChild(head, node);
			size ++;
		}
		if (tail.length() > 0) {
			getOrCreateNode(tail.charAt(0), tail.substring(1), node);
		} else {
			node.terminal = true;
		}
		return node;
	}
	
	/**
	 * @author Ryan Pickett
	 *
	 */
	public static class Node {
		
		private Node[] children = new Node[26];
		private boolean terminal = false;
		
		
		Node getChild(char c) {
			return validCharacter(c) ? children[c - 97] : null;
		}
		
		Node getChild(String word) {
			if (word.length() == 0) {
				return this;
			}
			char c =  word.charAt(0);
			Node child = getChild(c);
			if (child != null) {
				return child.getChild(word.substring(1));
			}
			return null;
		}
		
		/**
		 * @return
		 */
		public boolean isTerminal() {
			return terminal;
		}
		
		void setChild(char c, Node n) throws InvalidCharacterException {
			if (!validCharacter(c)) {
				throw new InvalidCharacterException(c);
			}
			children[c - 97] = n;
		}
		
		boolean validCharacter(char c) {
			return c >= 97 && c < 123;
			
		}
	}
}