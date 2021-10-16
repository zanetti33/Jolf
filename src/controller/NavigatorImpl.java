package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class NavigatorImpl implements Navigator {
	
	public static final String SEP = File.separator;
	public static final String FILE_NAME = System.getProperty("user.home") + SEP + "Desktop"
			+ SEP + "workspace" + SEP + "Jolf" + SEP + "leaderboard.txt";

	@Override
	public Map<String,Integer> getLeaderboard() throws IOException {
		Map<String,Integer> leaderboard = new HashMap<>();
		
		try (final BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
			Optional<String> s = Optional.ofNullable(reader.readLine());
			while (s.isPresent()) {
				String[] line = s.get().split("=");
				String name = line[0];
				int score = Integer.parseInt(line[1]);
				if (leaderboard.containsKey(name)) {
					if (score < leaderboard.get(name)) {
						leaderboard.put(name, score);
					}
				} else {
					leaderboard.put(name, score);
				}
				//writeOnLeaderboard(name, score-10);      //test
				s = Optional.ofNullable(reader.readLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return sortByValue(leaderboard);
	}
	
	@Override
	public void writeOnLeaderboard(String player, int score) throws IOException {
		//TODO: Fix writing on files... it always deletes before writing
		try (final BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
			String line = player + "=" + score;
			writer.append(line);
			writer.newLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<String> getMaps() {
		return List.of();
	}

	@Override
	public List<String> getCourses() {
		return List.of();
	}
	
	private <K, V extends Comparable<? super V>> Map<K,V> sortByValue(Map<K,V> map) {
		List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        System.out.println(result);
		return result;
	}

}
