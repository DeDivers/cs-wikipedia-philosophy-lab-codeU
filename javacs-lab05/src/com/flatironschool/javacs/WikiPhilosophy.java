package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started

		String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);
		String baseUrl = "https://en.wikipedia.org";
		String location = "";
		List<String> visited = new ArrayList<>();
		Element firstPara = paragraphs.get(0);
		boolean notFound = true;
		boolean first = false;
		int i = 0;
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		visited.add(url);
		while (notFound && i < 7) {
			for (Node node: iter) {
				if (node instanceof Element) {
					Element e2 = (Element) node;
					String e2txt = e2.text();
					Elements e = e2.children();
					for (Node n : e) {
						if (n instanceof Element) {
							Element e3 = (Element) n;
							if (!first) {
								if (e3.tag().toString().equals("a")) {
									if (e2txt.indexOf("(") < e2txt.indexOf(e3.text()) && e2txt.indexOf(")") > e2txt.indexOf(e3.text())) {

									} else {
										first = true;
										String str = e3.toString();
										String[] arr = str.split(" ");
										location = arr[1].substring(6, arr[1].length() - 1);
									}
								}
							}
						}
					}
					if (!first) {
						System.out.println("No links found, exiting.");
						return;
					}
				}
			}
			paragraphs = wf.fetchWikipedia(baseUrl + location);
			iter = new WikiNodeIterable(paragraphs.get(0));
			first = false;
			if (visited.contains(baseUrl + location)) {
				System.out.println("Links go in a loop, exiting");
				return;
			}
			visited.add(baseUrl + location);
			i++;
			if (location.substring(6).equals("Philosophy")) {
				notFound = false;
				System.out.println("Philosophy found in " + i + " jumps");
			}
        }
        System.out.println(visited);
	}
}
